import { Component, EventEmitter, Output, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { UbicacionService } from '../../services/ubicacionService';
import { CementerioService } from '../../services/cementerioService';
import { ModalService } from '../../services/modalService';
import { Cementerio } from '../../models/cementerio';

@Component({
  selector: 'app-form-cementerio',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './form-cementerio.html',
  styleUrl: './form-cementerio.scss'
})
export class FormCementerio implements OnInit, OnChanges {
  @Input() idEmpresaInput: string | null = null;
  @Input() cementerioAEditar: Cementerio | null = null;
  @Output() cementerioCreado = new EventEmitter<void>();
  @Output() alCancelar = new EventEmitter<void>();

  cementerioForm!: FormGroup;
  provincias: any[] = [];
  localidades: any[] = [];
  selectedFile: File | null = null;
  editando: boolean = false;

  constructor(
    private cementerioService: CementerioService,
    private ubicacionService: UbicacionService,
    private modalService: ModalService
  ) {
    this.initForm();
  }

  ngOnInit(): void {
    this.cargarProvincias();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['cementerioAEditar'] && this.cementerioAEditar) {
      this.rellenarFormulario();
    } else if (changes['cementerioAEditar'] && !this.cementerioAEditar) {
      this.resetearFormulario();
    }
  }

  private initForm(): void {
    this.cementerioForm = new FormGroup({
      nombre: new FormControl('', [Validators.required]),
      direccion: new FormControl('', [Validators.required]),
      id_provincia: new FormControl(null, [Validators.required]),
      id_localidad: new FormControl({ value: null, disabled: true }, [Validators.required])
    });
  }

  private rellenarFormulario() {
    this.editando = true;
    const cem = this.cementerioAEditar;
    if (!cem) return;

    const idLoc = cem.id_localidad || cem.localidad?.id;
    const idProv = cem.localidad?.provincia?.id;

    if (idProv && idLoc) {
      this.cargarLocalidadesYParchear(idProv, idLoc);
    } else if (idLoc) {
      this.ubicacionService.getLocalidadPorId(idLoc).subscribe({
        next: (loc) => {
          // Usamos 'idProvincia' que es como viene de tu Java DTO
          if (loc && loc.idProvincia) {
            this.cargarLocalidadesYParchear(loc.idProvincia, idLoc);
          } else {
            this.rellenoBasico();
          }
        },
        error: () => this.rellenoBasico()
      });
    } else {
      this.rellenoBasico();
    }
  }

  private cargarLocalidadesYParchear(idProv: number, idLoc: number) {
    this.ubicacionService.getLocalidadesPorProvincia(idProv).subscribe({
      next: (res) => {
        this.localidades = res;
        const locControl = this.cementerioForm.get('id_localidad');
        
        if (this.localidades.length > 0) {
          locControl?.enable();
        }

        setTimeout(() => {
          this.cementerioForm.patchValue({
            nombre: this.cementerioAEditar?.nombre,
            direccion: this.cementerioAEditar?.direccion,
            id_provincia: idProv,
            id_localidad: idLoc
          });
        });
      }
    });
  }

  private rellenoBasico() {
    this.cementerioForm.patchValue({
      nombre: this.cementerioAEditar?.nombre,
      direccion: this.cementerioAEditar?.direccion
    });
  }

  cargarProvincias() {
    this.ubicacionService.getProvincias().subscribe(res => this.provincias = res);
  }

  onProvinciaChange() {
    const idProv = this.cementerioForm.get('id_provincia')?.value;
    const locControl = this.cementerioForm.get('id_localidad');

    this.localidades = [];
    locControl?.setValue(null);
    locControl?.disable();

    if (idProv) {
      this.ubicacionService.getLocalidadesPorProvincia(idProv).subscribe({
        next: (res) => {
          this.localidades = res;
          if (this.localidades.length > 0) {
            locControl?.enable();
          }
        }
      });
    }
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) this.selectedFile = file;
  }

  isInvalid(field: string): boolean {
    const control = this.cementerioForm.get(field);
    return !!(control && control.invalid && (control.touched || control.dirty));
  }

  guardar() {
    if (this.cementerioForm.invalid) {
      this.cementerioForm.markAllAsTouched();
      return;
    }

    const idEmpresaFinal = this.editando ? this.cementerioAEditar?.id_empresa : this.idEmpresaInput;

    if (!idEmpresaFinal) {
      this.modalService.mostrar('ERROR', 'No se ha detectado la empresa vinculada.');
      return;
    }

    const formData = new FormData();

    const values = this.cementerioForm.getRawValue();
    
    formData.append('nombre', values.nombre);
    formData.append('direccion', values.direccion);
    formData.append('id_localidad', values.id_localidad);
    formData.append('id_provincia', values.id_provincia);
    formData.append('id_empresa', String(idEmpresaFinal));

    if (this.selectedFile) {
      formData.append('imagen_archivo', this.selectedFile);
    }

    const peticion = (this.editando && this.cementerioAEditar)
      ? this.cementerioService.actualizar(this.cementerioAEditar.id, formData)
      : this.cementerioService.crear(formData);

    peticion.subscribe({
      next: () => {
        this.cementerioCreado.emit();
        this.resetearFormulario();
      },
      error: (err) => {
        console.error("Error al guardar:", err);
        this.modalService.mostrar('ERROR', 'Error al procesar la solicitud.');
      }
    });
  }

  private resetearFormulario() {
    this.editando = false;
    this.cementerioForm?.reset({
      id_provincia: null,
      id_localidad: null
    });
    this.cementerioForm.get('id_localidad')?.disable();
    this.localidades = [];
    this.selectedFile = null;
  }

  cancelar() {
    this.resetearFormulario();
    this.alCancelar.emit();
  }
}