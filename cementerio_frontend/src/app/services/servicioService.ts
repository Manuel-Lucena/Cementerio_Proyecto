import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auth } from './auth';

@Injectable({
  providedIn: 'root'
})
export class ServicioService {
  private apiUrl = 'http://localhost:8090/api/servicios';

  constructor(private http: HttpClient, private auth: Auth) {}

 
  getCatalogo(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/catalogo`);
  }

  
  getServiciosByCementerio(idCementerio: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/cementerio/${idCementerio}`, {
      headers: this.auth.getHeaders()
    });
  }

  // Guarda la lista completa
  guardarServicios(idCementerio: number, servicios: any[]): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/cementerio/${idCementerio}`, servicios, {
      headers: this.auth.getHeaders() 
    });
  }
}