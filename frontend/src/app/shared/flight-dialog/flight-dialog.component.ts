import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTabsModule } from '@angular/material/tabs';
import { FlightService } from '../../services/flight.service';

export interface FlightDialogData {
  mode: 'create' | 'edit' | 'delete';
  flight?: any;
  detail?: any;
}

@Component({
  selector: 'app-flight-dialog',
  standalone: true,
  imports: [
    CommonModule, FormsModule,
    MatDialogModule, MatIconModule, MatButtonModule,
    MatProgressSpinnerModule, MatTabsModule
  ],
  templateUrl: './flight-dialog.component.html',
  styleUrls: ['./flight-dialog.component.scss']
})
export class FlightDialogComponent implements OnInit {
  form: any = this.emptyForm();
  saving = false;
  errorMsg = '';

  constructor(
    public dialogRef: MatDialogRef<FlightDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: FlightDialogData,
    private flightService: FlightService
  ) {}

  ngOnInit() {
    if (this.data.mode === 'edit' && this.data.detail) {
      this.populateForm(this.data.detail);
    }
  }

  emptyForm() {
    return {
      flightNumber: null as number | null,
      route: '',
      category: 'SCHEDULED_COMMERCIAL',
      status: 'SCHEDULED',
      plannedDeparture: '',
      plannedArrival: '',
      passengerCount: 150,
      delayMinutes: 0,
      hasReplacementAircraft: false,
      hasReplacementCrew: false,
      aircraft: {
        age: 10, totalFlightHours: 5000,
        flightHoursSinceService: 200, cyclesSinceService: 100,
        nextServiceDate: ''
      },
      weather: {
        windSpeed: 20, windDirection: 270, crosswind: 15, tailwind: 5,
        visibility: 5000, temperature: 10, dewPoint: 3,
        precipitationType: 'NONE', precipitationIntensity: 'NONE', icingPresent: false
      },
      runway: { status: 'OPEN', rwycc: 6, runwaysBeingDeiced: false, deicingComplete: true },
      airport: {
        totalRunways: 2, availableRunways: 2, runwayLength: 3000,
        runwayHeading: 270, freeGates: 5, capacity: 50,
        lvtoCapability: true, lvtoPermit: true, specialPermit: false
      },
      crew: { complete: true, nightDuty: false, fdp: 6, restBeforeFlight: 14, sectorsToday: 1 }
    };
  }

  populateForm(detail: any) {
    const f = detail.flight;
    const ac = f.aircraft || {};
    this.form = {
      flightNumber: f.flightNumber,
      route: f.route,
      category: f.category,
      status: f.status,
      plannedDeparture: f.plannedDeparture?.substring(0, 16) || '',
      plannedArrival: f.plannedArrival?.substring(0, 16) || '',
      passengerCount: f.passengerCount,
      delayMinutes: f.delayMinutes || 0,
      hasReplacementAircraft: f.hasReplacementAircraft,
      hasReplacementCrew: f.hasReplacementCrew,
      aircraft: {
        age: ac.age, totalFlightHours: ac.totalFlightHours,
        flightHoursSinceService: ac.flightHoursSinceService,
        cyclesSinceService: ac.cyclesSinceService,
        nextServiceDate: ac.nextServiceDate || ''
      },
      weather: { ...detail.weather },
      runway: { ...detail.runway },
      airport: { ...detail.airport },
      crew: {
        complete: detail.crew?.isComplete ?? detail.crew?.complete ?? true,
        nightDuty: detail.crew?.isNightDuty ?? detail.crew?.nightDuty ?? false,
        fdp: detail.crew?.fdp,
        restBeforeFlight: detail.crew?.restBeforeFlight,
        sectorsToday: detail.crew?.sectorsToday
      }
    };
  }

  save() {
    this.errorMsg = '';
    if (!this.validate()) return;
    this.saving = true;

    const body = {
      ...this.form,
      plannedDeparture: this.form.plannedDeparture + ':00',
      plannedArrival: this.form.plannedArrival + ':00'
    };

    const obs = this.data.mode === 'create'
      ? this.flightService.createFlight(body)
      : this.flightService.updateFlight(this.form.flightNumber, body);

    obs.subscribe({
      next: () => { this.saving = false; this.dialogRef.close(true); },
      error: (e: any) => { this.saving = false; this.errorMsg = e.error || 'Failed to save.'; }
    });
  }

  confirmDelete() {
    this.saving = true;
    this.flightService.deleteFlight(this.data.flight.flightNumber).subscribe({
      next: () => { this.saving = false; this.dialogRef.close(true); },
      error: () => { this.saving = false; }
    });
  }

  validate(): boolean {
    if (!this.form.flightNumber)         { this.errorMsg = 'Flight number is required.'; return false; }
    if (!this.form.route)                { this.errorMsg = 'Route is required.'; return false; }
    if (!this.form.plannedDeparture)     { this.errorMsg = 'Planned departure is required.'; return false; }
    if (!this.form.plannedArrival)       { this.errorMsg = 'Planned arrival is required.'; return false; }
    if (!this.form.aircraft.nextServiceDate) { this.errorMsg = 'Next service date is required.'; return false; }
    return true;
  }
}
