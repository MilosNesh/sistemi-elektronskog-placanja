import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { AuthService } from "./auth.service";
import { Observable } from "rxjs";
import { PaymentMethod } from "../models/payment-method.model";
import { enivironment } from "../../environments/environment";

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

  public createCryptoPayment(transactionId: string, amount: number): Observable<{btcAddress: string, btcAmount: string}>{
    return this.http.post<{btcAddress: string, btcAmount: string}>(
      `${enivironment.backenUrl}/payment/payments/${transactionId}/crypto`,
      { amount: amount },
      { headers: this.authService.getHeaderToken() }
    );
  }
}
