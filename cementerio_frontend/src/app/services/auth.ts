import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'; // Añadido HttpHeaders
import { TokenResponse } from '../models/token';

@Injectable({
  providedIn: 'root'
})
export class Auth {
  private apiUrl = 'http://localhost:8090/api/auth';

  constructor(private http: HttpClient) { }

  getHeaders(): HttpHeaders {
    const token = this.obtenerToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  login(username: string, password: string) {
    return this.http.post<TokenResponse>(
      `${this.apiUrl}/login`,
      { username, password },
      { headers: { 'Content-Type': 'application/json' } }
    );
  }

  private obtenerPayload(): any {
    const token = this.obtenerToken();
    if (!token) return null;
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      return JSON.parse(jsonPayload);
    } catch (e) {
      return null;
    }
  }

  getRol(): string | null {
    const payload = this.obtenerPayload();
    if (payload && payload.roles) {
      return Array.isArray(payload.roles) ? payload.roles[0] : payload.roles;
    }
    return null;
  }

  esAdmin(): boolean { return this.getRol() === 'ADMIN'; }
  esEmpresa(): boolean { return this.getRol() === 'EMPRESA'; }
  esCliente(): boolean { return this.getRol() === 'CLIENTE'; }

  guardarToken(token: string) { localStorage.setItem('token', token); }
  obtenerToken(): string | null { return localStorage.getItem('token'); }
  logout() { localStorage.removeItem('token'); }
  
  obtenerIdUsuario(): number | null {
    const payload = this.obtenerPayload();
    return payload ? payload.id : null;
  }

  estaLogueado(): boolean {
    return !!this.obtenerToken();
  }
}