import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from '../services/auth';

export const empresaGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);

  const logueado = authService.estaLogueado();
  const rol = authService.getRol();

  if (logueado && (authService.esEmpresa() || rol === 'ADMIN')) {
    return true;
  }

  router.navigate(['/landing']);
  return false;
};