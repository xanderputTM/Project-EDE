import { Routes } from '@angular/router';
import { FlightComponent} from "./components/flight/flight.component";
import { TestComponent } from "./components/test/test.component";

export const routes: Routes = [
    {'path': '', component: TestComponent},
    {'path': 'flights', component: FlightComponent}
];
