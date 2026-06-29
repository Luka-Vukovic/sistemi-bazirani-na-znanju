import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'flights', pathMatch: 'full' },
  {
    path: 'flights',
    loadComponent: () => import('./pages/flight-list/flight-list.component').then(m => m.FlightListComponent)
  },
  {
    path: 'flights/:id',
    loadComponent: () => import('./pages/flight-detail/flight-detail.component').then(m => m.FlightDetailComponent)
  }
];
