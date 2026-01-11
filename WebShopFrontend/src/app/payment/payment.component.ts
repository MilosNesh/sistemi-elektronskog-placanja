import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Reservation } from '../models/reservation.model';
import { VehicleService } from '../vehicle.service';

@Component({
  selector: 'app-payment',
  imports: [CommonModule],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent implements OnInit{
  status!: string;
  id!: string;
  reservation: Reservation | null = null;
  constructor(private route: ActivatedRoute, private vehicleService: VehicleService) {}

  ngOnInit(): void {
    // this.status = this.route.snapshot.paramMap.get('status') || "";
    this.id = this.route.snapshot.paramMap.get('id') || "";
    this.vehicleService.getReservation(this.id).subscribe({
      next: (res) => {
        this.reservation = res
      }
    })
  }
}
