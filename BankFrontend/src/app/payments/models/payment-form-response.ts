export interface PaymentFormResponse{
    paymentId: number;
    amount: number;
    currency: string;
    expired: boolean;
}