import { Component } from '@angular/core';
import { VehicleService } from '../vehicle.service';
import { Vehicle } from '../models/vehicle.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  imports: [
    CommonModule,
    RouterModule,
    FormsModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  vehicles : Vehicle[] = []
  startDate: string = '';
  endDate: string = '';
  minStartDate: string = '';
  minEndDate: string = '';
  constructor(private vehicleService: VehicleService) {}

  ngOnInit() {
    const today = new Date();
    this.minStartDate = this.formatDate(today); 
    this.minEndDate = this.minStartDate;

    this.vehicleService.getVehicles().subscribe({
      next: (res) => {
        this.vehicles = res; 
      }
    })
  }

  onStartDateChange(): void {
    if (this.startDate) {
      const start = new Date(this.startDate);
      start.setDate(start.getDate() + 1); 
      this.minEndDate = this.formatDate(start);

      // ako je end date manji od novog minEndDate → reset
      if (this.endDate && new Date(this.endDate) < new Date(this.minEndDate)) {
        this.endDate = '';
      }
    }
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  search() {
    this.vehicleService.search(this.startDate, this.endDate).subscribe({
      next: (res) => {
        this.vehicles = res;
      }
    })
  }
}
