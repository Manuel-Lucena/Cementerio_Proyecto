import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SepulturaService } from '../../services/sepulturaService';
import { Auth } from '../../services/auth';
import { ModalService } from '../../services/modalService';
import { Agrupacion } from '../../models/agrupacion';
import { Sepultura } from '../../models/sepultura';

@Component({
  selector: 'app-modal-sepultura',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './modal-sepultura.html',
  styleUrl: './modal-sepultura.scss'
})export class ModalSepultura implements OnInit, OnChanges {
  @Input() agrupacion!: Agrupacion;
  @Output() cerrar = new EventEmitter<void>();
  @Output() onConcesion = new EventEmitter<Sepultura>();

  sepulturas: Sepultura[] = [];
  cargando: boolean = true;
  isEmpresa: boolean = false;

  constructor(
    private sepulturaService: SepulturaService,
    private auth: Auth,
    private modalService: ModalService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.isEmpresa = this.auth.getRol() === 'EMPRESA';
    if (this.agrupacion) {
      this.cargarSepulturas();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['agrupacion'] && !changes['agrupacion'].firstChange) {
      this.cargarSepulturas();
    }
  }

  cargarSepulturas(): void {
    if (!this.agrupacion?.id) return;

    this.cargando = true;
    this.sepulturas = [];

    this.sepulturaService.listarPorAgrupacion(this.agrupacion.id).subscribe({
      next: (data) => {
        this.sepulturas = data.map(sep => {
   
          const esDisponible = sep.estado?.trim().toUpperCase() === 'DISPONIBLE' || sep.ocupado === false;
          const objetoTransformado = {
            ...sep,
            isOcupado: !esDisponible
          };
          return objetoTransformado;
        });
        this.finalizarCarga();
      },
      error: (err) => {
        console.error("Error:", err);
        this.modalService.mostrar('ERROR', 'No se pudieron cargar las sepulturas.');
        this.finalizarCarga();
      }
    });
  }

 
  private finalizarCarga(): void {
    this.cargando = false;
    this.cdr.detectChanges();
  }

  seleccionarSepultura(sep: Sepultura): void {
    if (this.isEmpresa) return;

    if (!sep.isOcupado) {
      this.onConcesion.emit(sep);
    } else {
      this.modalService.mostrar('OCUPADO', `Esta sepultura no está disponible.`);
    }
  }

  cerrarModal(): void {
    this.cerrar.emit();
  }
}