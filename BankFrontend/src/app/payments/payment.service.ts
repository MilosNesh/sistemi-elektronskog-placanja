import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { PaymentFormResponse } from "./models/payment-form-response";
import { CardPaymentRequest } from "./models/card-payment-request";

@Injectable({providedIn: 'root'})
export class PaymentService {
    private readonly baseUrl = 'http://localhost:8080/payments';

    constructor(private http: HttpClient) {}

    getPaymentForm(paymentId: number): Observable<PaymentFormResponse>{
        return this.http.get<PaymentFormResponse>(`${this.baseUrl}/${paymentId}`);
    }

    pay(paymentId: number, request: CardPaymentRequest): Observable<void>{
        return this.http.post<void>(`${this.baseUrl}/${paymentId}/pay`, request);
    }
}