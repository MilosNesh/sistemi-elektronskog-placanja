import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { AuthService } from "./auth.service";
import { Observable } from "rxjs";
import { Merchant } from "../models/merchant.model";
import { PaymentMethod } from "../models/payment-method.model";

@Injectable({
  providedIn: 'root'
})
export class MerchantService {

  constructor(private http: HttpClient, private authService: AuthService) { }

  public getById(id: number) : Observable<Merchant>{
    return this.http.get<Merchant>(`http://localhost:8080/merchant/id/${id}`, { headers: this.authService.getHeaderToken() });
  }

  public getPaymentMethods() : Observable<PaymentMethod[]>{
    return this.http.get<PaymentMethod[]>('http://localhost:8080/payment-method/all', { headers: this.authService.getHeaderToken() });
  }

  public getByEmail(email: string) : Observable<Merchant>{
    return this.http.get<Merchant>(`http://localhost:8080/merchant/email/${email}`, { headers: this.authService.getHeaderToken() });
  }

  public updateMerchant(merchant: Merchant) : Observable<Merchant>{
    return this.http.put<Merchant>(`http://localhost:8080/merchant`, merchant, { headers: this.authService.getHeaderToken() });
  }
}
