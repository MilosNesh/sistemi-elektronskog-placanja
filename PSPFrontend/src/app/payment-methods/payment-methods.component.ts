import { Component, OnInit } from '@angular/core';
import { MerchantService } from '../services/merchant.service';
import { CommonModule } from '@angular/common';
import { PaymentMethodService } from '../services/payment-method.service';
import { PaymentMethod } from '../models/payment-method.model';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import {QRCodeComponent} from 'angularx-qrcode';

@Component({
  selector: 'app-payment-methods',
  imports: [
    CommonModule,
    QRCodeComponent,
    RouterModule
  ],
  templateUrl: './payment-methods.component.html',
  styleUrl: './payment-methods.component.css'
})
export class PaymentMethodsComponent implements OnInit {

  paymentMethods: PaymentMethod[] = [];
  merchantId!: string;
  transactionId!: string;

  isCryptoSelected = false;
  btcAddress!: string;
  btcAmount!: string;

  constructor(
    private merchantService: MerchantService,
    private paymentMethodService: PaymentMethodService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(){
  this.merchantId = this.route.snapshot.paramMap.get('merchantId')!;
  this.transactionId = this.route.snapshot.paramMap.get('transactionId')!;

  this.paymentMethodService.getByMerchantId(this.merchantId).subscribe({
    next: (response) => {
      this.paymentMethods = response;
    },
    error: () => {
      console.log("Greška pri učitavanju metoda plaćanja");
    }
  })
  }

  onPaymentMethodSelected(paymentMethod: PaymentMethod) {
    console.log('TYPE:', paymentMethod.type);
    if(paymentMethod.type === 'CRYPTO') {
      this.router.navigate(['/payment/crypto', this.transactionId]);
      return;
    }
    console.log('Selected payment method:', paymentMethod);

    this.paymentMethodService.selectPaymentMethod(paymentMethod, this.transactionId).subscribe({
      next: (response) => {
        console.log('Payment method sent to backend successfully, redirect url: ', response);
        window.location.href = response;
      },
      error: (err) => console.error('Error sending payment method', err)
    });

    // this.paymentMethodService
    // .selectPaymentMethod(paymentMethod, this.transactionId)
    // .subscribe({
    //   next: (response) => window.location.href = response,
    //   error: (err) => console.error(err)
    // });

  }

}
