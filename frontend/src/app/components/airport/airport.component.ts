import { Component, OnInit  } from '@angular/core';
import { DataService } from '../../services/data.service';
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-airport',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './airport.component.html',
  styleUrl: './airport.component.css'
})
export class AirportComponent implements OnInit {
  airports: any[] = [];
  airport = null;
  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.loadAirports();
  }

  loadAirports(): void {
    this.dataService.getAllAirports().subscribe((data) => {
      this.airports = data;
    })
  }

  getAirportByCode(airportCode: string): void {
    this.dataService.getAirportByCode(airportCode).subscribe((data) => {
      this.airport = data;
    })
  }
}
