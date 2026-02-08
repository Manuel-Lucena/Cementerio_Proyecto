import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UbicacionService {

  private apiUrl = 'http://localhost:8090/api/ubicaciones';

  constructor(private http: HttpClient) { }

  getProvincias(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/provincias`);
  }


  getLocalidadesPorProvincia(idProvincia: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/provincias/${idProvincia}/localidades`);
  }


  getLocalidadPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/localidades/${id}`);
  }
}