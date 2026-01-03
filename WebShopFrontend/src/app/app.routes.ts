import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegistrationComponent } from './auth/registration/registration.component';

export const routes: Routes = [
    { path: "login", component: LoginComponent },
    { path: "registration", component: RegistrationComponent}
];
