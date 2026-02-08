import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auth } from './auth';

@Injectable({
  providedIn: 'root'
})
export class ConcesionService {
  private apiUrl = 'http://localhost:8090/api/concesiones';

  constructor(private http: HttpClient, private auth: Auth) { }

  crearConcesion(datos: any): Observable<any> {
    return this.http.post(this.apiUrl, datos, { 
      headers: this.auth.getHeaders() 
    });
  }

  // Cambiamos 'listarPorUsuario' a 'listarPorCliente' para que coincida con el componente
  listarPorCliente(idCliente: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/cliente/${idCliente}`, {
      headers: this.auth.getHeaders()
    });
  }

  // Opcional: Si tu API usa la ruta /usuario/ en lugar de /cliente/
  // simplemente cambia la URL interna, pero mantén el nombre de la función:
  /*
  listarPorCliente(idCliente: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/usuario/${idCliente}`, {
      headers: this.auth.getHeaders()
    });
  }
  */
}