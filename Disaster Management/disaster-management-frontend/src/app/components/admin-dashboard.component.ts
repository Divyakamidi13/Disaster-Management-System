import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../components/navbar.component';
import { AlertsPageComponent } from '../components/alerts-page.component';
import { AdminModerationComponent } from '../components/admin-moderation.component';
import { RealtimeAlertsComponent } from '../components/realtime-alerts.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, NavbarComponent, AlertsPageComponent, AdminModerationComponent, RealtimeAlertsComponent],
  template: `
    <div>
      <app-navbar></app-navbar>
      <div class="container-fluid py-4">
        <!-- Navigation Tabs -->
        <ul class="nav nav-tabs mb-4">
          <li class="nav-item">
            <button class="nav-link" [class.active]="activeTab === 'alerts'" (click)="switchTab('alerts')">
              <i class="fas fa-exclamation-triangle me-2"></i>Alerts Management
            </button>
          </li>
          <li class="nav-item">
            <button class="nav-link" [class.active]="activeTab === 'moderation'" (click)="switchTab('moderation')">
              <i class="fas fa-shield-alt me-2"></i>Admin Moderation
            </button>
          </li>
          <li class="nav-item">
            <button class="nav-link" [class.active]="activeTab === 'realtime'" (click)="switchTab('realtime')">
              <i class="fas fa-bell me-2"></i>Live Updates
            </button>
          </li>
        </ul>
        
        <!-- Tab Content -->
        <div>
          <app-alerts-page *ngIf="activeTab === 'alerts'"></app-alerts-page>
          <app-admin-moderation *ngIf="activeTab === 'moderation'"></app-admin-moderation>
          <app-realtime-alerts *ngIf="activeTab === 'realtime'"></app-realtime-alerts>
        </div>
      </div>
    </div>
  `
})
export class AdminDashboardComponent {
  activeTab = 'alerts';

  switchTab(tab: string) {
    this.activeTab = tab;
  }
}
