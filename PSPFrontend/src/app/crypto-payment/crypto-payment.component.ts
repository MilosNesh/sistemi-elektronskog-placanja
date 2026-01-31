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

  amount!: number;
  currency!: string; // RSD

  btcAddress!: string;
  btcAmount!: string;

  loading = true;
  error?: string;

  status = "PENDING";
  txHash?: string;
  private statusInterval: any;

  constructor(private route: ActivatedRoute, private paymentMethodService: PaymentMethodService){}

  ngOnInit(): void {
    this.transactionId = this.route.snapshot.paramMap.get('transactionId')!;

    this.paymentMethodService.getTransaction(this.transactionId).subscribe({
      next: (tx) => {
        this.amount = tx.amount;
        this.currency = tx.currency;

        this.createCryptoPayment();
      },
      error: () => {
        this.error = 'Greška pri učitavanju transakcije';
        this.loading = false;
      }
    });
  }

  private createCryptoPayment(): void {
    this.paymentMethodService
      .createCryptoPayment(this.transactionId, this.amount)
      .subscribe({
        next: (data) => {
          this.btcAddress = data.btcAddress;
          this.btcAmount = data.btcAmount;
          this.loading = false;

          this.startStatusPolling();
        },
        error: () => {
          this.error = 'Greška pri kreiranju crypto plaćanja';
          this.loading = false;
        }
      });
  }

  get btcQrData(): string {
    return `bitcoin:${this.btcAddress}?amount=${this.btcAmount}`;
  }

  private startStatusPolling(): void{
    this.statusInterval = setInterval(() => {
      this.checkPaymentStatus();
    }, 5000);
  }

  private checkPaymentStatus(): void {
    this.paymentMethodService
    .getCryptoStatus(this.transactionId)
    .subscribe({
      next: (res) => {
        this.status = res.status;
        this.txHash = res.txHash;

        if (res.status === 'SUCCESS') {
          clearInterval(this.statusInterval);
          this.onPaymentSuccess();
        }
      },
      error: () => {
      }
    });
  }

  payDemo(): void {
  this.paymentMethodService
    .payCryptoPayment(this.transactionId)
    .subscribe({
      next: () => {
        // odmah proveri status nakon "plaćanja"
        this.checkPaymentStatus();
      },
      error: () => {
        this.error = 'Greška pri demo plaćanju';
      }
    });
  }

  private onPaymentSuccess(): void {
    alert('Payment successful');
  }
}
