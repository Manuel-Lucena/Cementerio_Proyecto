export interface Cliente {
  id: number;
  nombre: string;
  apellidos: string;
  email: string;
  telefono: string;
  direccion: string;
  fechaNacimiento: string; 
  nombreUsuario?: string;
  idUsuario?: number;
  fotoRuta?: string; 
}