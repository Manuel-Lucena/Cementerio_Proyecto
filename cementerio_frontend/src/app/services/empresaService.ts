import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auth } from './auth'; 

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {
  private apiUrl = 'http://localhost:8090/api/empresas';

  constructor(
    private http: HttpClient,
    private auth: Auth 
  ) { }

  listar(): Observable<any[]> {
    const opciones = { headers: this.auth.getHeaders() };
    const respuesta = this.http.get<any[]>(this.apiUrl, opciones);
    
    return respuesta;
  }

  crear(formData: FormData): Observable<any> {
    const opciones = { headers: this.auth.getHeaders() };
    const respuesta = this.http.post(this.apiUrl, formData, opciones);
    
    return respuesta;
  }

  actualizar(id: number, formData: FormData): Observable<any> {
    const opciones = { headers: this.auth.getHeaders() };
    const url = `${this.apiUrl}/${id}`;
    const respuesta = this.http.put(url, formData, opciones);
    
    return respuesta;
  }

  eliminar(id: number): Observable<any> {
    const opciones = { headers: this.auth.getHeaders() };
    const url = `${this.apiUrl}/${id}`;
    const respuesta = this.http.delete<any>(url, opciones);
    
    return respuesta;
  }
}