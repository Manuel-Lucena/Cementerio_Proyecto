export interface Difunto {
  id: number;
  nombre: string;
  apellidos: string;
  fechaFallecimiento: string;
  ubicacion: string;
  idCementerio: number;
  // Campos nuevos:
  nombreCementerio: string;
  localidad: string;
  provincia: string;
}