import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Vehicle } from './models/vehicle.model';
import { AuthService } from './auth/auth.service';
import { Insurance } from './models/insurance.model';
import { AdditionalService } from './models/additional-service.model';
import { Reservation } from './models/reservation.model';
import {environment} from '../environments/environment'

@Injectable({
  providedIn: 'root'
})
export class VehicleService {

  constructor(private http: HttpClient, private authService: AuthService) { }

  public getVehicles() : Observable<Vehicle[]>{
    return this.http.get<Vehicle[]>(`${environment.backendUrl}/vehicle/`, { headers: this.authService.getHeaderToken() });
  }

  public getVehicle(id: number) : Observable<Vehicle>{
    return this.http.get<Vehicle>(`${environment.backendUrl}/vehicle/${id}`, { headers: this.authService.getHeaderToken() });
  }

  public getInsurances() : Observable<Insurance[]>{
    return this.http.get<Insurance[]>(`${environment.backendUrl}/vehicle/insurance/`, { headers: this.authService.getHeaderToken() });
  }

  public getAdditionalServices() : Observable<AdditionalService[]>{
    return this.http.get<AdditionalService[]>(`${environment.backendUrl}/vehicle/additional-service/`, { headers: this.authService.getHeaderToken() });
  }

  public createReservation(reservation: Reservation) : Observable<string> {
    return this.http.post(`${environment.backendUrl}/vehicle/reservation/`, reservation, { headers: this.authService.getHeaderToken() , responseType: 'text'});
  }

  public getReservations() : Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${environment.backendUrl}/vehicle/reservation/`, { headers: this.authService.getHeaderToken() });
  }

  public search(from: string, to: string) : Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${environment.backendUrl}/vehicle/available/${from}/${to}`, { headers: this.authService.getHeaderToken() });
  }
}
