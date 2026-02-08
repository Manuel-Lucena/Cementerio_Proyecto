import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Agrupacion } from '../models/agrupacion';
import { Auth } from './auth';

@Injectable({
  providedIn: 'root'
})
export class AgrupacionService {
  private apiUrl = 'http://localhost:8090/api/agrupaciones';

  constructor(private http: HttpClient, private auth: Auth) { }


  getAgrupacionesPorCementerio(idCementerio: string): Observable<Agrupacion[]> {
    return this.http.get<Agrupacion[]>(`${this.apiUrl}/cementerio/${idCementerio}`, {
      headers: this.auth.getHeaders()
    });
  }

  crearAgrupacion(datos: any): Observable<Agrupacion> {
    return this.http.post<Agrupacion>(this.apiUrl, datos, {
      headers: this.auth.getHeaders()
    });
  }

  actualizar(id: number, data: any): Observable<Agrupacion> {
    return this.http.put<Agrupacion>(`${this.apiUrl}/${id}`, data, {
      headers: this.auth.getHeaders()
    });
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.auth.getHeaders()
    });
  }
}