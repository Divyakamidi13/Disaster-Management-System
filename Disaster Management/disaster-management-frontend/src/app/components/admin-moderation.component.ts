import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DisasterEventService } from '../services/disaster-event.service';
import { DisasterEventDTO, DisasterSummaryDTO } from '../models/disaster-event.model';

@Component({
  selector: 'app-admin-moderation',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container-fluid py-4 fade-in">
      <!-- Summary Cards -->
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
            <div class="card-icon bg-warning bg-opacity-10 text-warning">
              <i class="fas fa-clock"></i>
            </div>
            <div class="card-value">{{ summary?.totalPending || 0 }}</div>
            <div class="card-label">Pending</div>
          </div>
        </div>
        <div class="col-lg-3 col-md-6 mb-3">
          <div class="dashboard-card">
            <div class="card-icon bg-success bg-opacity-10 text-success">
              <i class="fas fa-check-circle"></i>
            </div>
            <div class="card-value">{{ summary?.totalVerified || 0 }}</div>
            <div class="card-label">Verified</div>
          </div>
        </div>
        <div class="col-lg-3 col-md-6 mb-3">
          <div class="dashboard-card">
            <div class="card-icon bg-info bg-opacity-10 text-info">
              <i class="fas fa-robot"></i>
            </div>
            <div class="card-value">{{ summary?.totalAutoApproved || 0 }}</div>
            <div class="card-label">Auto-Approved</div>
          </div>
        </div>
      </div>

      <!-- Pending Alerts Section -->
      <div class="row">
        <div class="col-12">
          <div class="card border-0 shadow-sm">
            <div class="card-header bg-white border-bottom">
              <div class="d-flex justify-content-between align-items-center">
                <h5 class="mb-0">
                  <i class="fas fa-shield-alt text-warning me-2"></i>
                  Pending Alerts - Admin Verification
                </h5>
                <button class="btn btn-sm btn-outline-primary" (click)="refreshData()">
                  <i class="fas fa-sync-alt me-1"></i>Refresh
                </button>
              </div>
            </div>
            <div class="card-body">
              <!-- Loading State -->
              <div *ngIf="isLoading" class="text-center py-5">
                <div class="spinner-border text-primary" role="status">
                  <span class="visually-hidden">Loading...</span>
                </div>
                <p class="mt-2 text-muted">Loading pending alerts...</p>
              </div>

              <!-- Empty State -->
              <div *ngIf="!isLoading && pendingAlerts.length === 0" class="text-center py-5">
                <div class="empty-state">
                  <i class="fas fa-check-circle text-success fa-3x mb-3"></i>
                  <h5 class="text-muted">No Pending Alerts</h5>
                  <p class="text-muted">All alerts have been reviewed!</p>
                </div>
              </div>

              <!-- Pending Alerts List -->
              <div *ngIf="!isLoading && pendingAlerts.length > 0" class="table-responsive">
                <table class="table table-hover">
                  <thead class="table-light">
                    <tr>
                      <th>Title</th>
                      <th>Type</th>
                      <th>Severity</th>
                      <th>Location</th>
                      <th>Source</th>
                      <th>Created</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr *ngFor="let alert of pendingAlerts">
                      <td>
                        <div class="fw-semibold">{{ alert.title }}</div>
                        <small class="text-muted">{{ alert.description | slice:0:50 }}...</small>
                      </td>
                      <td>
                        <span class="badge bg-secondary">{{ alert.disasterType }}</span>
                      </td>
                      <td>
                        <span class="badge" [ngClass]="getSeverityBadgeClass(alert.severity)">
                          {{ alert.severity }}
                        </span>
                      </td>
                      <td>{{ alert.locationName || 'N/A' }}</td>
                      <td>
                        <span class="badge bg-info">{{ alert.source }}</span>
                      </td>
                      <td>{{ alert.createdAt | date:'short' }}</td>
                      <td>
                        <div class="btn-group btn-group-sm">
                          <button class="btn btn-success" 
                                  (click)="approveAlert(alert.id)"
                                  title="Approve">
                            <i class="fas fa-check"></i>
                          </button>
                          <button class="btn btn-danger" 
                                  (click)="rejectAlert(alert.id)"
                                  title="Reject">
                            <i class="fas fa-times"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class AdminModerationComponent {
  pendingAlerts: DisasterEventDTO[] = [];
  summary: DisasterSummaryDTO | null = null;
  isLoading = false;

  constructor(private disasterEventService: DisasterEventService) {}

  ngOnInit() {
    // Mock login for demonstration
    this.mockLogin();
    this.loadData();
  }

  mockLogin() {
    // Add mock JWT token for demonstration
    const mockToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkFkbWluIiwiaWF0IjoxNTE2MjM5MDIyLCJyb2xlIjoiQURNSU4ifQ.mock';
    const mockUser = {
      id: 1,
      email: 'admin@example.com',
      role: 'ADMIN',
      name: 'Admin User'
    };
    
    localStorage.setItem('token', mockToken);
    localStorage.setItem('user', JSON.stringify(mockUser));
    console.log('=== DEBUG: Mock login completed ===');
  }

  loadData() {
    this.isLoading = true;
    
    // Load pending alerts and summary
    this.disasterEventService.getPendingAlerts().subscribe({
      next: (alerts) => {
        this.pendingAlerts = alerts;
        this.loadSummary();
      },
      error: (error) => {
        console.error('Error loading pending alerts:', error);
        this.isLoading = false;
      }
    });
  }

  loadSummary() {
    this.disasterEventService.getDisasterSummary().subscribe({
      next: (summary) => {
        this.summary = summary;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading summary:', error);
        this.isLoading = false;
      }
    });
  }

  refreshData() {
    this.loadData();
  }

  approveAlert(id: number) {
    if (confirm('Are you sure you want to approve this alert?')) {
      this.disasterEventService.approveAlert(id).subscribe({
        next: () => {
          this.loadData(); // Refresh data
        },
        error: (error) => {
          console.error('Error approving alert:', error);
          alert('Error approving alert. Please try again.');
        }
      });
    }
  }

  rejectAlert(id: number) {
    if (confirm('Are you sure you want to reject this alert?')) {
      this.disasterEventService.rejectAlert(id).subscribe({
        next: () => {
          this.loadData(); // Refresh data
        },
        error: (error) => {
          console.error('Error rejecting alert:', error);
          alert('Error rejecting alert. Please try again.');
        }
      });
    }
  }

  getSeverityBadgeClass(severity: string): string {
    switch (severity) {
      case 'LOW': return 'severity-low';
      case 'MEDIUM': return 'severity-medium';
      case 'HIGH': return 'severity-high';
      case 'CRITICAL': return 'severity-critical';
      default: return 'severity-medium';
    }
  }
}
