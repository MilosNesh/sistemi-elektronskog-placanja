import { Routes } from '@angular/router';
import { PaymentPageComponent } from './payments/payment-page/payment-page.component';

export const routes: Routes = [
    {
        path: 'pay/:paymentId',
        component: PaymentPageComponent
    }
];
