export interface Sepultura {
  id: number;
  idAgrupacion: number;
  fila: number;
  columna: number;
  codigoVisual: string;
  estado: string;
  ocupado: boolean;
  isOcupado?: boolean;
}