import { Component, OnInit  } from '@angular/core';
import { DataService } from '../../services/data.service';
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-flight',
  standalone: true,
  templateUrl: './flight.component.html',
  imports: [
    NgForOf
  ],
  styleUrl: './flight.component.scss'
})
export class FlightComponent implements OnInit{
  flights: any[] = [];
  newFlight: any = {};

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.loadFlights();
  }

  loadFlights(): void {
    this.dataService.getAllFlights().subscribe((data) => {
      this.flights = data;
    });
  }

  createFlight(): void {
    this.dataService.createFlight(this.newFlight).subscribe(
      (response) => {
        console.log('Flight created successfully:', response);
        // Optionally, you can reset the form or perform other actions
        this.newFlight = {};
        this.loadFlights(); // Reload the flights after creating a new one
      },
      (error) => {
        console.error('Error creating flight:', error);
      }
    );
  }

  deleteFlight(flightId: number): void {
    this.dataService.deleteFlight(flightId).subscribe(() => {
      this.loadFlights();
    });
  }
}
