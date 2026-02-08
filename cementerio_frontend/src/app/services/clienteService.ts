import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente'; 
import { Auth } from './auth';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {


  private apiUrl = 'http://localhost:8090/api/clientes'; 
  
 
  private authApiUrl = 'http://localhost:8090/api/auth'; 

  constructor(private http: HttpClient, private auth: Auth) { }

  listarTodos(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.apiUrl, {
      headers: this.auth.getHeaders()
    });
  }

  registrar(data: any): Observable<Cliente> {
    const request = {
      ...data,
      username: data.email,
      role: 'CLIENTE'       
    };

    return this.http.post<Cliente>(`${this.authApiUrl}/register`, request, {
      headers: this.auth.getHeaders()
    });
  }

  
  actualizar(id: number, data: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.apiUrl}/${id}`, data, {
      headers: this.auth.getHeaders()
    });
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.auth.getHeaders()
    });
  }
}