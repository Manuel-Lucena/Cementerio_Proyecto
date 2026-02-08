import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Navbar } from '../../components/navbar/navbar';
import { Footer } from '../../components/footer/footer';
import { FormCementerio } from '../../components/form-cementerio/form-cementerio';
import { Confirmacion } from '../../components/confirmacion/confirmacion';
import { MensajesModalComponent } from '../../components/mensaje/mensaje';

import { CementerioService } from '../../services/cementerioService';
import { UbicacionService } from '../../services/ubicacionService';
import { ModalService } from '../../services/modalService';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-lista-cementerios',
  standalone: true,
  imports: [CommonModule, Navbar, Footer, FormCementerio, Confirmacion, FormsModule, MensajesModalComponent],
  templateUrl: './lista-cementerios.html',
  styleUrl: './lista-cementerios.scss',
})
export class ListaCementerios implements OnInit {
  listaCementerios: any[] = [];
  idEmpresa: string | null = null;

  provincias: any[] = [];
  localidades: any[] = [];
  filtroProvincia: any = null;
  filtroLocalidad: any = null;
  filtroBusqueda: string = '';

  mostrarModal = false;
  mostrarModalEliminar = false;
  cementerioSeleccionado: any = null;
  cementerioAEliminar: any = null;

  constructor(
    private cementerioService: CementerioService,
    private ubicacionService: UbicacionService,
    private modalService: ModalService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
    public auth: Auth
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.idEmpresa = params.get('id');
      this.cargarCementerios();
      this.cargarProvincias();
    });
  }

  cargarProvincias() {
    this.ubicacionService.getProvincias().subscribe(res => this.provincias = res);
  }

  onProvinciaChange() {
    this.filtroLocalidad = null;
    this.localidades = [];
    if (this.filtroProvincia && this.filtroProvincia !== 'null') {
      this.ubicacionService.getLocalidadesPorProvincia(this.filtroProvincia).subscribe(res => this.localidades = res);
    }
  }

  cargarCementerios(): void {
    const peticion = this.auth.esEmpresa()
      ? this.cementerioService.listarMisCementerios()
      : (this.idEmpresa
        ? this.cementerioService.listarPorEmpresa(this.idEmpresa)
        : this.cementerioService.listarTodos());


    peticion.subscribe({
      next: (res) => {
        this.listaCementerios = res;
        this.cdr.detectChanges();
      },
      error: () => this.modalService.mostrar('ERROR', 'No se pudieron cargar los cementerios.')
    });
  }
  get cementeriosMostrados() {
    return this.listaCementerios.filter(c => {
      const cumpleBusqueda = !this.filtroBusqueda ||
        c.nombre.toLowerCase().includes(this.filtroBusqueda.toLowerCase());

      const idProv = c.id_provincia || c.localidad?.provincia?.id;
      const cumpleProvincia = !this.filtroProvincia || this.filtroProvincia === 'null' || idProv == this.filtroProvincia;

      const idLoc = c.id_localidad || c.localidad?.id;
      const cumpleLocalidad = !this.filtroLocalidad || this.filtroLocalidad === 'null' || idLoc == this.filtroLocalidad;

      return cumpleBusqueda && cumpleProvincia && cumpleLocalidad;
    });
  }

  abrirModalNuevo() { this.cementerioSeleccionado = null; this.mostrarModal = true; }

  prepararEdicion(event: Event, cem: any) {
    event.stopPropagation();
    this.cementerioSeleccionado = { ...cem };
    this.mostrarModal = true;
  }

  cerrarModal() { this.mostrarModal = false; this.cementerioSeleccionado = null; }

  onCementerioGuardado() {
    this.cerrarModal();
    this.cargarCementerios();
    this.modalService.mostrar('ÉXITO', 'Datos guardados correctamente.');
  }

  abrirModalEliminar(event: Event, cem: any) {
    event.stopPropagation();
    this.cementerioAEliminar = cem;
    this.mostrarModalEliminar = true;
  }

  confirmarEliminacion() {
    if (this.cementerioAEliminar) {
      this.cementerioService.eliminar(this.cementerioAEliminar.id).subscribe({
        next: () => {
          this.mostrarModalEliminar = false;
          this.cargarCementerios();
          this.modalService.mostrar('ELIMINADO', 'El registro ha sido borrado.');
        },
        error: (err) => {
          console.error('Error al eliminar:', err);
          this.modalService.mostrar('ERROR', 'No se puede eliminar el cementerio (posiblemente tiene datos vinculados).');
        }
      });
    }
  }

  verUnidades(id: number) { this.router.navigate(['/mapa-cementerio', id]); }
}