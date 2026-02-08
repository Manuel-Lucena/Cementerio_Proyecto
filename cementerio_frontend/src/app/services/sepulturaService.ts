import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Sepultura } from '../models/sepultura';
import { Auth } from './auth';

@Injectable({ providedIn: 'root' })
export class SepulturaService {
  private apiUrl = 'http://localhost:8090/api/sepulturas';

  constructor(private http: HttpClient, private auth: Auth) {}

  listarPorAgrupacion(idAgrupacion: number): Observable<Sepultura[]> {
    const url = `${this.apiUrl}/agrupacion/${idAgrupacion}`;
    return this.http.get<Sepultura[]>(url, { headers: this.auth.getHeaders() });
  }
}