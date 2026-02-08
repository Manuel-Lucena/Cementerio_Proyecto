import { Component, EventEmitter, Input, OnInit, Output, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Servicio } from '../../models/servicio';
import { CementerioServicio } from '../../models/cementerioServicio';
import { ServicioService } from '../../services/servicioService';
import { ModalService } from '../../services/modalService';

@Component({
  selector: 'app-modal-gestion-servicios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-gestion-servicios.html',
  styleUrl: './modal-gestion-servicios.scss'
})
export class ModalGestionServicios implements OnInit {
  @Input() idCementerio: string | null = null;
  @Output() onCerrar = new EventEmitter<void>();

  serviciosGlobales: Servicio[] = [];
  serviciosAsignados: CementerioServicio[] = [];
  cargando: boolean = false;

  constructor(
    private servicioService: ServicioService,
    private modalService: ModalService,
    private cdr: ChangeDetectorRef 
  ) { }

  ngOnInit(): void {
    this.cargarDatos();
  }


  get formularioValido(): boolean {
    return this.serviciosAsignados.every(asig => 
      asig.coste !== null && asig.coste !== undefined && asig.coste >= 0
    );
  }

  cargarDatos(): void {
    if (!this.idCementerio) return;
    const idNum = Number(this.idCementerio);

    this.servicioService.getCatalogo().subscribe({
      next: (data) => {
        this.serviciosGlobales = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error catálogo:', err);
        this.modalService.mostrar('ERROR', 'No se pudo cargar el catálogo.');
      }
    });

    this.servicioService.getServiciosByCementerio(idNum).subscribe({
      next: (data) => {
        this.serviciosAsignados = data;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Error asignados:', err)
    });
  }

  estaAsignado(idServicio: number | undefined): boolean {
    if (!idServicio) return false;
    return this.serviciosAsignados.some(asig => asig.id_servicio == idServicio);
  }

  toggleServicio(servicio: Servicio): void {
    const index = this.serviciosAsignados.findIndex(asig => asig.id_servicio == servicio.id);
    
    if (index > -1) {
      this.serviciosAsignados.splice(index, 1);
    } else {
      this.serviciosAsignados.push({
        id: undefined,
        id_cementerio: Number(this.idCementerio),
        id_servicio: servicio.id!,
        coste: 0,
        nombreServicio: servicio.nombre
      });
    }
    this.cdr.markForCheck();
  }

  guardar(): void {
    if (!this.idCementerio || !this.formularioValido) return;
    
    this.cargando = true;
    const idNum = Number(this.idCementerio);

    const datosParaGuardar = this.serviciosAsignados.map(s => ({
      ...s,
      id: undefined 
    }));

    this.servicioService.guardarServicios(idNum, datosParaGuardar).subscribe({
      next: () => {
        this.cargando = false;
        this.modalService.mostrar('ÉXITO', 'Los servicios se han guardado correctamente.');
        this.onCerrar.emit();
      },
      error: (err) => {
        this.cargando = false;
        console.error('Error al guardar:', err);
        this.modalService.mostrar('ERROR', 'Hubo un error al guardar. Revisa los precios.');
      }
    });
  }

  cerrar(): void {
    this.onCerrar.emit();
  }
}