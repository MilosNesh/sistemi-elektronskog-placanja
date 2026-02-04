import { Component } from '@angular/core';
import { MerchantService } from '../services/merchant.service';
import { Merchant } from '../models/merchant.model';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { PaymentMethod } from '../models/payment-method.model';
import { PaymentMethodService } from '../services/payment-method.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-payment-method-configuration',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './payment-method-configuration.component.html',
  styleUrl: './payment-method-configuration.component.css'
})
export class PaymentMethodConfigurationComponent {

  merchant!: Merchant;
  paymentMethods!: PaymentMethod[];
  feedbackMessage: string = '';
  feedbackSuccess: boolean = true;

  constructor(
    private merchantService: MerchantService,
    private authService: AuthService,
    private paymentMethodService: PaymentMethodService
  ) {}

  ngOnInit(){
    this.merchantService.getByEmail(this.authService.getEmail()).subscribe({
      next: (response) => {
        console.log("Ucitan merchant: ", response);
        this.merchant = response;
        this.loadPaymentMethods();
      },
      error: (err) => {
        console.log("Greska pri ucitavanju Merchant-a");
      }
    })
  }

  loadPaymentMethods(){
    this.paymentMethodService.getAvailablePaymentMethods().subscribe({
      next: (response) => {
        this.paymentMethods = response;
        console.log("Payment methods: ", response);
      },
      error: (err) => {
        console.log("Greska pri ucitavanju nacina placanja");
      }
    })
  }

  isMethodEnabled(paymentMethodId: number): boolean {
    if (!this.merchant || !this.merchant.merchantPaymentMethods) {
      return false;
    }

  const method = this.merchant.merchantPaymentMethods.find(pm => {
    return pm.paymentMethodId === paymentMethodId;
  });

    return method ? method.enabled : false;
  }

  togglePaymentMethod(paymentMethodId: number, event: any) {
    const isChecked = event.target.checked;

    if (!isChecked) {
    const enabledCount = this.paymentMethods.filter(pm => {
            const merchantMethod = this.merchant.merchantPaymentMethods.find(
                mpm => mpm.paymentMethodId === pm.paymentMethodId
            );
            return merchantMethod && merchantMethod.enabled;
        }).length;

    console.log("Broj omogućenih metoda koji su u listi: ", enabledCount);
    if (enabledCount <= 1) {
      event.target.checked = true;
      this.feedbackMessage = "You must have at least one payment method enabled";
      return;
    }
  }

    const method = this.merchant.merchantPaymentMethods.find(
      pm => pm.paymentMethodId === paymentMethodId
    );

    if (method) {
      method.enabled = isChecked;
      console.log(`Updated ${paymentMethodId} to: ${isChecked}`);
    } else {
      console.error(`Payment method ${paymentMethodId} not found in merchant data!`);
    }
}

  saveMerchant() {
    this.merchantService.updateMerchant(this.merchant).subscribe({
      next: (response) => {
        console.log('Merchant updated successfully', response);
        this.feedbackMessage = 'Configuration saved successfully!';
        this.feedbackSuccess = true;
      },
      error: (err) => {
        console.error('Error updating merchant', err);
        this.feedbackMessage = 'Error saving configuration!';
        this.feedbackSuccess = false;
      }
    });
  }
}
