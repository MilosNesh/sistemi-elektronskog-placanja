import { Routes } from '@angular/router';
import { PaymentMethodConfigurationComponent } from './payment-method-configuration/payment-method-configuration.component';
import { LoginComponent } from './login/login.component';

export const routes: Routes = [
  { path: 'payment-method', component: PaymentMethodConfigurationComponent},
  { path: 'login', component: LoginComponent}
];
