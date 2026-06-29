import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FlightService } from '../../services/flight.service';
import { AccumulateDialogComponent } from '../../shared/accumulate-dialog/accumulate-dialog.component';
import { FlightDialogComponent } from '../../shared/flight-dialog/flight-dialog.component';

@Component({
  selector: 'app-flight-list',
  standalone: true,
  imports: [
    CommonModule, FormsModule,
    MatTableModule, MatPaginatorModule, MatSortModule,
    MatIconModule, MatButtonModule,
    MatProgressSpinnerModule, MatDialogModule,
    MatTooltipModule, MatSnackBarModule
  ],
  templateUrl: './flight-list.component.html',
  styleUrls: ['./flight-list.component.scss']
})
export class FlightListComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns = ['flightNumber', 'route', 'category', 'departure', 'arrival', 'status', 'delay', 'weather', 'runway', 'actions'];

  loading = false;
  error = '';
  selectedDate = new Date().toISOString().split('T')[0];

  constructor(
    private flightService: FlightService,
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() { this.load(); }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  load() {
    this.loading = true;
    this.error = '';
    this.flightService.getFlights(this.selectedDate).subscribe({
      next: (flights) => { this.dataSource.data = flights; this.loading = false; },
      error: () => { this.error = 'Could not connect to backend.'; this.loading = false; }
    });
  }

  onDateChange() { this.load(); }

  openFlight(row: any, event: Event) {
    this.router.navigate(['/flights', row.flightNumber]);
  }

  openAccumulate() {
    this.dialog.open(AccumulateDialogComponent, { panelClass: 'ops-dialog' });
  }

  openCreate() {
    const ref = this.dialog.open(FlightDialogComponent, {
      panelClass: 'ops-dialog',
      data: { mode: 'create' }
    });
    ref.afterClosed().subscribe(result => { if (result) { this.load(); this.notify('Flight created.'); } });
  }

  openEdit(row: any, event: Event) {
    event.stopPropagation();
    this.flightService.getFlightDetail(row.flightNumber).subscribe(detail => {
      const ref = this.dialog.open(FlightDialogComponent, {
        panelClass: 'ops-dialog',
        data: { mode: 'edit', flight: row, detail }
      });
      ref.afterClosed().subscribe(result => { if (result) { this.load(); this.notify('Flight updated.'); } });
    });
  }

  openDelete(row: any, event: Event) {
    event.stopPropagation();
    const ref = this.dialog.open(FlightDialogComponent, {
      panelClass: 'ops-dialog',
      data: { mode: 'delete', flight: row }
    });
    ref.afterClosed().subscribe(result => { if (result) { this.load(); this.notify('Flight deleted.'); } });
  }

  notify(msg: string) {
    this.snackBar.open(msg, '', {
      duration: 3000,
      verticalPosition: 'top',
      horizontalPosition: 'center',
      panelClass: ['ops-snackbar']
    });
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      'ON_TIME': 'on-time', 'DELAYED': 'delayed', 'CANCELLED': 'cancelled',
      'REDIRECTED': 'redirect', 'SCHEDULED': 'scheduled', 'BOARDED': 'boarded', 'DEPARTED': 'departed'
    };
    return map[status] || 'scheduled';
  }

  getStatusLabel(status: string): string {
    const map: Record<string, string> = {
      'ON_TIME': 'On Time', 'DELAYED': 'Delayed', 'CANCELLED': 'Cancelled',
      'REDIRECTED': 'Redirected', 'SCHEDULED': 'Scheduled', 'BOARDED': 'Boarded', 'DEPARTED': 'Departed'
    };
    return map[status] || status;
  }

  getVisibilityClass(vis: number): string {
    if (vis < 400) return 'val-critical';
    if (vis < 1000) return 'val-warning';
    return '';
  }

  getRwyccClass(rwycc: number): string {
    if (rwycc <= 1) return 'val-critical';
    if (rwycc <= 3) return 'val-warning';
    return '';
  }

  getCategoryLabel(cat: string): string {
    const map: Record<string, string> = {
      'SCHEDULED_COMMERCIAL': 'Commercial', 'BUSINESS_AVIATION': 'Business',
      'CARGO': 'Cargo', 'GENERAL_AVIATION': 'General'
    };
    return map[cat] || cat;
  }
}
