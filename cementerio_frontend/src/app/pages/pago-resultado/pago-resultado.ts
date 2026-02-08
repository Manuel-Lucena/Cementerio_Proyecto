import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { PaymentService } from '../../services/PaymentService'; 
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-pago-resultado',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './pago-resultado.html',
  styleUrl: './pago-resultado.scss',
})
export class PagoResultado implements OnInit {
  orderId: string | null = null;
  pago: any = null;
  cargando = true;

  constructor(
    private route: ActivatedRoute,
    private paymentService: PaymentService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.orderId = this.route.snapshot.queryParamMap.get('orderId');

    if (this.orderId) {
      this.paymentService.getPaymentStatus(this.orderId)
        .pipe(
          finalize(() => {
            this.cargando = false;
            this.cdr.detectChanges();
          })
        )
        .subscribe({
          next: (res) => this.pago = res,
          error: () => this.pago = { estado: 'FAILED' }
        });
    } else {
      this.cargando = false;
    }
  }
}