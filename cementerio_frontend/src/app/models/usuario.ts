export interface Usuario {
  id: number;
  nombreUsuario: string;
  rol: 'admin' | 'empresa' | 'cliente';
  token?: string;
}

