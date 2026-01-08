import { Routes } from '@angular/router';
import { PaymentMethodConfigurationComponent } from './payment-method-configuration/payment-method-configuration.component';
import { LoginComponent } from './login/login.component';
import { PaymentMethodsComponent } from './payment-methods/payment-methods.component';

export const routes: Routes = [
  { path: 'payment-method-config', component: PaymentMethodConfigurationComponent},
  { path: 'login', component: LoginComponent},
  { path: 'payment-method', component: PaymentMethodsComponent}
];
