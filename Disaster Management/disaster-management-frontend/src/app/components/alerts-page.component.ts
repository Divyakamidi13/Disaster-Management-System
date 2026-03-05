import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DisasterEventService, DisasterEventDTO, DisasterSummaryDTO } from '../services/disaster-event.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-alerts-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container-fluid py-4 fade-in">
      <!-- Dashboard Summary Cards -->
      <div class="row mb-4">
        <div class="col-lg-3 col-md-6 mb-3">
          <div class="dashboard-card">
            <div class="card-icon bg-primary bg-opacity-10 text-primary">
              <i class="fas fa-exclamation-triangle"></i>
            </div>
            <div class="card-value">{{ summary?.totalEvents || 0 }}</div>
            <div class="card-label">Total Events</div>
          </div>
        </div>
        <div class="col-lg-3 col-md-6 mb-3">
          <div class="dashboard-card">
            <div class="card-icon bg-danger bg-opacity-10 text-danger">
              <i class="fas fa-fire"></i>
            </div>
            <div class="card-value">{{ summary?.totalPending || 0 }}</div>
            <div class="card-label">Pending Events</div>
          </div>
        </div>
        <div class="col-lg-3 col-md-6 mb-3">
          <div class="dashboard-card">
            <div class="card-icon bg-success bg-opacity-10 text-success">
              <i class="fas fa-shield-alt"></i>
            </div>
            <div class="card-value">{{ summary?.totalVerified || 0 }}</div>
            <div class="card-label">Verified Events</div>
          </div>
        </div>
        <div class="col-lg-3 col-md-6 mb-3">
          <div class="dashboard-card">
            <div class="card-icon bg-info bg-opacity-10 text-info">
              <i class="fas fa-map-marker-alt"></i>
            </div>
            <div class="card-value">{{ userRole }}</div>
            <div class="card-label">Your Role</div>
          </div>
        </div>
      </div>

      <!-- Loading State -->
      <div class="text-center py-5" *ngIf="isLoading">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <p class="mt-3 text-muted">Loading disaster alerts...</p>
      </div>

      <!-- Error State -->
      <div class="alert alert-danger" *ngIf="errorMessage">
        <i class="fas fa-exclamation-triangle me-2"></i>
        {{ errorMessage }}
      </div>

      <!-- Alerts List -->
      <div class="row" *ngIf="!isLoading && !errorMessage">
        <div class="col-12">
          <div class="card">
            <div class="card-header bg-primary text-white">
              <h5 class="mb-0">
                <i class="fas fa-exclamation-triangle me-2"></i>
                Verified Disaster Alerts
              </h5>
            </div>
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>Title</th>
                      <th>Type</th>
                      <th>Severity</th>
                      <th>Location</th>
                      <th>Source</th>
                      <th>Date</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr *ngFor="let alert of alerts">
                      <td>
                        <strong>{{ alert.title }}</strong>
                        <br>
                        <small class="text-muted">{{ alert.description }}</small>
                      </td>
                      <td>
                        <span class="badge bg-info">{{ alert.disasterType }}</span>
                      </td>
                      <td>
                        <span class="badge" [ngClass]="getSeverityClass(alert.severity)">
                          {{ alert.severity }}
                        </span>
                      </td>
                      <td>{{ alert.locationName }}</td>
                      <td>{{ alert.source }}</td>
                      <td>{{ alert.createdAt | date:'short' }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Empty State -->
      <div class="empty-state text-center py-5" *ngIf="!isLoading && !errorMessage && alerts.length === 0">
        <i class="fas fa-exclamation-triangle fa-3x text-muted mb-3"></i>
        <h4>No Verified Alerts</h4>
        <p class="text-muted">There are no verified disaster alerts at this time. Stay safe!</p>
      </div>
    </div>
  `
})
export class AlertsPageComponent {
  alerts: DisasterEventDTO[] = [];
  summary: DisasterSummaryDTO | null = null;
  isLoading = false;
  errorMessage = '';
  userRole = '';
  userRegion = '';

  constructor(
    private disasterEventService: DisasterEventService,
    private authService: AuthService
  ) {
    this.userRole = this.authService.getUserRole() || 'USER';
    const user = localStorage.getItem('user');
    if (user) {
      const userData = JSON.parse(user);
      this.userRegion = userData.region || 'Unknown';
    }
    this.loadAlerts();
    this.loadSummary();
  }

  loadAlerts(): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.disasterEventService.getVerifiedAlerts().subscribe({
      next: (data) => {
        this.alerts = data;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading alerts:', error);
        this.errorMessage = 'Failed to load disaster alerts. Please try again.';
        this.isLoading = false;
      }
    });
  }

  loadSummary(): void {
    this.disasterEventService.getDisasterSummary().subscribe({
      next: (data) => {
        this.summary = data;
      },
      error: (error) => {
        console.error('Error loading summary:', error);
      }
    });
  }

  getSeverityClass(severity: string): string {
    switch (severity) {
      case 'CRITICAL':
        return 'bg-danger';
      case 'HIGH':
        return 'bg-warning';
      case 'MEDIUM':
        return 'bg-info';
      case 'LOW':
        return 'bg-success';
      default:
        return 'bg-secondary';
    }
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('en-US', { 
      month: 'short', 
      day: 'numeric', 
      year: 'numeric' 
    });
  }
}
