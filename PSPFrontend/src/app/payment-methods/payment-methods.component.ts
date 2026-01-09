import { Component } from '@angular/core';
import { MerchantService } from '../services/merchant.service';
import { CommonModule } from '@angular/common';
import { PaymentMethodService } from '../services/payment-method.service';
import { PaymentMethod } from '../models/payment-method.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-payment-methods',
  imports: [
    CommonModule
  ],
  templateUrl: './payment-methods.component.html',
  styleUrl: './payment-methods.component.css'
})
export class PaymentMethodsComponent {

  paymentMethods!: PaymentMethod[];
  merchantId!: string;
  transactionId!: string;

  constructor(
    private merchantService: MerchantService,
    private paymentMethodService: PaymentMethodService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(){
    this.merchantId = this.route.snapshot.paramMap.get('merchantId')!;
    this.transactionId = this.route.snapshot.paramMap.get('transactionId')!;

    this.paymentMethodService.getByMerchantId(this.merchantId).subscribe({
      next: (response) => {
        this.paymentMethods = response;
      },
      error: (err) => {
        console.log("Greska pri ucitavanju metoda placanja za merchant id: ", 1);
      }
    })
  }

  onPaymentMethodSelected(paymentMethod: PaymentMethod) {
    console.log('Selected payment method:', paymentMethod);

    this.paymentMethodService.selectPaymentMethod(paymentMethod, this.transactionId).subscribe({
      next: (response) => {
        console.log('Payment method sent to backend successfully, redirect url: ', response);
        window.location.href = response;
      },
      error: (err) => console.error('Error sending payment method', err)
    });

  }

}
