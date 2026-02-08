export interface Agrupacion {
  id?: number;
  nombre: string;
  tipo_forma: 'rect' | 'circle' | 'polygon';
  filas: number;
  columnas: number;
  coordenadas_mapa: string;
  descripcion?: string;
  id_cementerio: number;
}