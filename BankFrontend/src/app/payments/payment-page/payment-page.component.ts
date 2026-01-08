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

  ngOnInit(): void {
    this.form = this.fb.group({
    pan: ['', [Validators.required, Validators.minLength(16), Validators.maxLength(19)]],
    cardHolderName: ['', [Validators.required]],
    expiryDate: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(4)]]
    });

    const paymentId = Number(this.route.snapshot.paramMap.get('paymentId'));
    this.paymentService.getPaymentForm(paymentId).subscribe
    ({
      next: res => {
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

    this.paymentService.pay(this.payment.paymentId, this.form.value as any)
      .subscribe({
        next: () => alert('Payment successful'),
        error: err => this.error = err.error?.message || 'Payment failed'
      });
  }
}
