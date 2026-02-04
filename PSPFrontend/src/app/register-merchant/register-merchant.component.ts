import { Component } from '@angular/core';
import { MerchantService } from '../services/merchant.service';
import { RegisterMerchant } from '../models/register-merchant.model';
import { FormsModule } from '@angular/forms';
import { Merchant } from '../models/merchant.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register-merchant',
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './register-merchant.component.html',
  styleUrl: './register-merchant.component.css'
})
export class RegisterMerchantComponent {

  constructor(private merchantService: MerchantService) {}

  merchants: Merchant[] = [];
  public merchant: RegisterMerchant = {
    merchantEmail: '',
    password: '',
    sellerUrl: ''
  };

  ngOnInit(){
    this.loadMerchants();
  }

  loadMerchants(){
  this.merchantService.getAllMerchants().subscribe({
        next: (res) => {
          this.merchants = res;
        },
        error: (err) => console.error('Greska pri dobavljanju svih merchant-a')
    });
  }

  onSubmit() {
    this.merchantService.registerMerchant(this.merchant).subscribe({
      next: (res) => {
        console.log('Uspešno sačuvano:', res);
        alert('Trgovac je uspešno registrovan!');
        this.merchant.merchantEmail = '';
        this.merchant.password = '';
        this.merchant.sellerUrl = '';
        this.loadMerchants();
      },
      error: (err) => console.error('Greška pri registraciji novog merchant-a:', err)
    });
  }
}
