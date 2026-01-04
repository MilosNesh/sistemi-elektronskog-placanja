import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginDetails } from '../models/login-details.model';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private tokenSubject = new BehaviorSubject<string>('');
  public token$ = this.tokenSubject.asObservable();

  constructor(private http: HttpClient) {
    this.refreshToken();
  }

  public login(loginDetails: LoginDetails) : Observable<string> {
    return this.http.post("http://localhost:8080/user/login", loginDetails, {responseType: 'text'});
  }
  
  public register(user: User) : Observable<User> {
    return this.http.post<User>("http://localhost:8080/user/register", user);
  }

  public getToken() : string {
    return localStorage.getItem("ws_token") || "";
  }

  public getHeaderToken() : HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.getToken()}` 
    });
  }

  public logout() {
    localStorage.removeItem("ws_token");
    this.refreshToken();
  }

  public refreshToken() {
    this.tokenSubject.next(this.getToken())
  }
}
