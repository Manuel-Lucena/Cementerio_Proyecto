import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';


import { Navbar } from "../../components/navbar/navbar";
import { Footer } from "../../components/footer/footer";
import { FormAgrupacion } from "../../components/form-agrupacion/form-agrupacion";
import { ModalSepultura } from "../../components/modal-sepultura/modal-sepultura";
import { FormConcesion } from "../../components/form-concesion/form-concesion";
import { Confirmacion } from "../../components/confirmacion/confirmacion";
import { MensajesModalComponent } from '../../components/mensaje/mensaje';
import { ModalGestionServicios } from '../../components/modal-gestion-servicios/modal-gestion-servicios';

import { AgrupacionService } from '../../services/agrupacionService';
import { CementerioService } from '../../services/cementerioService';
import { ModalService } from '../../services/modalService';
import { Auth } from '../../services/auth';

import { Cementerio } from '../../models/cementerio';
import { Agrupacion } from '../../models/agrupacion';
import { Sepultura } from '../../models/sepultura';

@Component({
  selector: 'app-mapa-cementerio',
  standalone: true,
  imports: [
    CommonModule, Navbar, Footer, FormAgrupacion, ModalSepultura,
    FormConcesion, Confirmacion, MensajesModalComponent, ModalGestionServicios
  ],
  templateUrl: './mapa-cementerio.html',
  styleUrl: './mapa-cementerio.scss'
})
export class MapaCementerio implements OnInit {
  @ViewChild('modalSep') modalSepRef!: any;

  cementerio: Cementerio | null = null;
  agrupaciones: Agrupacion[] = [];
  idCementerio: string | null = null;
  urlImagenPlano: string = '';

  mostrarModalAgrupacion: boolean = false;
  mostrarModalEliminar: boolean = false;
  mostrarModalServicios: boolean = false;

  agrupacionSeleccionada: Agrupacion | null = null;
  agruAEditar: Agrupacion | null = null;
  agruAEliminar: Agrupacion | null = null;
  sepulturaSeleccionada: Sepultura | null = null;

  anchoOriginal: number = 1200;
  altoOriginal: number = 800;

  constructor(
    private route: ActivatedRoute,
    private cementerioService: CementerioService,
    private agruService: AgrupacionService,
    private modalService: ModalService,
    private cdr: ChangeDetectorRef,
    public auth: Auth
  ) { }

  ngOnInit(): void {
    this.idCementerio = this.route.snapshot.paramMap.get('id');
    if (this.idCementerio) {
      this.urlImagenPlano = `/planos/plano_${this.idCementerio}.jpg`;
      this.cargarDatos();
    }
  }

  // --- LÓGICA DE COORDENADAS PARA ROTACIÓN Y CENTRADO ---

  private getParsedCoords(coords: string): number[] {
    if (!coords) return [];
  
    return coords.trim().split(/[\s,]+/).map(c => parseFloat(c)).filter(n => !isNaN(n));
  }

  obtenerXTexto(agru: Agrupacion): number {
    const p = this.getParsedCoords(agru.coordenadas_mapa);
    if (p.length === 0) return 0;

    if (agru.tipo_forma === 'polygon') {
      const xs = p.filter((_, i) => i % 2 === 0);
      return xs.reduce((a, b) => a + b, 0) / xs.length;
    }
    if (agru.tipo_forma === 'rect') return p[0] + (p[2] / 2);
    return p[0];
  }

  obtenerYTexto(agru: Agrupacion): number {
    const p = this.getParsedCoords(agru.coordenadas_mapa);
    if (p.length === 0) return 0;

    if (agru.tipo_forma === 'polygon') {
  
      const ys = p.filter((_, i) => i % 2 !== 0);
      return ys.reduce((a, b) => a + b, 0) / ys.length;
    }
    if (agru.tipo_forma === 'rect') return p[1] + (p[3] / 2);
    return p[1]; 
  }

  splitCoords(coords: string, index: number): number {
    const p = this.getParsedCoords(coords);
    return p[index] || 0;
  }



  cargarDatos(): void {
    if (!this.idCementerio) return;
    this.cementerioService.obtenerPorId(this.idCementerio).subscribe({
      next: (data) => {
        this.cementerio = data;
        this.cargarSectores();
      },
      error: () => this.modalService.mostrar('ERROR', 'No se cargó el cementerio.')
    });
  }

  cargarSectores(): void {
    if (!this.idCementerio) return;
    this.agruService.getAgrupacionesPorCementerio(this.idCementerio).subscribe({
      next: (data) => {
        this.agrupaciones = data || [];
        this.cdr.detectChanges();
      }
    });
  }

  verDetalleAgrupacion(agru: Agrupacion): void {
    this.agrupacionSeleccionada = null;
    this.cdr.detectChanges();
    setTimeout(() => {
      this.agrupacionSeleccionada = agru;
      this.cdr.detectChanges();
    }, 50);
  }

  manejarCargaImagen(event: any): void {
    this.anchoOriginal = event.target.naturalWidth;
    this.altoOriginal = event.target.naturalHeight;
    this.cdr.detectChanges();
  }

  manejarErrorImagen(event: any): void { event.target.src = '/planos/plano-no-disponible.jpg'; }

  // --- MODALES Y ACCIONES ---

  abrirModalServicios(): void { this.mostrarModalServicios = true; }
  abrirModal(): void { this.agruAEditar = null; this.mostrarModalAgrupacion = true; }
  prepararEdicion(agru: Agrupacion): void { this.agruAEditar = { ...agru }; this.mostrarModalAgrupacion = true; }
  cerrarModal(): void { this.mostrarModalAgrupacion = false; this.agruAEditar = null; }

  onAgrupacionCreada(): void {
    this.cargarSectores();
    this.modalService.mostrar(this.agruAEditar ? 'ACTUALIZADO' : 'CREADO', 'Operación exitosa.');
    this.cerrarModal();
  }

  abrirModalEliminar(agru: Agrupacion): void { this.agruAEliminar = agru; this.mostrarModalEliminar = true; }

  confirmarEliminacion(): void {
    if (this.agruAEliminar?.id) {
      this.agruService.eliminar(this.agruAEliminar.id).subscribe({
        next: () => {
          this.mostrarModalEliminar = false;
          this.cargarSectores();
          this.modalService.mostrar('ELIMINADO', 'Sector borrado.');
        }
      });
    }
  }

  abrirFormularioConcesion(sep: Sepultura): void { this.sepulturaSeleccionada = sep; }

  finalizarTramite(): void {
    this.sepulturaSeleccionada = null;
    if (this.modalSepRef) this.modalSepRef.cargarSepulturas();
  }
}