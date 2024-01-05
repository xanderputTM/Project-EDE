import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {

  private apiUrl = 'http://localhost:8085/api';  // Aanpassen aan jouw API Gateway URL

  constructor(private http: HttpClient) {}

  getAllAirports(): Observable<any> {
    return this.http.get(`${this.apiUrl}/airports`);
  }
  getAirportByCode(airportCode : string): Observable<any> {
    return this.http.get(`${this.apiUrl}?code=${airportCode}`)
  }

  getAllFlights(): Observable<any> {
    return this.http.get(`${this.apiUrl}/api/flight/all`);
  }

  createFlight(flightData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/api/flight`, flightData);
  }

  deleteFlight(flightId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/api/flight/${flightId}`);
  }
}
