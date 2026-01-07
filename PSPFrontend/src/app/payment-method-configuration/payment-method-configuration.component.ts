import { Component } from '@angular/core';
import { MerchantService } from '../services/merchant.service';
import { Merchant } from '../models/merchant.model';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-payment-method-configuration',
  imports: [
    CommonModule
  ],
  templateUrl: './payment-method-configuration.component.html',
  styleUrl: './payment-method-configuration.component.css'
})
export class PaymentMethodConfigurationComponent {

  merchant!: Merchant;
  feedbackMessage: string = '';
  feedbackSuccess: boolean = true;

  constructor(private merchantService: MerchantService, private authService: AuthService) {}

  ngOnInit(){
    this.merchantService.getByEmail(this.authService.getEmail()).subscribe({
      next: (response) => {
        this.merchant = response;
        console.log("Merchant: ", this.merchant);
      },
      error: (err) => {
        console.log("Greska pri ucitavanju Merchant-a");
      }
    })
  }

  isMethodEnabled(methodType: string): boolean {
    if (!this.merchant || !this.merchant.paymentMethods) {
      return false;
    }

    const method = this.merchant.paymentMethods.find(
      pm => pm.paymentMethod === methodType
    );

    return method ? method.isEnabled : false;
  }

  togglePaymentMethod(methodType: string, event: any) {
  const isChecked = event.target.checked;

  const method = this.merchant.paymentMethods.find(
    pm => pm.paymentMethod === methodType
  );

  if (method) {
    method.isEnabled = isChecked;
    console.log(`Updated ${methodType} to: ${isChecked}`);
  } else {
    console.error(`Payment method ${methodType} not found in merchant data!`);
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
