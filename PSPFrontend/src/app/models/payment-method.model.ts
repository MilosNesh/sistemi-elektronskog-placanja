export interface PaymentMethod {
  paymentMethodId: number;
  type: string;
  image?: string;
  description?: string;
  isAvailable?: boolean;
}
