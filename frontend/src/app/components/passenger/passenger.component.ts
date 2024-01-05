import { Component } from '@angular/core';
import { DataService } from '../../services/data.service';
import { NgForOf } from "@angular/common";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-passenger',
  standalone: true,
  imports: [
    NgForOf,
    FormsModule
  ],
  templateUrl: './passenger.component.html',
  styleUrls: ['./passenger.component.css']
})
export class PassengerComponent {
  passengers: any[] = [];
  flights: any[] = [];
  selectedFlightNumber: string = ""; // Add a variable to store the selected flightNumber
  newPassenger: any = {};

  ngOnInit() {
    this.loadFlights();
  }

  constructor(private dataService: DataService) {
  }

  loadFlights(): void {
    this.dataService.getAllFlights().subscribe((data) => {
      this.flights = data;
      // Set default selected flightNumber
      this.selectedFlightNumber = this.flights.length > 0 ? this.flights[0]['flightNumber'] : "";
      // Call getPassengersByFlightNumber here, after loadFlights is done
      this.getPassengersByFlightNumber(this.selectedFlightNumber);
    });
  }

  getPassengersByFlightNumber(flightNumber: string): void {
    this.dataService.getPassengersByFlightNumber(flightNumber).subscribe((data) => {
      this.passengers = data;
    })
  }

  createPassenger(): void {
    this.dataService.createPassenger(this.newPassenger).subscribe((response) => {
        console.log('Flight created successfully:', response);
        this.newPassenger = {};
        this.getPassengersByFlightNumber(this.selectedFlightNumber);
      },
      (error) => {
        console.error('Error creating passenger:', error);
      });
  }

  deletePassenger(passengerId: number): void {
    this.dataService.deletePassenger(passengerId).subscribe(() => {
      this.getPassengersByFlightNumber(this.selectedFlightNumber);
    });
  }
}
