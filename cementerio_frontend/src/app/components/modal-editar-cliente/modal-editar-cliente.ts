import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Validador } from '../../validator/validador';

@Component({
  selector: 'app-modal-editar-cliente',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './modal-editar-cliente.html',
  styleUrl: './modal-editar-cliente.scss',
})
export class ModalEditarCliente implements OnInit {
  @Input() cliente: any = null;
  @Output() guardado = new EventEmitter<any>();
  @Output() cancelado = new EventEmitter<void>();

  formularioCliente!: FormGroup;
  titulo: string = '';
  hidePassword = true;

  ngOnInit(): void {
    this.titulo = this.cliente ? 'Editar Cliente' : 'Registro de Cliente';

    let fechaLimpia = '';
    if (this.cliente && this.cliente.fechaNacimiento) {
      fechaLimpia = this.cliente.fechaNacimiento.split('T')[0];
    }

    this.formularioCliente = new FormGroup({
      id: new FormControl(this.cliente?.id || null),
      nombreUsuario: new FormControl(this.cliente?.nombreUsuario || '', [
        Validators.required,
        Validators.minLength(4)
      ]),
   
      password: new FormControl('', this.cliente ? [] : [Validators.required, Validators.minLength(6)]),
      password_confirm: new FormControl(''),
      nombre: new FormControl(this.cliente?.nombre || '', [Validators.required]),
      apellidos: new FormControl(this.cliente?.apellidos || '', [Validators.required]),
      email: new FormControl(this.cliente?.email || '', [Validators.required, Validators.email]),
      telefono: new FormControl(this.cliente?.telefono || '', [
        Validators.required,
        Validador.telefonoFormato 
      ]),
      direccion: new FormControl(this.cliente?.direccion || '', [Validators.required]),
      fecha_nacimiento: new FormControl(fechaLimpia, [
        Validators.required,
        Validador.mayorEdad 
      ])
    }, {

      validators: Validador.camposIguales('password', 'password_confirm')
    });
  }

  isInvalid(field: string): boolean {
    const control = this.formularioCliente.get(field);
    return !!(control && control.invalid && (control.touched || control.dirty));
  }

  getErrorMessage(field: string): string {
    const control = this.formularioCliente.get(field);
    if (control?.hasError('required')) return 'Este campo es obligatorio.';
    if (control?.hasError('email')) return 'Formato de email inválido.';
    if (control?.hasError('minlength')) return `Mínimo ${control.errors?.['minlength'].requiredLength} caracteres.`;


    if (control?.hasError('erroresArray')) return control.getError('erroresArray')[0];
    if (this.formularioCliente.hasError('coincidencia') && field === 'password_confirm') {
      return 'Las contraseñas no coinciden.';
    }

    return 'Dato no válido.';
  }

  enviar(): void {
    if (this.formularioCliente.valid) {
      const formValue = this.formularioCliente.value;
      let dataEnvio: any;

      if (this.cliente) {
        dataEnvio = {
          id: formValue.id,
          nombre: formValue.nombre,
          apellidos: formValue.apellidos,
          email: formValue.email,
          telefono: formValue.telefono,
          direccion: formValue.direccion,
          nombreUsuario: formValue.nombreUsuario,
          fechaNacimiento: formValue.fecha_nacimiento
        };
        if (formValue.password) {
          dataEnvio.password = formValue.password;
          dataEnvio.passwordConfirm = formValue.password_confirm;
        }
      } else {
        dataEnvio = { ...formValue };
      }
      this.guardado.emit(dataEnvio);
    } else {
      this.formularioCliente.markAllAsTouched();
    }
  }

  cerrar(): void { this.cancelado.emit(); }
}