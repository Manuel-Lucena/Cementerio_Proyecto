import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FacturaService } from '../../services/facturaService';
import { PaymentService } from '../../services/PaymentService';
import { ModalService } from '../../services/modalService';
import { Navbar } from "../../components/navbar/navbar";
import { Footer } from "../../components/footer/footer";
import { MensajesModalComponent } from "../../components/mensaje/mensaje";
import { Confirmacion } from "../../components/confirmacion/confirmacion";

@Component({
  selector: 'app-lista-facturas',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    Navbar, 
    Footer, 
    MensajesModalComponent, 
    Confirmacion
  ],
  templateUrl: './lista-facturas.html',
  styleUrl: './lista-facturas.scss',
})
export class ListaFacturas implements OnInit {
  facturas: any[] = [];
  cargando: boolean = false;

  mostrarModalConfirmarAnulacion: boolean = false;
  idFacturaAAnular: number | null = null;

  paginaActual: number = 0;
  totalPaginas: number = 0;
  totalElementos: number = 0;
  tamanoPagina: number = 4;

  filtroEstado: string = 'TODAS';
  ordenarPor: string = 'fechaFactura';
  direccionOrden: 'asc' | 'desc' = 'desc';
  filtroBusqueda: string = '';

  constructor(
    private facturaService: FacturaService,
    private paymentService: PaymentService,
    private modalService: ModalService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.cargarFacturas();
  }

  cargarFacturas(): void {
    this.cargando = true;
    this.facturaService.listarMisFacturas(
      this.filtroEstado,
      this.ordenarPor,
      this.direccionOrden,
      this.paginaActual,
      this.tamanoPagina
    ).subscribe({
      next: (res: any) => {
        if (res && res.content) {
          this.facturas = res.content.map((f: any) => ({
            ...f,
            conceptoReal: f.concepto || 'Servicio General',
            estadoNormal: f.estado ? f.estado.toUpperCase().trim() : 'PENDIENTE'
          }));
          this.totalPaginas = res.totalPages;
          this.totalElementos = res.totalElements;
        } else {
          this.facturas = [];
        }
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        this.cargando = false;
        this.modalService.mostrar('Error', 'No se pudo obtener el historial de facturas.');
      }
    });
  }

  // --- GESTIÓN DE ANULACIÓN ---
  
  prepararAnulacion(id: number): void {
    this.idFacturaAAnular = id;
    this.mostrarModalConfirmarAnulacion = true;
  }

  confirmarAnulacion(): void {
    if (this.idFacturaAAnular) {
      this.facturaService.cancelarFactura(this.idFacturaAAnular).subscribe({
        next: () => {
          this.mostrarModalConfirmarAnulacion = false;
          this.idFacturaAAnular = null;
          this.modalService.mostrar('Éxito', 'La factura ha sido anulada correctamente.');
          this.cargarFacturas();
        },
        error: () => {
          this.mostrarModalConfirmarAnulacion = false;
          this.modalService.mostrar('Error', 'Hubo un problema al intentar anular la factura.');
        }
      });
    }
  }

  cancelarAnulacion(): void {
    this.mostrarModalConfirmarAnulacion = false;
    this.idFacturaAAnular = null;
  }

  // --- ACCIONES ADICIONALES ---

  pagar(id: number): void {
    this.paymentService.initPayment(id).subscribe({
      next: (res) => res?.redirectUrl ? window.location.href = res.redirectUrl :
        this.modalService.mostrar('Error', 'URL de pago no válida.')
    });
  }

  imprimirFactura(factura: any): void {
    this.modalService.mostrar('PDF', `Generando documento para la factura #${factura.id}...`);
  }

  // --- FILTROS Y PAGINACIÓN ---

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < this.totalPaginas) {
      this.paginaActual = nuevaPagina;
      this.cargarFacturas();
    }
  }

  cambiarEstado(nuevoEstado: string): void {
    this.filtroEstado = nuevoEstado;
    this.paginaActual = 0; 
    this.cargarFacturas();
  }

  toggleOrden(campo: string): void {
    this.direccionOrden = (this.ordenarPor === campo && this.direccionOrden === 'asc') ? 'desc' : 'asc';
    this.ordenarPor = campo;
    this.paginaActual = 0;
    this.cargarFacturas();
  }

  get facturasMostradas() {
    const query = this.filtroBusqueda.toLowerCase().trim();
    if (!query) return this.facturas;
    return this.facturas.filter(f =>
      f.conceptoReal.toLowerCase().includes(query) ||
      f.id.toString().includes(query)
    );
  }
}