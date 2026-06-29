import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FlightService } from '../../services/flight.service';

@Component({
  selector: 'app-flight-detail',
  standalone: true,
  imports: [
    CommonModule, FormsModule,
    MatIconModule, MatButtonModule, MatProgressSpinnerModule,
    MatTooltipModule, MatSnackBarModule
  ],
  templateUrl: './flight-detail.component.html',
  styleUrls: ['./flight-detail.component.scss']
})
export class FlightDetailComponent implements OnInit {
  flightNumber!: number;
  detail: any = null;
  loading = false;
  error = '';

  recommendation: any = null;
  conditions: any = null;
  recLoading = false;

  cepStatus: any = null;
  cepLoading = false;
  alarmSeverity = 'LOW';
  weatherForm = { windSpeed: 50, visibility: 500, temperature: -5 };

  severityOptions = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private flightService: FlightService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.flightNumber = +this.route.snapshot.paramMap.get('id')!;
    this.loadDetail();
    this.loadCepStatus();
  }

  loadDetail() {
    this.loading = true;
    this.flightService.getFlightDetail(this.flightNumber).subscribe({
      next: (d) => { this.detail = d; this.loading = false; },
      error: () => { this.error = 'Flight not found.'; this.loading = false; }
    });
  }

  loadCepStatus() {
    this.flightService.getCepStatus(this.flightNumber).subscribe({
      next: (s) => { this.cepStatus = s; }
    });
  }

  evaluate() {
    this.recLoading = true;
    this.recommendation = null;
    this.conditions = null;
    this.flightService.getConditions(this.flightNumber).subscribe({
      next: (r) => {
        this.recommendation = r.recommendation;
        this.conditions = r.unmetConditions || [];
        this.recLoading = false;
        // Osvezi status u detail objektu
        if (r.recommendation && this.detail?.flight) {
          this.detail.flight.status = this.recToStatus(r.recommendation.action);
        }
      },
      error: () => { this.recLoading = false; }
    });
  }

  setWeatherPreset(windSpeed: number, visibility: number, temperature: number) {
    this.weatherForm = { windSpeed, visibility, temperature };
  }

  sendWeather() {
    this.cepLoading = true;
    const { windSpeed, visibility, temperature } = this.weatherForm;
    this.flightService.sendWeatherEvent(this.flightNumber, windSpeed, visibility, temperature).subscribe({
      next: (r) => {
        this.cepStatus = r;
        this.cepLoading = false;
        // Lokalno ažuriraj weather report u detail-u
        if (this.detail?.weather) {
          this.detail.weather.windSpeed  = windSpeed;
          this.detail.weather.visibility = visibility;
          this.detail.weather.temperature = temperature;
        }
        this.notify('Weather event sent', 'info');
      },
      error: () => { this.cepLoading = false; }
    });
  }

  sendAlarm() {
    this.cepLoading = true;
    this.flightService.sendAlarmEvent(this.flightNumber, this.alarmSeverity).subscribe({
      next: (r) => {
        this.cepStatus = r;
        this.cepLoading = false;
        // Odmah ažuriraj alarm counts u detail objektu
        if (r.alarmCounts && this.detail) {
          this.detail.alarmCounts = r.alarmCounts;
        }
        this.notify(`Alarm [${this.alarmSeverity}] registered`, 'warning');
      },
      error: () => { this.cepLoading = false; }
    });
  }

  notify(msg: string, type: 'info' | 'warning' | 'error' = 'info') {
    const panelClass = type === 'warning' ? ['ops-snackbar', 'snackbar-warning']
                     : type === 'error'   ? ['ops-snackbar', 'snackbar-error']
                     : ['ops-snackbar'];
    this.snackBar.open(msg, '', {
      duration: 3000,
      verticalPosition: 'top',
      horizontalPosition: 'center',
      panelClass
    });
  }

  recToStatus(action: string): string {
    const map: Record<string, string> = {
      'CANCEL': 'CANCELLED', 'DELAY': 'DELAYED',
      'REDIRECT': 'REDIRECTED', 'DEPART_ON_TIME': 'ON_TIME'
    };
    return map[action] || 'SCHEDULED';
  }

  goBack() { this.router.navigate(['/flights']); }

  // ── Helpers ──

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

  getRecClass(action: string): string {
    const map: Record<string, string> = {
      'CANCEL': 'cancel', 'DELAY': 'delay', 'REDIRECT': 'redirect', 'DEPART_ON_TIME': 'depart-on-time'
    };
    return map[action] || '';
  }

  getRecIcon(action: string): string {
    const map: Record<string, string> = {
      'CANCEL': 'cancel', 'DELAY': 'schedule', 'REDIRECT': 'alt_route', 'DEPART_ON_TIME': 'check_circle'
    };
    return map[action] || 'help';
  }

  getVisibilityLabel(vis: number): string {
    if (vis < 75)  return 'ZERO';
    if (vis < 400) return 'CRITICAL';
    if (vis < 550) return 'LOW';
    return 'OK';
  }

  getVisibilityClass(vis: number): string {
    if (vis < 400) return 'val-critical';
    if (vis < 550) return 'val-warning';
    return 'val-ok';
  }

  getRwyccLabel(rwycc: number): string {
    const labels: Record<number, string> = {
      0: 'CLOSED', 1: 'ICE', 2: 'SLUSH', 3: 'WET SNOW', 4: 'PACKED SNOW', 5: 'WET', 6: 'DRY'
    };
    return labels[rwycc] || String(rwycc);
  }

  getRwyccClass(rwycc: number): string {
    if (rwycc <= 1) return 'val-critical';
    if (rwycc <= 3) return 'val-warning';
    return 'val-ok';
  }

  getCategoryLabel(cat: string): string {
    const map: Record<string, string> = {
      'SCHEDULED_COMMERCIAL': 'Scheduled Commercial', 'BUSINESS_AVIATION': 'Business Aviation',
      'CARGO': 'Cargo', 'GENERAL_AVIATION': 'General Aviation'
    };
    return map[cat] || cat;
  }

  getMelLabel(mel: string): string {
    const map: Record<string, string> = {
      'A': 'Cat A', 'B': 'Cat B', 'C': 'Cat C', 'D': 'Cat D', 'NOT_ON_MEL': 'Not on MEL'
    };
    return map[mel] || mel;
  }

  getAlarmCountEntries(): { key: string; value: number; cls: string }[] {
    if (!this.detail?.alarmCounts) return [];
    const clsMap: Record<string, string> = {
      'LOW': 'low', 'MEDIUM': 'medium', 'HIGH': 'high', 'CRITICAL': 'critical'
    };
    return Object.entries(this.detail.alarmCounts)
      .filter(([, v]) => (v as number) > 0)
      .map(([k, v]) => ({ key: k, value: v as number, cls: clsMap[k] || 'low' }));
  }

  hasActiveAlarms(): boolean {
    if (!this.detail?.alarmCounts) return false;
    return Object.values(this.detail.alarmCounts).some((v: any) => v > 0);
  }
}
