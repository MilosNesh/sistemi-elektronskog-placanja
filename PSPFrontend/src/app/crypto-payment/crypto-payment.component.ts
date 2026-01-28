import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { QRCodeComponent } from 'angularx-qrcode';
import { PaymentMethodService } from '../services/payment-method.service';

@Component({
  selector: 'app-crypto-payment',
  standalone: true,
  imports: [CommonModule, QRCodeComponent],
  templateUrl: './crypto-payment.component.html',
  styleUrl: './crypto-payment.component.css'
})
export class CryptoPaymentComponent implements OnInit {
  transactionId!: string;

  btcAddress!: string;
  btcAmount!: string;

  loading = true;
  error?: string;

  constructor(private route: ActivatedRoute, private paymentMethodService: PaymentMethodService){}

  ngOnInit(): void {
    this.transactionId = this.route.snapshot.paramMap.get('transactionId')!;

    // za sada hardkod amount
    this.paymentMethodService
    .createCryptoPayment(this.transactionId, 1) // 1 RSD
    .subscribe({
      next: (data) =>{
        this.btcAddress = data.btcAddress;
        this.btcAmount = data.btcAmount;
        this.loading = false;
      },
      error: () =>{
        this.error = "Greska pri kreiranju crypto placanja";
        this.loading = false;
      }
    });
  }

  get btcQrData(): string {
    return `bitcoin:${this.btcAddress}?amount=${this.btcAmount}`;
  }
}
