import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { EmpresaService } from '../../services/empresaService';
import { ModalService } from '../../services/modalService';
import { Validador } from '../../validator/validador';

@Component({
  selector: 'app-form-empresa',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './form-empresa.html',
  styleUrl: './form-empresa.scss'
})
export class FormEmpresa implements OnChanges {
  @Input() empresaAEditar: any = null;
  @Output() empresaCreada = new EventEmitter<void>();
  @Output() alCancelar = new EventEmitter<void>();

  empresaForm: FormGroup;
  fotoSeleccionada: File | null = null;

  hidePassword = true;
  constructor(
    private empresaService: EmpresaService,
    private modalService: ModalService
  ) {
    this.empresaForm = this.initForm();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['empresaAEditar']) {
      if (this.empresaAEditar) {
        this.rellenarFormulario();
      } else {
        this.resetearParaNuevo();
      }
    }
  }

  private initForm(): FormGroup {
    return new FormGroup({
      nombre_usuario: new FormControl('', [Validators.required, Validators.minLength(4)]),
      password: new FormControl('', [Validators.required, Validators.minLength(6)]),
      password_confirm: new FormControl('', [Validators.required]),
      nombre: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      telefono: new FormControl('', [Validators.required, Validador.telefonoFormato]),
      direccion: new FormControl('', [Validators.required]),
      años_reutilizar_nichos: new FormControl(5, [Validators.required, Validators.min(5)]) 
    }, { validators: Validador.camposIguales('password', 'password_confirm') });
  }

  private rellenarFormulario() {
    const e = this.empresaAEditar;

    this.empresaForm.get('nombre_usuario')?.disable();

    this.empresaForm.patchValue({
      nombre_usuario: e.usuario?.nombreUsuario || e.nombre_usuario || '',
      nombre: e.nombre,
      email: e.email,
      telefono: e.telefono,
      direccion: e.direccion,
      años_reutilizar_nichos: e.añosReutilizarNichos || e.años_reutilizar_nichos || 5
    });

 
    this.empresaForm.get('password')?.clearValidators();
    this.empresaForm.get('password')?.setValidators([Validators.minLength(6)]);
    this.empresaForm.get('password_confirm')?.clearValidators();

    this.empresaForm.get('password')?.updateValueAndValidity();
    this.empresaForm.get('password_confirm')?.updateValueAndValidity();
  }

  private resetearParaNuevo() {
    this.empresaForm.reset({ años_reutilizar_nichos: 5 });
    this.empresaForm.get('nombre_usuario')?.enable();
    this.empresaForm.get('password')?.setValidators([Validators.required, Validators.minLength(6)]);
    this.empresaForm.get('password_confirm')?.setValidators([Validators.required]);
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) this.fotoSeleccionada = file;
  }

  guardar() {
    if (this.empresaForm.invalid) {
      this.empresaForm.markAllAsTouched();
      return;
    }

    const formValues = this.empresaForm.getRawValue();
    const formData = new FormData();


    formData.append('nombre_usuario', formValues.nombre_usuario);
    formData.append('nombre', formValues.nombre);
    formData.append('email', formValues.email);
    formData.append('telefono', formValues.telefono);
    formData.append('direccion', formValues.direccion);
    formData.append('años_reutilizar_nichos', formValues.años_reutilizar_nichos.toString());


    if (formValues.password) {
      formData.append('password', formValues.password);
    }

 
    if (this.fotoSeleccionada) {
      formData.append('imagenArchivo', this.fotoSeleccionada);
    }

  
    if (this.empresaAEditar?.id) {
      formData.append('id', this.empresaAEditar.id.toString());
    }

    console.log("Datos enviados al servidor:", formValues);

    const peticion = this.empresaAEditar
      ? this.empresaService.actualizar(this.empresaAEditar.id, formData)
      : this.empresaService.crear(formData);

    peticion.subscribe({
      next: () => {
        this.modalService.mostrar('ÉXITO', 'Datos guardados correctamente');
        this.empresaCreada.emit();
      },
      error: (err) => {
        console.error("Error 400 detallado:", err);
        const msg = err.error?.message || 'Error de validación. Revisa los campos.';
        this.modalService.mostrar('ERROR AL GUARDAR', msg);
      }
    });
  }
}