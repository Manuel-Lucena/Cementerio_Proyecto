import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Difunto } from '../models/difunto';
import { Auth } from './auth';

@Injectable({
  providedIn: 'root'
})
export class DifuntoService {
  private urlDifuntos = 'http://localhost:8090/api/difuntos';
  private urlServicios = 'http://localhost:8090/api/cementerio-servicios';

  constructor(private http: HttpClient, private auth: Auth) { }

  getMisDifuntos(): Observable<Difunto[]> {
    return this.http.get<Difunto[]>(`${this.urlDifuntos}/mis-difuntos`, {
      headers: this.auth.getHeaders()
    });
  }


  getServiciosPorCementerio(idCementerio: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.urlServicios}/cementerio/${idCementerio}`, {
      headers: this.auth.getHeaders() 
    });
  }
}