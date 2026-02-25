import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from './core/api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  view = 'login';
  role: 'CUSTOMER' | 'OFFICER' = 'CUSTOMER';
  user: any;
  alert = '';
  booking: any;
  trackingResult: any;
  history: any[] = [];
  shipments: any[] = [];

  loginForm = this.fb.group({
    userId: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]],
    password: ['', [Validators.required, Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{8,30}$/)]],
    role: ['CUSTOMER', Validators.required]
  });

  registerForm = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(50)]],
    email: ['', [Validators.required, Validators.email]],
    countryCode: ['+91', Validators.required],
    mobile: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
    address: ['', [Validators.required, Validators.maxLength(250)]],
    userId: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]],
    password: ['', [Validators.required, Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{8,30}$/)]],
    confirmPassword: ['', [Validators.required]],
    preferences: ['Email alerts and fragile handling']
  });

  bookingForm = this.fb.group({
    customerId: [''], senderName: ['', Validators.required], senderAddress: ['', Validators.required], senderContact: ['', Validators.required],
    receiverName: ['', Validators.required], receiverAddress: ['', Validators.required], receiverPin: ['', [Validators.required, Validators.pattern(/^\d{6}$/)]], receiverContact: ['', Validators.required],
    parcelSize: ['MEDIUM', Validators.required], parcelWeight: [1, [Validators.required, Validators.min(0.1)]], contents: ['', Validators.required],
    deliverySpeed: ['STANDARD', Validators.required], packaging: ['Standard Packaging', Validators.required], pickupTime: ['', Validators.required], dropOffTime: ['', Validators.required],
    paymentMethod: ['Debit', Validators.required], insurance: [false], trackingService: [true]
  });

  paymentForm = this.fb.group({
    cardNo: ['', [Validators.required, Validators.pattern(/^\d{16}$/)]],
    cardHolder: ['', Validators.required],
    expiry: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/[0-9]{2}$/)]],
    cvv: ['', [Validators.required, Validators.pattern(/^\d{3}$/)]]
  });

  constructor(private fb: FormBuilder, private api: ApiService) {}

  doLogin() {
    if (this.loginForm.invalid) return;
    this.api.login(this.loginForm.value).subscribe({
      next: (res) => {
        this.user = res;
        this.role = res.role;
        if (this.role === 'OFFICER') this.loadShipments();
        this.view = this.role === 'CUSTOMER' ? 'customer-home' : 'officer-home';
        this.alert = '';
      },
      error: (err) => this.alert = err.error?.message || 'Invalid credentials'
    });
  }

  doRegister() {
    if (this.registerForm.invalid || this.registerForm.value.password !== this.registerForm.value.confirmPassword) {
      this.alert = 'Please fix registration details'; return;
    }
    this.api.register(this.registerForm.value).subscribe({
      next: (res) => { this.user = res; this.view = 'ack'; this.alert = ''; },
      error: (err) => this.alert = err.error?.message || 'Registration failed'
    });
  }

  prepareBooking() {
    if (this.role === 'CUSTOMER' && this.user) {
      this.api.customer(this.user.userId).subscribe(c => {
        this.bookingForm.patchValue({
          customerId: c.userId, senderName: c.name, senderAddress: c.address, senderContact: `${c.countryCode} ${c.mobile}`
        });
      });
    }
    if (this.role === 'OFFICER') this.bookingForm.patchValue({ customerId: 'OFFICER-BOOKING' });
    this.view = 'booking';
  }

  submitBooking() {
    if (this.bookingForm.invalid) return;
    this.api.createBooking(this.bookingForm.value).subscribe({
      next: (res) => { this.booking = res; this.view = 'payment'; this.alert = ''; },
      error: (err) => this.alert = err.error?.message || 'Booking failed'
    });
  }

  payNow() {
    if (this.paymentForm.invalid || !this.booking) return;
    this.api.pay(this.booking.bookingId, this.paymentForm.value).subscribe({
      next: (res) => { this.booking = res; this.view = 'invoice'; this.alert = 'Payment Successful'; },
      error: () => this.alert = 'Payment failed'
    });
  }

  track(bookingId: string) {
    this.api.track(bookingId).subscribe({ next: (res) => this.trackingResult = res, error: () => this.alert = 'Booking not found' });
  }

  loadHistory(customerId: string = this.user?.userId) {
    this.api.customerHistory(customerId).subscribe(h => this.history = h);
  }

  loadOfficerHistory(customerId: string, from: string, to: string) {
    this.api.officerHistory(customerId, from, to).subscribe(h => this.history = h);
  }

  loadShipments() { this.api.shipments().subscribe(s => this.shipments = s); }

  updatePickup(bookingId: string, dt: string) { this.api.updatePickup(bookingId, dt).subscribe(() => this.loadShipments()); }
  updateStatus(bookingId: string, status: string) { this.api.updateStatus(bookingId, status).subscribe(() => this.loadShipments()); }

  logout() {
    this.user = null; this.booking = null; this.trackingResult = null; this.history = []; this.alert='';
    this.loginForm.reset({ role: 'CUSTOMER' });
    this.view = 'login';
  }
}
