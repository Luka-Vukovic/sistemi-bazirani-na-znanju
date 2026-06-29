import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FlightService } from '../../services/flight.service';

@Component({
  selector: 'app-accumulate-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatIconModule, MatButtonModule, MatProgressSpinnerModule],
  template: `
    <div class="dialog-container">
      <div class="dialog-header">
        <div class="dialog-title-row">
          <mat-icon>analytics</mat-icon>
          <h2>Operations Summary</h2>
        </div>
        <button mat-icon-button (click)="dialogRef.close()">
          <mat-icon>close</mat-icon>
        </button>
      </div>

      <div class="dialog-body">
        <div *ngIf="loading" class="loading-state">
          <mat-spinner diameter="36"></mat-spinner>
          <span>Loading report...</span>
        </div>

        <div *ngIf="error" class="error-state">
          <mat-icon>error_outline</mat-icon>
          <span>{{ error }}</span>
        </div>

        <div *ngIf="report && !loading" class="report-grid">
          <div class="metric-card delay">
            <div class="metric-icon"><mat-icon>schedule</mat-icon></div>
            <div class="metric-body">
              <div class="metric-value mono">{{ report.totalDelayMinutes }}</div>
              <div class="metric-unit">minutes</div>
              <div class="metric-label">Total Delay</div>
            </div>
          </div>

          <div class="metric-card flights">
            <div class="metric-icon"><mat-icon>flight</mat-icon></div>
            <div class="metric-body">
              <div class="metric-value mono">{{ report.delayedFlightsCount }}</div>
              <div class="metric-unit">flights</div>
              <div class="metric-label">Delayed Today</div>
            </div>
          </div>

          <div class="metric-card passengers">
            <div class="metric-icon"><mat-icon>people</mat-icon></div>
            <div class="metric-body">
              <div class="metric-value mono">{{ report.affectedPassengers }}</div>
              <div class="metric-unit">passengers</div>
              <div class="metric-label">Affected (Cancelled / Delay &gt; 2h)</div>
            </div>
          </div>

          <div class="metric-card visibility">
            <div class="metric-icon"><mat-icon>visibility</mat-icon></div>
            <div class="metric-body">
              <div class="metric-value mono">{{ report.avgVisibility | number:'1.0-0' }}</div>
              <div class="metric-unit">meters</div>
              <div class="metric-label">Avg. Visibility</div>
            </div>
          </div>
        </div>
      </div>

      <div class="dialog-footer">
        <button mat-button (click)="load()" [disabled]="loading">
          <mat-icon>refresh</mat-icon> Refresh
        </button>
        <button mat-flat-button (click)="dialogRef.close()">Close</button>
      </div>
    </div>
  `,
  styles: [`
    .dialog-container {
      background: var(--bg-surface);
      color: var(--text-primary);
      min-width: 520px;
    }

    .dialog-header {
      display: flex; align-items: center; justify-content: space-between;
      padding: 16px 20px 12px;
      border-bottom: 1px solid var(--border);
    }

    .dialog-title-row {
      display: flex; align-items: center; gap: 10px;
      mat-icon { color: var(--accent); }
      h2 { font-size: 15px; font-weight: 600; color: var(--text-primary); }
    }

    mat-icon[mat-icon-button] { color: var(--text-secondary); }

    .dialog-body { padding: 20px; min-height: 160px; }

    .loading-state, .error-state {
      display: flex; align-items: center; justify-content: center;
      gap: 12px; min-height: 140px;
      color: var(--text-secondary);
    }

    .error-state { color: #EF5350; }

    .report-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 12px;
    }

    .metric-card {
      display: flex; align-items: center; gap: 14px;
      padding: 16px;
      background: var(--bg-surface-2);
      border: 1px solid var(--border);
      border-radius: 6px;

      .metric-icon {
        width: 40px; height: 40px; border-radius: 8px;
        display: flex; align-items: center; justify-content: center;
        flex-shrink: 0;
        mat-icon { font-size: 20px; width: 20px; height: 20px; }
      }

      .metric-body { min-width: 0; }

      .metric-value {
        font-size: 26px; font-weight: 700;
        color: var(--text-primary);
        line-height: 1.1;
      }

      .metric-unit {
        font-size: 11px; color: var(--text-muted);
        text-transform: uppercase; letter-spacing: 0.08em;
        margin-bottom: 2px;
      }

      .metric-label {
        font-size: 12px; color: var(--text-secondary);
        white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
      }

      &.delay .metric-icon    { background: rgba(255,152,0,0.15); mat-icon { color: #FFA726; } }
      &.flights .metric-icon  { background: rgba(33,150,243,0.15); mat-icon { color: #42A5F5; } }
      &.passengers .metric-icon { background: rgba(156,39,176,0.15); mat-icon { color: #AB47BC; } }
      &.visibility .metric-icon { background: rgba(76,175,80,0.15); mat-icon { color: #66BB6A; } }
    }

    .dialog-footer {
      display: flex; justify-content: flex-end; gap: 8px;
      padding: 12px 20px;
      border-top: 1px solid var(--border);

      button[mat-button] { color: var(--text-secondary); }
      button[mat-flat-button] {
        background: var(--accent); color: white;
      }
    }
  `]
})
export class AccumulateDialogComponent implements OnInit {
  report: any = null;
  loading = false;
  error = '';

  constructor(
    public dialogRef: MatDialogRef<AccumulateDialogComponent>,
    private flightService: FlightService
  ) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.error = '';
    this.flightService.getAccumulateReport().subscribe({
      next: (r) => { this.report = r; this.loading = false; },
      error: () => { this.error = 'Failed to load report.'; this.loading = false; }
    });
  }
}
