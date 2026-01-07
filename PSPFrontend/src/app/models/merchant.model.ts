import { PaymentMethod } from './payment-method.model';

export interface Merchant {
  merchantId?: number;
  merchantEmail: string;
  merchantPassword?: string;
  sellerUrl?: string;
  port?: number;
  successUrl?: string;
  failedUrl?: string;
  errorUrl?: string;
  paymentMethods: PaymentMethod[];
}
