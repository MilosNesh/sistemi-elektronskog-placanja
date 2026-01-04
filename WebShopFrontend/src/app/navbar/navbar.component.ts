import { Component } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [
    CommonModule,
    RouterLink
],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  token: string = "";
  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(){
    this.authService.token$.subscribe({
      next: (res) => {
        this.token = res;
      }
    })
  }

  logout() {
    this.authService.logout();
    this.router.navigate(["login"])
  }
}
