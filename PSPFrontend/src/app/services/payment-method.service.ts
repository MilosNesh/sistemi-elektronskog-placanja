import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { AuthService } from "./auth.service";
import { Observable } from "rxjs";
import { PaymentMethod } from "../models/payment-method.model";

@Injectable({
  providedIn: 'root'
})
export class PaymentMethodService {

  constructor(private http: HttpClient, private authService: AuthService) { }

  public getByMerchantId(id: string) : Observable<PaymentMethod[]>{
    return this.http.get<PaymentMethod[]>(`http://localhost:8080/payment-method/merchant/${id}`, { headers: this.authService.getHeaderToken() });
  }

    public getPaymentMethods() : Observable<PaymentMethod[]>{
    return this.http.get<PaymentMethod[]>('http://localhost:8080/payment-method/all', { headers: this.authService.getHeaderToken() });
  }

  public selectPaymentMethod(paymentMethod: PaymentMethod, transactionId: string) : Observable<string>{
    console.log("PAYMENT METHOD: ", paymentMethod);
    return this.http.post<string>(`http://localhost:8080/payment/${transactionId}/make`, paymentMethod);
  }


}
