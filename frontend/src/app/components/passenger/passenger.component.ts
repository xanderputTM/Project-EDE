import { Component  } from '@angular/core';
import { DataService } from '../../services/data.service';
@Component({
  selector: 'app-passenger',
  standalone: true,
  imports: [],
  templateUrl: './passenger.component.html',
  styleUrl: './passenger.component.css'
})
export class PassengerComponent {
  passengers: any[] = [];
  flights: any[] = [];
  newPassenger: any = {};

  ngOnInit() {
    this.loadFlights();
    this.getPassengersByFlightNumber(this.flights[0]['flightNumber'])
  }

  loadFlights(): void {
    this.dataService.getAllFlights().subscribe((data) => {
      this.flights = data;
    });
  }
  constructor(private dataService: DataService) {
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
      this.getPassengersByFlightNumber(this.flights[0]['flightNumber'])
    },
      (error) => {
      console.error('Error creating passenger:', error);
      });
  }

  deletePassenger(passengerId: number): void {
    this.dataService.deletePassenger(passengerId).subscribe(() => {
      this.getPassengersByFlightNumber(this.flights[0]['flightNumber'])
    })
  }
}
