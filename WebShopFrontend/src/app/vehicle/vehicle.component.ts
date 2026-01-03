import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Vehicle } from '../models/vehicle.model';
import { VehicleService } from '../vehicle.service';
import { Insurance } from '../models/insurance.model';
import { AdditionalService } from '../models/additional-service.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Reservation } from '../models/reservation.model';

@Component({
  selector: 'app-vehicle',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './vehicle.component.html',
  styleUrl: './vehicle.component.css'
})
export class VehicleComponent {
  vehicleId!: number
  vehicle!: Vehicle;
  insurances: Insurance[] = [];
  additionalServices: AdditionalService[] = []
  openReservation: boolean = false;
  selectedInsurance: Insurance | null = null;
  selectedServices: AdditionalService[] = [];
  startDate: string = '';
  endDate: string = '';
  minStartDate: string = '';
  minEndDate: string = '';

  constructor(private route: ActivatedRoute, private vehicleService: VehicleService, private router: Router) {}
  
  ngOnInit(): void {
    this.vehicleId = Number(this.route.snapshot.paramMap.get('id'));
    const today = new Date();
    this.minStartDate = this.formatDate(today); 
    this.minEndDate = this.minStartDate;
    this.vehicleService.getVehicle(this.vehicleId).subscribe({
      next: (res) => {
        this.vehicle = res;
        this.vehicleService.getInsurances().subscribe({
          next: (ins) => {
            this.insurances = ins;
            this.vehicleService.getAdditionalServices().subscribe({
              next: (adds) => {
                this.additionalServices = adds;
              }
            })
          }
        })
      }
    })
  }

  openReservationDetails() {
    this.openReservation = !this.openReservation;
  }

  onServiceChange(service: AdditionalService, event: Event) {
    const checked = (event.target as HTMLInputElement).checked;

    if (checked) {
      this.selectedServices.push(service);
    } else {
      this.selectedServices =
        this.selectedServices.filter(s => s.id !== service.id);
    }
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

  getNumberOfDays(): number {
    if (!this.startDate || !this.endDate) return 0;
    const start = new Date(this.startDate);
    const end = new Date(this.endDate);
    const diffTime = end.getTime() - start.getTime();
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }

  isDateRangeValid(): boolean {
    return !!this.startDate && !!this.endDate && this.getNumberOfDays() > 0;
  }

  calculateTotalPrice(): number {
    const days = this.getNumberOfDays();

    if (!this.vehicle) return 0;

    let total = this.vehicle.pricePerDay * days;

    if (this.selectedInsurance) {
      total += this.selectedInsurance.pricePerDay * days;
    }

    this.selectedServices.forEach(service => {
      if (service) total += service.price;
    });

    return total;
  }

  createReservation() {
    if(this.getNumberOfDays() < 1)
      return;

    var reservation: Reservation = {
      totalPrice: this.calculateTotalPrice(),
      currency: this.vehicle.currency,
      dateFrom: this.startDate,
      dateTo: this.endDate,
      paymentStatus: 0,
      userDTO:  null,
      vehicleDTO: this.vehicle,
      insuranceDTO: this.selectedInsurance,
      additionalServiceDTOs: this.selectedServices
    }

    console.log(reservation)

    this.vehicleService.createReservation(reservation).subscribe({
      next: (res) => {
        this.router.navigate(["home"]);
      }
    })
  }


}
