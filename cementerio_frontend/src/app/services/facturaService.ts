import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auth } from './auth';

@Injectable({
  providedIn: 'root'
})
export class FacturaService {
  private api = 'http://localhost:8090/api/facturas';

  constructor(private http: HttpClient, private auth: Auth) { }

  listarMisFacturas(
    estado: string, 
    campo: string, 
    dir: string, 
    page: number, 
    size: number
  ): Observable<any> {
    const params = new HttpParams()
      .set('estado', estado)
      .set('campo', campo)
      .set('dir', dir)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.api}/mis-facturas`, { 
      headers: this.auth.getHeaders(),
      params: params 
    });
  }

  contratarServicios(idDifunto: number, idsServicios: number[]): Observable<any> {
    const body = { idDifunto, idsCementerioServicios: idsServicios };
    return this.http.post(`${this.api}/contratar`, body, { 
      headers: this.auth.getHeaders() 
    });
  }

  pagarFactura(id: number): Observable<any> {
    return this.http.post(`${this.api}/${id}/pagar`, {}, { 
      headers: this.auth.getHeaders() 
    });
  }

  cancelarFactura(id: number): Observable<any> {
    return this.http.delete(`${this.api}/${id}`, { 
      headers: this.auth.getHeaders() 
    });
  }
}