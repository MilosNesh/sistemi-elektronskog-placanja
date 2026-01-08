import { Component } from '@angular/core';
import { MerchantService } from '../services/merchant.service';
import { CommonModule } from '@angular/common';
import { PaymentMethodService } from '../services/payment-method.service';
import { PaymentMethod } from '../models/payment-method.model';

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

  constructor(private merchantService: MerchantService, private paymentMethodService: PaymentMethodService) {}

  ngOnInit(){
    this.paymentMethodService.getByMerchantId(1).subscribe({
      next: (response) => {
        this.paymentMethods = response;
      },
      error: (err) => {
        console.log("Greska pri ucitavanju metoda placanja za merchant id: ", 1);
      }
    })
  }

  loadMerchant(){

  }
}
