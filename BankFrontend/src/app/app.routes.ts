import { Routes } from '@angular/router';
import { PaymentPageComponent } from './payments/payment-page/payment-page.component';
import { PaymentQrComponent } from './payment-qr/payment-qr.component';

export const routes: Routes = [
    {
        path: 'pay/:paymentId',
        component: PaymentPageComponent
    },
    {
        path: 'pay/:paymentId/qr',
        component: PaymentQrComponent
    }
];
