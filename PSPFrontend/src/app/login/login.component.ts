import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginDetails } from '../models/login-details.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatFormFieldModule,
    FormsModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
 loginForm!: FormGroup
  errorMessage =  ""
  recoveryMessage = ""

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit(): void {
    if(!this.loginForm.valid)
      return;

    var loginDetails: LoginDetails = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    }
    this.recoveryMessage = ""
    this.authService.login(loginDetails).subscribe({
      next: (res) => {
        this.router.navigate(['/verify-code', loginDetails.email]);
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage = err.error?.message || "Greška prilikom prijave.";
      }
  });

  }
}
