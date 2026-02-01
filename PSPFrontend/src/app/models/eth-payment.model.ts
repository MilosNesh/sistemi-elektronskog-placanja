export interface EthPaymentRequest{
    toAddress: string;
    amount: number; // ETH
}

export interface ETHPayment{
    paymentId: string;
  fiatAmount: number;
  ethAmount: number;
  fromAddress: string;
  toAddress: string;
  status: string;
  createdAt: string;
}