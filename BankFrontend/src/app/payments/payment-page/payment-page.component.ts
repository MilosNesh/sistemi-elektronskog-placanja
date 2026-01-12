import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PaymentService } from '../payment.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PaymentFormResponse } from '../models/payment-form-response';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-payment-page',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './payment-page.component.html',
  styleUrl: './payment-page.component.css'
})
export class PaymentPageComponent implements OnInit{
  payment?: PaymentFormResponse;
  loading = true;
  error?: string;

  form!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private paymentService: PaymentService,
    private fb: FormBuilder
  ) {}

  luhnCheck(value: string): boolean {
  let sum = 0;
  let shouldDouble = false;

  // prolazimo unazad kroz sve cifre
  for (let i = value.length - 1; i >= 0; i--) {
    let digit = parseInt(value.charAt(i), 10);
    if (shouldDouble) {
      digit *= 2;
      if (digit > 9) digit -= 9;
    }
    sum += digit;
    shouldDouble = !shouldDouble;
  }
  return sum % 10 === 0;
  }

  panValidator() {
    return (control: any) => {
      const value = control.value?.replace(/\s+/g, '');
      if (!value) return null;
      return this.luhnCheck(value) ? null : { invalidPan: true };
    };
  }

  ngOnInit(): void {
    this.form = this.fb.group({
    pan: ['', [Validators.required, Validators.minLength(16), Validators.maxLength(19),this.panValidator()]],
    cardHolderName: ['', [Validators.required]],
    expiryDate: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/\d{2}$/)]],
    securityCode: ['', [Validators.minLength(3), Validators.maxLength(4)]]
    });

    const paymentId = Number(this.route.snapshot.paramMap.get('paymentId'));
    this.paymentService.getPaymentForm(paymentId).subscribe
    ({
      next: (res: PaymentFormResponse) => {
        console.log(res);
        console.log(res.amount)
        this.payment = res;
        this.loading = false;

        if(res.expired){
          this.form.disable();
        }
      },
      error: () => {
        this.error = 'Payment is not available';
        this.loading = false;
      }
    });
  }



  submit(): void{
    if (!this.payment || this.form.invalid) {
      return;
    }
    console.log(this.payment);

    this.paymentService.pay(this.payment.paymentId, this.form.value as any)
      .subscribe({
        next: () => alert('Payment successful'),
        error: err => this.error = err.error?.message || 'Payment failed'
      });
  }
}
