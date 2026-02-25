import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private authBase = 'http://localhost:8080/api/auth';
  private parcelBase = 'http://localhost:8080/api/parcel';
  constructor(private http: HttpClient) {}

  login(payload: any) { return this.http.post<any>(`${this.authBase}/login`, payload); }
  register(payload: any) { return this.http.post<any>(`${this.authBase}/register`, payload); }
  customer(userId: string) { return this.http.get<any>(`${this.authBase}/customer/${userId}`); }

  createBooking(payload: any) { return this.http.post<any>(`${this.parcelBase}/booking`, payload); }
  pay(bookingId: string, payload: any) { return this.http.post<any>(`${this.parcelBase}/booking/${bookingId}/pay`, payload); }
  track(bookingId: string) { return this.http.get<any>(`${this.parcelBase}/tracking/${bookingId}`); }
  customerHistory(customerId: string, page = 0, size = 10) { return this.http.get<any[]>(`${this.parcelBase}/history/customer/${customerId}?page=${page}&size=${size}`); }
  officerHistory(customerId: string, from: string, to: string) { return this.http.get<any[]>(`${this.parcelBase}/history/officer?customerId=${customerId}&from=${from}&to=${to}`); }
  shipments() { return this.http.get<any[]>(`${this.parcelBase}/officer/shipments`); }
  updatePickup(bookingId: string, pickupTime: string) { return this.http.put<any>(`${this.parcelBase}/officer/${bookingId}/pickup`, { pickupTime }); }
  updateStatus(bookingId: string, status: string) { return this.http.put<any>(`${this.parcelBase}/officer/${bookingId}/status`, { status }); }
}
