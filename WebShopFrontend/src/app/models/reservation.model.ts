import { AdditionalService } from "./additional-service.model";
import { Insurance } from "./insurance.model";
import { PaymentStatus } from "./payment-status.enum";
import { User } from "./user.model";
import { Vehicle } from "./vehicle.model";

export interface Reservation {
    totalPrice: number,
    currency: string,
    dateFrom: string,
    dateTo: string,
    paymentStatus: PaymentStatus,
    userDTO: User | null,
    vehicleDTO: Vehicle,
    insuranceDTO: Insurance | null,
    additionalServiceDTOs: AdditionalService[]
}