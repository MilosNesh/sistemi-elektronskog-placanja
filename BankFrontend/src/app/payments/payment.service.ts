import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { PaymentFormResponse } from "./models/payment-form-response";
import { CardPaymentRequest } from "./models/card-payment-request";
import { baseUrl } from "../env";

@Injectable({providedIn: 'root'})
export class PaymentService {

    constructor(private http: HttpClient) {}

    getPaymentForm(paymentId: number): Observable<PaymentFormResponse>{
        return this.http.get<PaymentFormResponse>(`${baseUrl}/payments/${paymentId}`);
    }

    pay(paymentId: number, request: CardPaymentRequest): Observable<void>{
        return this.http.post<void>(`${baseUrl}/payments/${paymentId}/pay`, request);
    }
}