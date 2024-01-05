import { Routes } from '@angular/router';
import { FlightComponent} from "./components/flight/flight.component";
import { AirportComponent} from "./components/airport/airport.component";
import { PassengerComponent} from "./components/passenger/passenger.component";
import { GateComponent} from "./components/gate/gate.component";
import {LoginComponent} from "./components/login/login.component";

export const routes: Routes = [
    {'path': '', loadComponent: ()=> import('./components/login/login.component').then(a=> a.LoginComponent)},
    {'path': 'flights', component: FlightComponent},
    {'path': 'passengers', component: PassengerComponent},
    {'path': 'airports', component: AirportComponent},
    {'path': 'gates', component: GateComponent},
    {'path': 'login', loadComponent: ()=> import('./components/login/login.component').then(a=> a.LoginComponent)},

];
