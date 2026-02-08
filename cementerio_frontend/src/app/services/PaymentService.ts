import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auth } from './auth'; 

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  private urlPayment = 'http://localhost:8090/api/payment';

  constructor(private http: HttpClient, private auth: Auth) { }


  initPayment(idFactura: number): Observable<any> {
    return this.http.post<any>(`${this.urlPayment}/init`, { id_factura: idFactura }, {
      headers: this.auth.getHeaders()
    });
  }

  getPaymentStatus(orderId: string): Observable<any> {
    return this.http.get<any>(`${this.urlPayment}/${orderId}`, {
      headers: this.auth.getHeaders()
    });
  }
}