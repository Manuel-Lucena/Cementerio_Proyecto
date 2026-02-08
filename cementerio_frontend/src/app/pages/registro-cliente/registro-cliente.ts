import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Validador } from '../../validator/validador';
import { RegistroService } from '../../services/registroService';
import { RegistroUsuario } from '../../models/registroUsuario';

@Component({
  selector: 'app-registro-cliente',
  standalone: true,
  imports: [ReactiveFormsModule, RouterModule, CommonModule],
  templateUrl: './registro-cliente.html',
  styleUrl: './registro-cliente.scss',
})
export class RegistroCliente implements OnInit {
  formularioCliente!: FormGroup;
  hidePassword = true;

  constructor(
    private registroService: RegistroService, 
    private router: Router
  ) { }

  ngOnInit(): void {
    this.formularioCliente = new FormGroup({
      nombreUsuario: new FormControl('', [Validators.required, Validators.minLength(4)]),
      password: new FormControl('', [Validators.required, Validators.minLength(6)]),
      password_confirm: new FormControl('', [Validators.required]),
      nombre: new FormControl('', [Validators.required]),
      apellidos: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      telefono: new FormControl('', [Validators.required, Validador.telefonoFormato]),
      direccion: new FormControl('', [Validators.required]),
      fecha_nacimiento: new FormControl('', [Validators.required, Validador.mayorEdad])
    }, { 
      validators: Validador.camposIguales('password', 'password_confirm') 
    });
  }

  guardar(): void {
    if (this.formularioCliente.invalid) {
      this.formularioCliente.markAllAsTouched(); 
      return;
    }

    const datosRegistro: RegistroUsuario = this.formularioCliente.value;

    this.registroService.registrar(datosRegistro).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Error en registro:', err);
      }
    });
  }
}