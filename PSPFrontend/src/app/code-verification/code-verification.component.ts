import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { VerificationCode } from '../models/verification-code.model';
import { ActivatedRoute, Router } from '@angular/router';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-code-verification',
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './code-verification.component.html',
  styleUrl: './code-verification.component.css'
})
export class CodeVerificationComponent {

  errorMessage =  ""
  userEmail: string = '';
  fullCode: string = '';

  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute){}

ngOnInit(): void {
    this.userEmail = this.route.snapshot.paramMap.get('email') || '';

    if (!this.userEmail) {
      console.error('Email not found in URL!');
    }
  }

  onInput() {
    // Čistimo sve što nisu brojevi (ako korisnik nalepi nešto)
    this.fullCode = this.fullCode.replace(/[^0-9]/g, '');

    // Ako je uneto svih 6, možemo automatski okinuti submit
    if (this.fullCode.length === 6) {
      console.log("Spreman za slanje:", this.fullCode);
    }
  }


  onSubmit() {
    if (this.fullCode.length < 6) return;

    const payload: VerificationCode = {
      email: this.userEmail,
      code: this.fullCode
    };

    this.authService.verifyCode(payload).subscribe({
      next: (jwt: string) => {
        localStorage.setItem("psp_token", jwt);
        this.authService.refreshToken();

        console.log("Role: ", this.authService.getRole())
        console.log("Is admin?: ", this.authService.isAdmin())
        if(this.authService.isAdmin()){
          this.router.navigate(["payment-method-dashboard"]);
        }
        else if(this.authService.isSuperAdmin()){
          this.router.navigate(["merchant/register"]);
        }
        else{
          this.router.navigate(["payment-method-config"]);
        }
      },
      error: (err) => {
        console.error('Login failed', err);
        alert('Invalid code, try again.');
      }
    });
  }
}
