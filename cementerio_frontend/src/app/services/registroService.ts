import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistroService {

  private URL = 'http://localhost:8090/api/auth/register';

  constructor(private http: HttpClient) { }

  registrar(cliente: any): Observable<any> {
    return this.http.post(this.URL, cliente);
  }
}