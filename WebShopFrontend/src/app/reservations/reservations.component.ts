import { Component } from '@angular/core';
import { VehicleService } from '../vehicle.service';
import { Reservation } from '../models/reservation.model';
import { CommonModule } from '@angular/common';
import { PaymentStatus } from '../models/payment-status.enum';

@Component({
  selector: 'app-reservations',
  imports: [
    CommonModule
  ],
  templateUrl: './reservations.component.html',
  styleUrl: './reservations.component.css'
})
export class ReservationsComponent {
  reservations: Reservation[] = []
  constructor(private vehicleService: VehicleService) {}

  ngOnInit() {
    this.vehicleService.getReservations().subscribe({
      next: (res) => {
        this.reservations = res;
      }
    })
  }

  formatDate(dateString: string): string {
    var date = new Date(dateString)
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  isActive(reservation: Reservation): boolean {
    var dateFrom = new Date(reservation.dateFrom);
    var dateTo = new Date(reservation.dateTo);
    console.log(reservation.paymentStatus);
    return dateFrom < new Date() && dateTo > new Date()  && reservation.paymentStatus === PaymentStatus.PAID; 
  }
}
