import { Routes } from '@angular/router';
import { Landing } from './pages/landing/landing';
import { Login } from './pages/login/login';
import { RegistroCliente } from './pages/registro-cliente/registro-cliente';
import { Empresas } from './pages/empresas/empresas';
import { ListaCementerios } from './pages/lista-cementerios/lista-cementerios';
import { adminGuard } from './guards/admin-guard';
import { userGuard } from './guards/user-guard';
import { empresaGuard } from './guards/empresa-guard';
import { clienteGuard } from './guards/cliente-guards';
import { MapaCementerio } from './pages/mapa-cementerio/mapa-cementerio';
import { ListaDifuntos } from './pages/lista-difuntos/lista-difuntos';
import { ListaFacturas } from './pages/lista-facturas/lista-facturas';
import { PanelAdmin } from './pages/panel-admin/panel-admin';
import { PagoResultado } from './pages/pago-resultado/pago-resultado';

export const routes: Routes = [
  { path: '', redirectTo: '/landing', pathMatch: 'full' },
  { path: 'landing', component: Landing },
  { path: 'login', component: Login },
  { path: 'registro', component: RegistroCliente },
  { path: 'listaEmpresas', component: Empresas, canActivate: [userGuard] },
  { path: 'cementerios', component: ListaCementerios, canActivate: [userGuard] },
  { path: 'cementerios/:id', component: ListaCementerios, canActivate: [userGuard] },
  { path: 'mapa-cementerio/:id', component: MapaCementerio, canActivate: [userGuard] },
  { path: 'mis-difuntos', component: ListaDifuntos, canActivate: [clienteGuard] },
  { path: 'mis-facturas', component: ListaFacturas, canActivate: [clienteGuard] },
  { path: 'panel-admin', component: PanelAdmin, canActivate: [adminGuard] },
  { path: 'pago-resultado', component: PagoResultado, canActivate: [userGuard] }]