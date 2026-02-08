import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auth } from './auth';

@Injectable({
  providedIn: 'root'
})
export class EstadisticaService {
  private apiUrl = 'http://localhost:8090/api/estadisticas';

  constructor(
    private http: HttpClient,
    private auth: Auth 
  ) { }

  getResumen(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/resumen`, { 
      headers: this.auth.getHeaders() 
    });
  }
}