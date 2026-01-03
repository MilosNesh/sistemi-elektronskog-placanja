import { Component } from '@angular/core';
import { VehicleService } from '../vehicle.service';
import { Vehicle } from '../models/vehicle.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  vehicles : Vehicle[] = []

  constructor(private vehicleService: VehicleService) {}

  ngOnInit() {
    this.vehicleService.getVehicles().subscribe({
      next: (res) => {
        this.vehicles = res; 
      }
    })
  }
}
