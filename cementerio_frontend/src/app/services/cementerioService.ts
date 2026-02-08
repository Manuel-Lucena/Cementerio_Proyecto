import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cementerio } from '../models/cementerio';
import { Auth } from './auth';

@Injectable({ providedIn: 'root' })
export class CementerioService {
    private apiUrl = 'http://localhost:8090/api/cementerios';

    constructor(private http: HttpClient, private auth: Auth) { }

    listarTodos(): Observable<Cementerio[]> {
        return this.http.get<Cementerio[]>(this.apiUrl, {
            headers: this.auth.getHeaders()
        });
    }

    listarMisCementerios(): Observable<Cementerio[]> {
        return this.http.get<Cementerio[]>(`${this.apiUrl}/mis-cementerios`, {
            headers: this.auth.getHeaders()
        });
    }

    listarPorEmpresa(idEmpresa: string): Observable<Cementerio[]> {
        return this.http.get<Cementerio[]>(`${this.apiUrl}/empresa/${idEmpresa}`, {
            headers: this.auth.getHeaders()
        });
    }

    crear(formData: FormData): Observable<any> {
        return this.http.post(this.apiUrl, formData, {
            headers: this.auth.getHeaders()
        });
    }

    obtenerPorId(id: string): Observable<Cementerio> {
        return this.http.get<Cementerio>(`${this.apiUrl}/${id}`, {
            headers: this.auth.getHeaders()
        });
    }

    actualizar(id: number, data: FormData): Observable<any> {
        return this.http.put(`${this.apiUrl}/${id}`, data, {
            headers: this.auth.getHeaders()
        });
    }

    eliminar(id: number): Observable<any> {
        return this.http.delete(`${this.apiUrl}/${id}`, {
            headers: this.auth.getHeaders()
        });
    }
}