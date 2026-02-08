export interface Empresa {
  id: number;
  nombre: string;
  direccion: string;
  telefono: string;
  email: string;
  imagen?: string;
  añosReutilizarNichos?: number;
  cementerios?: any[]; // Podríamos detallarlo más adelante
}