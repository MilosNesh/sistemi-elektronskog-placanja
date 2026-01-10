import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-qr-scan',
  standalone: true,
  imports: [],
  templateUrl: './qr-scan.component.html',
  styleUrl: './qr-scan.component.css'
})
export class QrScanComponent {
  scanResult: string | null = null;
  errorMessage: string | null = null;

  constructor(private http: HttpClient) {}

  onScanSuccess(result: string){
    this.scanResult = result;

    this.http.post(
      'https://localhost:8443/payments/qr-pay',
      { qrPayload: result }
    ).subscribe({
      next: (res) => {
        console.log('Payment success', res);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Payment failed';
      }
  });
}
}
