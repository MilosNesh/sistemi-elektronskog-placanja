import { MerchantPaymentMethod } from "./merchant-payment-method.model";
export interface Merchant {
  merchantId?: number;
  merchantEmail: string;
  sellerUrl?: string;
  port?: number;
  successUrl?: string;
  failedUrl?: string;
  errorUrl?: string;
  merchantPaymentMethods: MerchantPaymentMethod[];
}
