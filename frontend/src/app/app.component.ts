import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common'; // <-- Dodato
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-root',
  standalone: true,
  // Dodat je CommonModule u imports niz ispod
  imports: [RouterOutlet, MatIconModule, CommonModule], 
  template: `
    <div class="app-shell">
      <header class="topbar">
        <div class="topbar-left">
          <mat-icon>flight_takeoff</mat-icon>
          <span class="app-title">FLIGHT OPS</span>
          <span class="app-subtitle">Decision Support System</span>
        </div>
        <div class="topbar-right">
          <span class="mono text-muted">{{ now | date:'HH:mm:ss' }} UTC</span>
        </div>
      </header>
      <main class="app-content">
        <router-outlet />
      </main>
    </div>
  `,
  styles: [`
    .app-shell { display: flex; flex-direction: column; min-height: 100vh; }

    .topbar {
      display: flex; align-items: center; justify-content: space-between;
      padding: 0 24px; height: 52px;
      background: #0A1520;
      border-bottom: 1px solid var(--border);
      position: sticky; top: 0; z-index: 100;
      flex-shrink: 0;
    }

    .topbar-left { display: flex; align-items: center; gap: 12px; }

    mat-icon { color: var(--accent); font-size: 22px; }

    .app-title {
      font-family: 'JetBrains Mono', monospace;
      font-size: 15px; font-weight: 700;
      color: var(--text-primary);
      letter-spacing: 0.15em;
    }

    .app-subtitle {
      font-size: 11px; color: var(--text-muted);
      text-transform: uppercase; letter-spacing: 0.1em;
      padding-left: 12px; border-left: 1px solid var(--border);
    }

    .app-content { flex: 1; padding: 24px; max-width: 1400px; margin: 0 auto; width: 100%; }
  `]
})
export class AppComponent {
  get now() { return new Date(); }
}