import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DisasterEventService } from '../services/disaster-event.service';
import { DisasterEventDTO, DisasterType, SeverityLevel } from '../models/disaster-event.model';

@Component({
  selector: 'app-realtime-alerts',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container-fluid py-4 fade-in">
      <!-- Header -->
      <div class="row mb-4">
        <div class="col-12">
          <div class="d-flex justify-content-between align-items-center">
            <h4 class="mb-0">
              <i class="fas fa-bell text-primary me-2"></i>
              Live Disaster Alerts
            </h4>
            <div class="d-flex gap-2">
              <select class="form-select form-select-sm" 
                      [(ngModel)]="filters.disasterType" 
                      (change)="loadAlerts()"
                      style="width: 150px;">
                <option value="">All Types</option>
                <option *ngFor="let type of disasterTypes" [value]="type">{{ type }}</option>
              </select>
              <select class="form-select form-select-sm" 
                      [(ngModel)]="filters.severity" 
                      (change)="loadAlerts()"
                      style="width: 150px;">
                <option value="">All Severity</option>
                <option *ngFor="let severity of severityLevels" [value]="severity">{{ severity }}</option>
              </select>
              <input type="text" 
                     class="form-control form-control-sm" 
                     [(ngModel)]="filters.location" 
                     (keyup.enter)="loadAlerts()"
                     placeholder="Location filter"
                     style="width: 200px;">
              <button class="btn btn-sm btn-outline-primary" (click)="loadAlerts()">
                <i class="fas fa-search me-1"></i>Filter
              </button>
              <button class="btn btn-sm btn-outline-success" (click)="connectSSE()">
                <i class="fas fa-plug me-1"></i>Live
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Live Status -->
      <div class="row mb-3">
        <div class="col-12">
          <div class="alert alert-info d-flex align-items-center" role="alert">
            <i class="fas fa-info-circle me-2"></i>
            <div>
              <strong>Live Updates:</strong> 
              <span *ngIf="isConnected" class="text-success">
                <i class="fas fa-circle me-1"></i>Connected - Real-time updates active
              </span>
              <span *ngIf="!isConnected" class="text-warning">
                <i class="fas fa-circle me-1"></i>Disconnected - Refresh for updates
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- Loading State -->
      <div *ngIf="isLoading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <p class="mt-2 text-muted">Loading verified alerts...</p>
      </div>

      <!-- Empty State -->
      <div *ngIf="!isLoading && alerts.length === 0" class="text-center py-5">
        <div class="empty-state">
          <i class="fas fa-shield-alt text-muted fa-3x mb-3"></i>
          <h5 class="text-muted">No Verified Alerts</h5>
          <p class="text-muted">No alerts match your current filters.</p>
        </div>
      </div>

      <!-- Alerts Grid -->
      <div *ngIf="!isLoading && alerts.length > 0" class="row">
        <div *ngFor="let alert of alerts" class="col-lg-6 col-xl-4 mb-4">
          <div class="alert-card" [ngClass]="{'auto-approved': alert.autoApproved}">
            <div class="alert-header">
              <div class="alert-title">
                <h6 class="mb-1">{{ alert.title }}</h6>
                <div class="alert-meta">
                  <span class="badge bg-secondary me-2">{{ alert.disasterType }}</span>
                  <span class="badge" [ngClass]="getSeverityBadgeClass(alert.severity)">
                    {{ alert.severity }}
                  </span>
                  <span *ngIf="alert.autoApproved" class="badge bg-info ms-auto">
                    <i class="fas fa-robot me-1"></i>Auto
                  </span>
                </div>
              </div>
              <div class="alert-time">
                <small class="text-muted">
                  <i class="fas fa-clock me-1"></i>
                  {{ alert.createdAt | date:'short' }}
                </small>
              </div>
            </div>
            
            <div class="alert-body">
              <p class="mb-2">{{ alert.description }}</p>
              <div class="alert-location">
                <i class="fas fa-map-marker-alt me-1"></i>
                {{ alert.locationName || 'Unknown Location' }}
              </div>
            </div>
            
            <div class="alert-footer">
              <div class="alert-source">
                <small class="text-muted">Source: {{ alert.source }}</small>
              </div>
              <div class="alert-status">
                <small class="text-success">
                  <i class="fas fa-check-circle me-1"></i>
                  Verified by {{ alert.approvedBy || 'System' }}
                </small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .alert-card {
      border: 1px solid #e0e0e0;
      border-radius: 12px;
      padding: 1rem;
      background: white;
      transition: all 0.3s ease;
      height: 100%;
      display: flex;
      flex-direction: column;
    }
    
    .alert-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    }
    
    .alert-card.auto-approved {
      border-left: 4px solid #17a2b8;
    }
    
    .alert-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 0.5rem;
    }
    
    .alert-title h6 {
      font-weight: 600;
      color: #2c3e50;
    }
    
    .alert-meta {
      display: flex;
      gap: 0.25rem;
      flex-wrap: wrap;
    }
    
    .alert-time {
      font-size: 0.75rem;
    }
    
    .alert-body {
      flex: 1;
      margin-bottom: 1rem;
    }
    
    .alert-body p {
      font-size: 0.9rem;
      color: #555;
      margin-bottom: 0.5rem;
    }
    
    .alert-location {
      font-size: 0.85rem;
      color: #666;
    }
    
    .alert-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 0.75rem;
      border-top: 1px solid #f0f0f0;
      padding-top: 0.5rem;
      margin-top: auto;
    }
  `]
})
export class RealtimeAlertsComponent {
  alerts: DisasterEventDTO[] = [];
  isLoading = false;
  isConnected = false;
  
  filters = {
    disasterType: '',
    severity: '',
    location: ''
  };
  
  disasterTypes = Object.values(DisasterType);
  severityLevels = Object.values(SeverityLevel);
  
  private websocket: WebSocket | null = null;
  private eventSource: EventSource | null = null;

  constructor(private disasterEventService: DisasterEventService) {}

  ngOnInit() {
    this.loadAlerts();
    this.connectSSE();
    
    // Add some mock data to show the component is working
    this.alerts = [
      {
        id: 1,
        title: "Test Earthquake Alert",
        description: "Magnitude 6.2 earthquake detected in Pacific Ocean",
        disasterType: DisasterType.EARTHQUAKE,
        severity: SeverityLevel.HIGH,
        locationName: "Pacific Ocean",
        source: "USGS",
        status: 'VERIFIED' as any,
        autoApproved: false,
        approvedBy: "Admin",
        approvedAt: new Date().toISOString(),
        createdAt: new Date().toISOString()
      },
      {
        id: 2,
        title: "Test Flood Warning",
        description: "Heavy rainfall expected in coastal areas",
        disasterType: DisasterType.FLOOD,
        severity: SeverityLevel.MEDIUM,
        locationName: "Coastal Region",
        source: "Weather Service",
        status: 'VERIFIED' as any,
        autoApproved: true,
        approvedBy: "System",
        approvedAt: new Date().toISOString(),
        createdAt: new Date().toISOString()
      }
    ];
  }

  ngOnDestroy() {
    this.disconnectSSE();
  }

  loadAlerts() {
    // Comment out API call to show mock data
    // this.isLoading = true;
    
    // const activeFilters = Object.fromEntries(
    //   Object.entries(this.filters).filter(([_, value]) => value !== '')
    // );
    
    // this.disasterEventService.getVerifiedAlerts(activeFilters).subscribe({
    //   next: (alerts) => {
    //     this.alerts = alerts;
    //     this.isLoading = false;
    //   },
    //   error: (error) => {
    //     console.error('Error loading alerts:', error);
    //     this.isLoading = false;
    //   }
    // });
    
    // Set loading to false since we have mock data
    this.isLoading = false;
  }

  connectSSE() {
    try {
      this.eventSource = new EventSource('http://localhost:8080/disasters/stream');
      
      this.eventSource.addEventListener('disaster-update', (event: MessageEvent) => {
        const disaster = JSON.parse(event.data);
        this.alerts.unshift(disaster); // Add new alert to top
      });
      
      this.eventSource.addEventListener('summary-update', (event: MessageEvent) => {
        console.log('Summary update received:', JSON.parse(event.data));
      });
      
      this.eventSource.onopen = () => {
        this.isConnected = true;
        console.log('SSE connected');
      };
      
      this.eventSource.onerror = (error) => {
        this.isConnected = false;
        console.error('SSE error:', error);
      };
      
    } catch (error) {
      console.error('Error connecting to SSE:', error);
      this.isConnected = false;
    }
  }

  disconnectSSE() {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
      this.isConnected = false;
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
