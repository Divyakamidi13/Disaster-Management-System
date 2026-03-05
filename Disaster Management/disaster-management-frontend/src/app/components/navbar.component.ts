import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark">
      <div class="container">
        <a class="navbar-brand" href="#">
          <i class="fas fa-shield-alt me-2"></i>
          Disaster Alert System
        </a>
        
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto">
            <li class="nav-item" *ngIf="userRole === 'ADMIN'">
              <a class="nav-link" (click)="navigateTo('/admin-dashboard')">
                <i class="fas fa-tachometer-alt me-2"></i>Dashboard
              </a>
            </li>
            <li class="nav-item" *ngIf="userRole === 'RESPONDER'">
              <a class="nav-link" (click)="navigateTo('/responder-dashboard')">
                <i class="fas fa-tachometer-alt me-2"></i>Dashboard
              </a>
            </li>
            <li class="nav-item" *ngIf="userRole === 'CLIENT'">
              <a class="nav-link" (click)="navigateTo('/client-dashboard')">
                <i class="fas fa-tachometer-alt me-2"></i>Dashboard
              </a>
            </li>
          </ul>
          
          <ul class="navbar-nav">
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown">
                <span class="role-badge me-2">{{ userRole }}</span>
                <i class="fas fa-map-marker-alt me-2"></i>
                {{ userRegion }}
                <i class="fas fa-user-circle ms-2"></i>
              </a>
              <ul class="dropdown-menu dropdown-menu-end">
                <li>
                  <a class="dropdown-item" href="#">
                    <i class="fas fa-user me-2"></i>Profile
                  </a>
                </li>
                <li><hr class="dropdown-divider"></li>
                <li>
                  <a class="dropdown-item text-danger" (click)="logout()">
                    <i class="fas fa-sign-out-alt me-2"></i>Logout
                  </a>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  `,
  styles: [`
    .role-badge {
      background: rgba(255, 255, 255, 0.2);
      color: white;
      padding: 0.25rem 0.75rem;
      border-radius: 20px;
      font-size: 0.875rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
  `]
})
export class NavbarComponent {
  userRole = '';
  userRegion = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.userRole = this.authService.getUserRole();
    // You can get region from stored user data
    const user = localStorage.getItem('user');
    if (user) {
      const userData = JSON.parse(user);
      this.userRegion = userData.region || 'Unknown';
    }
  }

  navigateTo(path: string): void {
    this.router.navigate([path]);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
