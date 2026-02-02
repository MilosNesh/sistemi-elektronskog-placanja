import { Component, OnInit } from '@angular/core';
import { MerchantService } from '../services/merchant.service';
import { CommonModule } from '@angular/common';
import { PaymentMethodService } from '../services/payment-method.service';
import { PaymentMethod } from '../models/payment-method.model';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-payment-methods',
  imports: [
    CommonModule,
    RouterModule,
    FormsModule
  ],
  templateUrl: './payment-methods.component.html',
  styleUrl: './payment-methods.component.css'
})
export class PaymentMethodsComponent implements OnInit {

  paymentMethods: PaymentMethod[] = [];
  merchantId!: string;
  transactionId!: string;
  amount!: number;

  // popup i crypto payment state
  showCryptoModal = false;
  cryptoLoading = false;
  cryptoSuccess = false;
  cryptoTxHash?: string;
  cryptoError?: string;

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
      this.paymentMethodService.redirect(this.transactionId).subscribe({
        next: (res) => {
          window.location.href = res;
        }
      })
    },
    error: () => {
      console.log("Greška pri učitavanju metoda plaćanja");
    }
  });

    this.paymentMethodService.getTransaction(this.transactionId).subscribe({
      next: (tx) => {
        console.log(tx);
        this.amount = tx.amount;
      },
      error: (err) => {
        console.log('Error fetching transaction:', err);
      }
    });
  }

  onPaymentMethodSelected(paymentMethod: PaymentMethod) {
    console.log('TYPE:', paymentMethod.type);
    if(paymentMethod.type === 'CRYPTO') {
      this.payWithCrypto();
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

  payWithCrypto(){
    this.showCryptoModal = true;
    this.cryptoLoading = true;
    this.cryptoSuccess = false;
    this.cryptoError = undefined;
    this.cryptoTxHash = undefined;
    
    console.log(this.amount);
    this.paymentMethodService.createEthPayment(this.transactionId, this.amount).subscribe({
      next: (payment: any) => {
        const request = {
          toAddress: payment.toAddress,
          amount: payment.ethAmount
        };
          this.paymentMethodService.sendEth(request).subscribe({
          next: (txHash: string) => {
            this.cryptoTxHash = txHash;
            this.cryptoLoading = false;
            this.cryptoSuccess = true;
          },
          error: (err) => {
            this.cryptoError = err.error || 'Transaction failed';
            this.cryptoLoading = false;
          }
        });
      },
      error: (err) => {
        this.cryptoError = err.error || 'Failed to create ETH payment';
        this.cryptoLoading = false;
      }
    });
  }

  closeCryptoModal() {
    this.showCryptoModal = false;
    window.location.href = 'https://localhost:4300/home';
  }

}
