import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentMethodDashboardComponent } from './payment-method-dashboard.component';

describe('PaymentMethodDashboardComponent', () => {
  let component: PaymentMethodDashboardComponent;
  let fixture: ComponentFixture<PaymentMethodDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaymentMethodDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PaymentMethodDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
