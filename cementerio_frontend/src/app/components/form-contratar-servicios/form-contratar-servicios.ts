import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Difunto } from '../../models/difunto';
import { DifuntoService } from '../../services/difuntoService';
import { FacturaService } from '../../services/facturaService';

@Component({
  selector: 'app-form-contratar-servicios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './form-contratar-servicios.html',
  styleUrl: './form-contratar-servicios.scss'
})
export class FormContratarServicios implements OnInit {
  @Input() difunto!: Difunto;
  @Output() onConfirmar = new EventEmitter<any>();
  @Output() onCancelar = new EventEmitter<void>();

  serviciosDisponibles: any[] = [];
  serviciosSeleccionados = new Set<any>();
  cargando: boolean = false;

  constructor(
    private difuntoService: DifuntoService,
    private facturaService: FacturaService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    if (this.difunto && this.difunto.idCementerio) {
      this.cargarServicios();
    }
  }

  cargarServicios(): void {
    this.difuntoService.getServiciosPorCementerio(this.difunto.idCementerio).subscribe({
      next: (res) => {
        this.serviciosDisponibles = res;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error cargando servicios:', err)
    });
  }

  toggleServicio(servicio: any): void {
    if (this.serviciosSeleccionados.has(servicio)) {
      this.serviciosSeleccionados.delete(servicio);
    } else {
      this.serviciosSeleccionados.add(servicio);
    }
  }

  get total(): number {
    return Array.from(this.serviciosSeleccionados).reduce((acc, s) => acc + s.coste, 0);
  }

  confirmar(): void {
    if (this.serviciosSeleccionados.size === 0 || this.cargando) return;

    this.cargando = true;

    const idsServicios = Array.from(this.serviciosSeleccionados).map(s => s.id);



    this.facturaService.contratarServicios(this.difunto.id, idsServicios).subscribe({
      next: (facturaGenerada) => {
        this.cargando = false;
        this.onConfirmar.emit(facturaGenerada);
        this.router.navigate(['/mis-facturas']);
      },
      error: (err) => {
        this.cargando = false;
        console.error('Error en la suscripción:', err);
        alert('No se pudo generar la factura. Revisa la consola del backend.');
      }
    });
  }
}