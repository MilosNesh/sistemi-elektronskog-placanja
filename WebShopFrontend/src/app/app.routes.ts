import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegistrationComponent } from './auth/registration/registration.component';
import { HomeComponent } from './home/home.component';
import { VehicleComponent } from './vehicle/vehicle.component';
import { NavbarComponent } from './navbar/navbar.component';

export const routes: Routes = [
    { path: "login", component: LoginComponent },
    { path: "registration", component: RegistrationComponent},
    { path: "home", component: HomeComponent },
    { path: "vehicle/:id", component: VehicleComponent },
    { path: "navbar", component: NavbarComponent }
]; 
