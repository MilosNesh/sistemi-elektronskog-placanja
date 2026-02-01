import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { AuthService } from "./auth.service";
import { Observable } from "rxjs";
import { PaymentMethod } from "../models/payment-method.model";
import { enivironment } from "../../environments/environment";
import { Transaction } from "../models/transaction.model";
import { ETHPayment, EthPaymentRequest } from "../models/eth-payment.model";

@Injectable({
  providedIn: 'root'
})
export class PaymentMethodService {

  constructor(private http: HttpClient, private authService: AuthService) { }

  public getByMerchantId(id: string) : Observable<PaymentMethod[]>{
    return this.http.get<PaymentMethod[]>(`${enivironment.backenUrl}/payment-method/merchant/${id}`, { headers: this.authService.getHeaderToken() });
  }

    public getPaymentMethods() : Observable<PaymentMethod[]>{
    return this.http.get<PaymentMethod[]>(`${enivironment.backenUrl}/payment-method/all`, { headers: this.authService.getHeaderToken() });
  }

  public selectPaymentMethod(paymentMethod: PaymentMethod, transactionId: string) : Observable<string>{
    return this.http.post(`${enivironment.backenUrl}/payment/${transactionId}/make`, paymentMethod,  { responseType: 'text' });
  }

  public redirect(transactionId: string) : Observable<string> {
    return this.http.get(`${enivironment.backenUrl}/payment/redirect/${transactionId}`, {responseType: 'text'});
  }

  public getTransaction(transactionId: string) {
    return this.http.get<Transaction>(
      `${enivironment.backenUrl}/payment/transaction/${transactionId}`
    );
  }

  public createCryptoPayment(transactionId: string, amount: number): Observable<{btcAddress: string, btcAmount: string}>{
    return this.http.post<{btcAddress: string, btcAmount: string}>(
      `${enivironment.backenUrl}/payment/payments/${transactionId}/crypto`,
      { amount: amount },
      { headers: this.authService.getHeaderToken() }
    );
  }

  public getCryptoStatus(paymentId: string): Observable<{ status: string, txHash?: string }> {
    return this.http.get<{ status: string, txHash?: string }>(
      `${enivironment.backenUrl}/payment/payments/${paymentId}/crypto/status`,
      { headers: this.authService.getHeaderToken() }
    );
  }

  public payCryptoPayment(paymentId: string): Observable<void> {
    return this.http.post<void>(
      `${enivironment.backenUrl}/payment/payments/${paymentId}/crypto/pay`,
      {},
      { headers: this.authService.getHeaderToken() }
    );
  }

  public createEthPayment(transactionId: string, amount: number): Observable<ETHPayment> {
    return this.http.post<ETHPayment>(
      `${enivironment.backenUrl}/payment/eth/${transactionId}/create`,
      { amount }
    );
  }

  public sendEth(request: EthPaymentRequest) {
    return this.http.post(`${enivironment.backenUrl}/payment/eth/send`, request, {responseType: 'text'});
  }

  public getBalance(): Observable<string> {
    return this.http.get<string>(`${enivironment.backenUrl}/payment/eth/balance`);
  }
}
