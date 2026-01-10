export interface CardPaymentRequest {
  pan: string;
  securityCode: string;
  cardHolderName: string;
  expiryDate: string; // MM/YY
}
