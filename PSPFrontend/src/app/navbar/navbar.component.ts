import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  imports: [
    CommonModule
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  public isLoggedIn: boolean = false;

  constructor(public authService: AuthService, private router: Router) {}

ngOnInit() {
      this.authService.token$.subscribe(token => {
      this.isLoggedIn = !!token;
    });
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/login']); // Preusmeri na login nakon logout-a
  }
}
