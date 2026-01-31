export interface Transaction{
    id: string;
    merchantId: number;
    amount: number;
    currency: string;
    merchantTimestamp: string;
    merchantOrderId: string;
    pspTimestamp: string;
}