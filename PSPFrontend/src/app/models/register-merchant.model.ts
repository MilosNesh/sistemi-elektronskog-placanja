import { MerchantPaymentMethod } from "./merchant-payment-method.model";
export interface RegisterMerchant {
  merchantId?: number;
  merchantEmail: string;
  sellerUrl?: string;
  password?: string;
}
