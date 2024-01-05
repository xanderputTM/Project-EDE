import { Component, OnInit  } from '@angular/core';
import { DataService } from '../../services/data.service';
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-gate',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './gate.component.html',
  styleUrl: './gate.component.css'
})
export class GateComponent implements OnInit {
  gates: any[] = [];
  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.loadGates();
  }

  loadGates(): void {
    this.dataService.getAllGates().subscribe((data) => {
      this.gates = data;
    })
  }

}
