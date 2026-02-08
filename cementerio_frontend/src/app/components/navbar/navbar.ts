import { Component } from '@angular/core';
import { RouterLink, Router } from "@angular/router";
import { CommonModule } from '@angular/common'; 
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, CommonModule], 
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {
  contraste: number = 100;

  constructor(public auth: Auth, private router: Router) {
    const guardado = localStorage.getItem('contraste');
    if (guardado) {
      this.contraste = parseInt(guardado);
      this.aplicarFiltro();
    }
  }

  cambiarContraste(event: any) {
    this.contraste = event.target.value;
    this.aplicarFiltro();
    localStorage.setItem('contraste', this.contraste.toString());
  }

  resetContraste() {
    this.contraste = 100;
    this.aplicarFiltro();
    localStorage.removeItem('contraste');
  }

private aplicarFiltro() {
  if (this.contraste === 100) {
    document.documentElement.style.filter = '';
  } else {
    document.documentElement.style.filter = `contrast(${this.contraste}%)`;
  }
}

  logout() {
    this.auth.logout();
    this.router.navigate(['/landing']); 
  }
}