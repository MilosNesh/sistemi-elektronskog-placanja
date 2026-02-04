import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { AuthService } from "./auth.service";
import { Observable } from "rxjs";
import { Merchant } from "../models/merchant.model";
import { PaymentMethod } from "../models/payment-method.model";
import { enivironment } from "../../environments/environment";
import { RegisterMerchant } from "../models/register-merchant.model";

@Injectable({
  providedIn: 'root'
})
export class MerchantService {

  constructor(private http: HttpClient, private authService: AuthService) { }

  public getById(id: number) : Observable<Merchant>{
    return this.http.get<Merchant>(`${enivironment.backenUrl}/merchant/id/${id}`, { headers: this.authService.getHeaderToken() });
  }

  public getByEmail(email: string) : Observable<Merchant>{
    return this.http.get<Merchant>(`${enivironment.backenUrl}/merchant/email/${email}`, { headers: this.authService.getHeaderToken() });
  }

  public updateMerchant(merchant: Merchant) : Observable<Merchant>{
    return this.http.put<Merchant>(`${enivironment.backenUrl}/merchant`, merchant, { headers: this.authService.getHeaderToken() });
  }

  public registerMerchant(merchantData: RegisterMerchant): Observable<RegisterMerchant> {
  return this.http.post<RegisterMerchant>(`${enivironment.backenUrl}/merchant`, merchantData, { headers: this.authService.getHeaderToken() });
  }

  public getAllMerchants(): Observable<Merchant[]> {
    return this.http.get<Merchant[]>( `${enivironment.backenUrl}/merchant/all`, { headers: this.authService.getHeaderToken() });
  }
}
