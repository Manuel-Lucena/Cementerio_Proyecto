import { Component } from '@angular/core';
import { Auth } from '../../services/auth';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TokenResponse } from '../../models/token'; 

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule, CommonModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class Login {
  nombreUsuario = '';
  password = '';
  error = '';
  hidePassword = true;

  constructor(private auth: Auth, private router: Router) {}

  onLogin(): void {
    this.error = ''; 
    if (!this.nombreUsuario || !this.password) {
      this.error = 'Por favor, rellena todos los campos';
      return;
    }

    this.auth.login(this.nombreUsuario, this.password).subscribe({
      next: (res: TokenResponse) => {
        this.auth.guardarToken(res.token);
        this.router.navigate(['/landing']);
      },
      error: (err) => {
        this.error = 'Usuario o contraseña incorrectos';
        console.error('Login error:', err);
      }
    });
  }
}