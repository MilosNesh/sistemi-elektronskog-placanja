import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginDetails } from '../models/login-details.model';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  public login(loginDetails: LoginDetails) : Observable<string> {
    return this.http.post("http://localhost:8080/user/login", loginDetails, {responseType: 'text'});
  }
  
  public register(user: User) : Observable<User> {
    return this.http.post<User>("http://localhost:8080/user/register", user);
  }
}
