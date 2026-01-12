import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {QRCodeComponent} from 'angularx-qrcode';

@Component({
  selector: 'app-payment-qr',
  standalone: true,
  imports: [CommonModule, QRCodeComponent],
  templateUrl: './payment-qr.component.html',
  styleUrl: './payment-qr.component.css'
})
export class PaymentQrComponent implements OnInit {
  qrValue: string | null = null;
  paymentId!: number;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.paymentId = Number(this.route.snapshot.paramMap.get('paymentId'));

    this.http.get(
      `https://localhost:8443/payments/${this.paymentId}/qr`,
      { responseType: 'text' }
    ).subscribe({
      next: qr => this.qrValue = qr,
      error: err => console.error(err)
    });
  }

  qrPay(result: string){

    this.http.post(
      `https://localhost:8443/payments/${this.paymentId}/qr-pay`,
      { qrPayload: result }
    ).subscribe({
      next: (res) => {
        alert("Payment success");
      },
      error: (err) => {
        alert("Payment error");
        // this.errorMessage = err.error?.message || 'Payment failed';
      }
  });
}

}
