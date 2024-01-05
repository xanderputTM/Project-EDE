import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {

  private apiUrl = 'https://api-gateway-xanderputtm.cloud.okteto.net';

  constructor(private http: HttpClient) {}

  // Airports
  getAllAirports(): Observable<any> {
    return this.http.get(`${this.apiUrl}/airports`);
  }
  getAirportByCode(airportCode : string): Observable<any> {
    return this.http.get(`${this.apiUrl}/airport?code=${airportCode}`)
  }

  // Flights
  getAllFlights(): Observable<any> {
    return this.http.get(`${this.apiUrl}/flights`);
  }

  createFlight(flightData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/flight`, flightData);
  }

  deleteFlight(flightId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/flight/${flightId}`);
  }

  // Gates
  getAllGates(): Observable<any> {
    return this.http.get(`${this.apiUrl}/gates`);
  }

  // Passengers
  getPassengersByFlightNumber(flightNumber: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/flight/passengers?flightNumber=${flightNumber}`)
  }

  createPassenger(passengerData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/api/passenger`, passengerData);
  }

  deletePassenger(passengerId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/api/passenger/${passengerId}`);
  }
}
