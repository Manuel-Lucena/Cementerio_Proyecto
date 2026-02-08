import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Footer } from "../../components/footer/footer";
import { Navbar } from "../../components/navbar/navbar";
import { Confirmacion } from "../../components/confirmacion/confirmacion";
import { ModalEditarCliente } from "../../components/modal-editar-cliente/modal-editar-cliente";
import { FormEmpresa } from "../../components/form-empresa/form-empresa";
import { MensajesModalComponent } from "../../components/mensaje/mensaje";

import { ClienteService } from '../../services/clienteService';
import { EmpresaService } from '../../services/empresaService';
import { ModalService } from '../../services/modalService';
import { EstadisticaService } from '../../services/estadisticaService';
import { ConcesionService } from '../../services/concesionService'; 

import { Cliente } from '../../models/cliente';
import { Empresa } from '../../models/empresa';
import { Concesion } from '../../models/concesion';

import { Chart, registerables, ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

Chart.register(...registerables);

@Component({
    selector: 'app-panel-admin',
    standalone: true,
    imports: [
        CommonModule, 
        FormsModule, 
        Footer, 
        Navbar, 
        Confirmacion,
        ModalEditarCliente, 
        FormEmpresa, 
        MensajesModalComponent, 
        BaseChartDirective
    ],
    templateUrl: './panel-admin.html',
    styleUrl: './panel-admin.scss',
})
export class PanelAdmin implements OnInit {
    vistaActual: 'clientes' | 'empresas' | 'stats' | 'concesiones' = 'stats';
    filtro: string = '';
    cargando: boolean = false;

    clientes: Cliente[] = [];
    empresas: Empresa[] = [];
    concesiones: Concesion[] = [];
    clienteSeleccionadoParaConcesiones: Cliente | null = null;
    datosResumen: any = null;

    public barChartOptions: ChartConfiguration['options'] = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: true, position: 'bottom' } }
    };
    public barChartType: ChartType = 'bar';
    public barChartData: ChartData<'bar'> = {
        labels: ['Vencidas', 'Activas'],
        datasets: [{
            data: [0, 0],
            label: 'Concesiones',
            backgroundColor: ['#dc3545', '#0d6efd']
        }]
    };

    mostrarModalBorrar = false;
    mostrarModalEditar = false;
    mostrarModalEmpresa = false;

    clienteSeleccionado: Cliente | null = null;
    empresaSeleccionada: Empresa | null = null;

    constructor(
        private clienteService: ClienteService,
        private empresaService: EmpresaService,
        private modalService: ModalService,
        private estadisticaService: EstadisticaService,
        private concesionService: ConcesionService,
        private cdr: ChangeDetectorRef
    ) { }

    ngOnInit(): void {
        this.cargarDatos();
    }

    cargarDatos() {
        this.cargando = true;
        if (this.vistaActual === 'stats') {
            this.cargarEstadisticas();
        } else if (this.vistaActual === 'clientes') {
            this.cargarClientes();
        } else if (this.vistaActual === 'empresas') {
            this.cargarEmpresas();
        }
    }

    cambiarVista(vista: 'clientes' | 'empresas' | 'stats' | 'concesiones') {
        if (this.vistaActual === vista) return;
        this.vistaActual = vista;
        this.filtro = '';
        this.concesiones = []; 
        this.cargarDatos();
    }

    verConcesiones(cliente: Cliente) {
        this.cargando = true;
        this.clienteSeleccionadoParaConcesiones = cliente;
        this.concesionService.listarPorCliente(cliente.id).subscribe({
            next: (data) => {
                this.concesiones = data;
                this.vistaActual = 'concesiones';
            },
            error: () => {
                this.modalService.mostrar('Error', 'No se pudieron cargar las concesiones.');
                this.cargando = false;
            },
            complete: () => {
                this.cargando = false;
                this.cdr.markForCheck();
            }
        });
    }

    cargarEstadisticas() {
        this.estadisticaService.getResumen().subscribe({
            next: (data) => {
                this.datosResumen = data;
                this.barChartData = {
                    ...this.barChartData,
                    datasets: [{
                        ...this.barChartData.datasets[0],
                        data: [data.concesionesVencidas, data.concesionesActivas]
                    }]
                };
            },
            error: () => { this.cargando = false; },
            complete: () => { 
                this.cargando = false; 
                this.cdr.markForCheck();
            }
        });
    }

    cargarClientes() {
        this.clienteService.listarTodos().subscribe({
            next: (data) => { this.clientes = data; },
            error: () => { this.cargando = false; },
            complete: () => { 
                this.cargando = false; 
                this.cdr.markForCheck();
            }
        });
    }

    cargarEmpresas() {
        this.empresaService.listar().subscribe({
            next: (data) => { this.empresas = data; },
            error: () => { this.cargando = false; },
            complete: () => { 
                this.cargando = false; 
                this.cdr.markForCheck();
            }
        });
    }

    get clientesFiltrados() {
        if (!this.filtro) return this.clientes;
        const bus = this.filtro.toLowerCase().trim();
        return this.clientes.filter(c => 
            c.nombre.toLowerCase().includes(bus) || 
            c.apellidos.toLowerCase().includes(bus)
        );
    }

    get empresasFiltradas() {
        if (!this.filtro) return this.empresas;
        const bus = this.filtro.toLowerCase().trim();
        return this.empresas.filter(e => e.nombre.toLowerCase().includes(bus));
    }

    nuevoCliente() { this.clienteSeleccionado = null; this.mostrarModalEditar = true; }
    editar(c: Cliente) { this.clienteSeleccionado = { ...c }; this.mostrarModalEditar = true; }

    confirmarGuardar(datos: Cliente) {
        const peticion = datos.id ? this.clienteService.actualizar(datos.id, datos) : this.clienteService.registrar(datos);
        peticion.subscribe({
            next: () => { 
                this.cargarClientes(); 
                this.mostrarModalEditar = false; 
                this.modalService.mostrar('Éxito', 'Registro guardado correctamente.'); 
            },
            error: () => this.modalService.mostrar('Error', 'Fallo al intentar guardar el registro.')
        });
    }

    nuevaEmpresa() { this.empresaSeleccionada = null; this.mostrarModalEmpresa = true; }
    editarEmpresa(e: Empresa) { this.empresaSeleccionada = { ...e }; this.mostrarModalEmpresa = true; }
    finalizarGuardadoEmpresa() { 
        this.mostrarModalEmpresa = false; 
        this.cargarEmpresas(); 
        this.modalService.mostrar('Éxito', 'Empresa guardada correctamente.');
    }

    abrirModalEliminar(item: any) {
        this.vistaActual === 'clientes' ? (this.clienteSeleccionado = item) : (this.empresaSeleccionada = item);
        this.mostrarModalBorrar = true;
    }

    confirmarEliminar() {
        this.cargando = true;
        if (this.vistaActual === 'clientes' && this.clienteSeleccionado) {
            this.clienteService.eliminar(this.clienteSeleccionado.id).subscribe({
                next: () => { 
                    this.clientes = this.clientes.filter(c => c.id !== this.clienteSeleccionado?.id); 
                    this.cerrarBorrado('Éxito', 'Cliente eliminado permanentemente.'); 
                },
                error: () => {
                    this.cargando = false;
                    this.mostrarModalBorrar = false;
                    this.modalService.mostrar('Error', 'No se puede eliminar el cliente (tiene contratos activos).');
                }
            });
        } else if (this.empresaSeleccionada) {
            this.empresaService.eliminar(this.empresaSeleccionada.id).subscribe({
                next: () => { 
                    this.empresas = this.empresas.filter(e => e.id !== this.empresaSeleccionada?.id); 
                    this.cerrarBorrado('Éxito', 'Empresa eliminada correctamente.'); 
                },
                error: () => {
                    this.cargando = false;
                    this.mostrarModalBorrar = false;
                    this.modalService.mostrar('Error', 'No se puede eliminar la empresa porque tiene cementerios asociados.');
                }
            });
        }
    }

    private cerrarBorrado(titulo: string, mensaje: string) {
        this.cargando = false;
        this.mostrarModalBorrar = false;
        this.modalService.mostrar(titulo, mensaje);
        this.cdr.markForCheck();
    }
}