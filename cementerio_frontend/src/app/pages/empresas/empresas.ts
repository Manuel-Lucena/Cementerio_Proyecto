import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Navbar } from "../../components/navbar/navbar";
import { Footer } from "../../components/footer/footer";
import { FormEmpresa } from '../../components/form-empresa/form-empresa';
import { Confirmacion } from '../../components/confirmacion/confirmacion';
import { MensajesModalComponent } from '../../components/mensaje/mensaje';

import { EmpresaService } from '../../services/empresaService';
import { ModalService } from '../../services/modalService';
import { Empresa } from '../../models/empresa';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-empresas',
  standalone: true,
  imports: [Navbar, Footer, CommonModule, FormEmpresa, Confirmacion, MensajesModalComponent, FormsModule],
  templateUrl: './empresas.html',
  styleUrl: './empresas.scss',
})
export class Empresas implements OnInit {
  listaEmpresas: Empresa[] = [];
  listaFiltrada: Empresa[] = [];
  filtroNombre = '';

  mostrarModal = false;
  mostrarModalEliminar = false;
  idEmpresaAEliminar: number | null = null;
  nombreEmpresaAEliminar = '';
  empresaSeleccionada: Empresa | null = null;

  constructor(
    private empresaService: EmpresaService,
    private modalService: ModalService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    public auth: Auth
  ) { }

  ngOnInit(): void {
    this.cargarEmpresas();
  }

  cargarEmpresas(): void {
    this.empresaService.listar().subscribe({
      next: (res) => {
        this.listaEmpresas = res;
        this.listaFiltrada = res;
        this.cdr.detectChanges();
      },
      error: () => this.modalService.mostrar('ERROR', 'No se pudieron cargar las empresas.')
    });
  }

  aplicarFiltros(): void {
    const busqueda = this.filtroNombre.toLowerCase().trim();
    this.listaFiltrada = this.listaEmpresas.filter(emp => 
      emp.nombre.toLowerCase().includes(busqueda)
    );
  }

  // --- MÉTODOS DE GESTIÓN ---
  abrirModal(): void {
    if (!this.auth.esAdmin()) return;
    this.empresaSeleccionada = null;
    this.mostrarModal = true;
  }

  prepararEdicion(empresa: Empresa): void {
    if (!this.auth.esAdmin()) return;
    this.empresaSeleccionada = { ...empresa };
    this.mostrarModal = true;
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.empresaSeleccionada = null;
  }

  finalizarGuardado(): void {
    this.cerrarModal();
    this.cargarEmpresas();
    this.modalService.mostrar('ÉXITO', 'Datos guardados correctamente.');
  }

  abrirModalEliminar(event: Event, empresa: Empresa): void {
    event.stopPropagation();
    if (!this.auth.esAdmin()) return;
    this.idEmpresaAEliminar = empresa.id;
    this.nombreEmpresaAEliminar = empresa.nombre;
    this.mostrarModalEliminar = true;
  }

  confirmarEliminacion(): void {
    if (this.idEmpresaAEliminar) {
      this.empresaService.eliminar(this.idEmpresaAEliminar).subscribe({
        next: () => {
          this.mostrarModalEliminar = false;
          this.cargarEmpresas();
          this.modalService.mostrar('ELIMINADO', 'Empresa borrada.');
        }
      });
    }
  }

  verCementerios(id: number): void {
    this.router.navigate(['/cementerios', id]);
  }
}