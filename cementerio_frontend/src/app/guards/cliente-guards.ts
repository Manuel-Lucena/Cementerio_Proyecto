import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from '../services/auth';

export const clienteGuard: CanActivateFn = (route, state) => {
  const auth = inject(Auth);
  const router = inject(Router);

  const logueado = auth.estaLogueado();
  const rol = auth.getRol();


  // Permitimos el acceso si es CLIENTE O si es ADMIN
  if (logueado && (rol === 'CLIENTE' || rol === 'ADMIN')) {
    return true;
  }

  console.error('Acceso Denegado. Redirigiendo...');
  router.navigate(['/landing']);
  return false;
};