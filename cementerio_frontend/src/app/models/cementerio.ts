export interface EmpresaRelacion {
  id: number;
  nombre: string;
}

export interface Cementerio {
  id: number;
  nombre: string;
  direccion: string;
  telefono?: string;
  email?: string;
  imagen_plano?: string;
  id_localidad?: number;
  localidad?: any; 
  

  empresa?: EmpresaRelacion; 
  id_empresa?: number;
}