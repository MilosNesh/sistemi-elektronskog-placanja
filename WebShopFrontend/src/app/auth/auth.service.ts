import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginDetails } from '../models/login-details.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  public login(loginDetails: LoginDetails) : Observable<string> {
    return this.http.post("http://localhost:8080/user/login", loginDetails, {responseType: 'text'});
  }
  
}
