import { Routes } from '@angular/router';
import { FlightComponent} from "./components/flight/flight.component";
import { AirportComponent} from "./components/airport/airport.component";
import { PassengerComponent} from "./components/passenger/passenger.component";
import { GateComponent} from "./components/gate/gate.component";

export const routes: Routes = [
    {'path': '', component: FlightComponent},
    {'path': 'flights', component: FlightComponent},
    {'path': 'passengers', component: PassengerComponent},
    {'path': 'airports', component: AirportComponent},
    {'path': 'gates', component: GateComponent},
];
