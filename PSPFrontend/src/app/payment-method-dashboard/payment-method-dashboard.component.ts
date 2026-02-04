import { Component } from '@angular/core';
import { PaymentMethod } from '../models/payment-method.model';
import { MerchantService } from '../services/merchant.service';
import { PaymentMethodService } from '../services/payment-method.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';

@Component({
  selector: 'app-payment-method-dashboard',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './payment-method-dashboard.component.html',
  styleUrl: './payment-method-dashboard.component.css'
})
export class PaymentMethodDashboardComponent {

  paymentMethods!: PaymentMethod[];
  merchantId!: string;
  transactionId!: string;
  feedbackMessage: string = '';

  newPaymentMethod: PaymentMethod = {
    paymentMethodId: 0,
    type: '',
    image: '',
    description: ''
  };

  constructor(
    private merchantService: MerchantService,
    private paymentMethodService: PaymentMethodService,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(){
    this.paymentMethodService.getPaymentMethods().subscribe({
      next: (response) => {
        console.log("Nacini placanja", response);
        this.paymentMethods = response;
      },
      error: (err) => {
        console.log("Greska pri ucitavanju metoda placanja za merchant id: ", 1);
      }
    })
  }

  onAddMethod() {
    // Provera da polja nisu prazna (osnovna validacija)
    if (!this.newPaymentMethod.type || !this.newPaymentMethod.description) {
      alert("Please fill in required fields");
      return;
    }

    console.log("Šaljem objekat:", this.newPaymentMethod);

    this.paymentMethodService.createPaymentMethod(this.newPaymentMethod).subscribe({
      next: (res: PaymentMethod) => {
        this.paymentMethods.push(res);
        // Resetovanje modela na početne vrednosti nakon čuvanja
        this.newPaymentMethod = { paymentMethodId: 0, type: '', image: '', description: '' };
      },
      error: (err) => console.error("Greška pri čuvanju:", err)
    });
  }

  toggleMethod(method: PaymentMethod, event: any) {
    const availableCount = this.paymentMethods.filter(pm => pm.isAvailable).length;
    console.log("AVAILABLE COUNT: ", availableCount);
    if(method.isAvailable && availableCount <= 1){
      event.target.checked = true;
      this.feedbackMessage = "You must have at least one payment method enabled";
      return;
    }

    if (method.isAvailable) {
      this.paymentMethodService.deactivatePaymentMethod(method).subscribe(updated => {
        this.updatePaymentMethods(updated);
        this.feedbackMessage = "";
        console.log("Metoda deaktivirana!");
      });
    } else {
      this.paymentMethodService.activatePaymentMethod(method).subscribe(updated => {
        this.updatePaymentMethods(updated);
        this.feedbackMessage = "";
        console.log("Metoda aktivirana!");
      });
    }
  }

  private updatePaymentMethods(updatedMethod: PaymentMethod) {
    const index = this.paymentMethods.findIndex(pm => pm.paymentMethodId === updatedMethod.paymentMethodId);
    if (index !== -1) {
      // Menjamo ceo objekat u nizu novim objektom sa backenda
      this.paymentMethods[index] = updatedMethod;
    }
  }

  isMethodAvailable(method: PaymentMethod): boolean {

    return method.isAvailable ? true : false;
  }
}
