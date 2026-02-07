import { Routes } from '@angular/router';
import { PaymentMethodConfigurationComponent } from './payment-method-configuration/payment-method-configuration.component';
import { LoginComponent } from './login/login.component';
import { PaymentMethodsComponent } from './payment-methods/payment-methods.component';
import { PaymentMethodDashboardComponent } from './payment-method-dashboard/payment-method-dashboard.component';
import { RegisterMerchantComponent } from './register-merchant/register-merchant.component';
import { CodeVerificationComponent } from './code-verification/code-verification.component';

export const routes: Routes = [
  { path: 'payment-method-config', component: PaymentMethodConfigurationComponent},
  { path: 'login', component: LoginComponent},
  { path: 'verify-code/:email', component: CodeVerificationComponent},
  { path: 'payment/:transactionId/:merchantId', component: PaymentMethodsComponent},
  { path: 'payment/:transactionId/:merchantId', component: PaymentMethodsComponent},
  { path: 'payment-method-dashboard', component: PaymentMethodDashboardComponent},
  { path: 'merchant/register', component: RegisterMerchantComponent}
];
