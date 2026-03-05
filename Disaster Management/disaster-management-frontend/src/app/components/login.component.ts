import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, LoginRequest } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="auth-container fade-in">
      <div class="auth-card">
        <div class="auth-header">
          <h4 class="mb-0">Sign In</h4>
          <small class="d-block mt-2">Access your disaster alert dashboard</small>
        </div>
        
        <div class="auth-body">
          <form (ngSubmit)="onSubmit()">
            <div class="mb-3 floating-label">
              <input 
                type="email" 
                id="email" 
                [(ngModel)]="loginData.email" 
                name="email" 
                required
                class="form-control"
                placeholder=" ">
              <label for="email">Email Address</label>
            </div>
            
            <div class="mb-3 floating-label password-toggle">
              <input 
                type="password" 
                id="password" 
                [(ngModel)]="loginData.password" 
                name="password" 
                required
                class="form-control"
                placeholder=" ">
              <label for="password">Password</label>
              <button type="button" (click)="togglePassword('password')">
                <i class="fas" [ngClass]="showPassword ? 'fa-eye-slash' : 'fa-eye'"></i>
              </button>
            </div>
            
            <div *ngIf="errorMessage" class="alert alert-danger">
              <i class="fas fa-exclamation-triangle me-2"></i>{{ errorMessage }}
            </div>
            
            <button type="submit" [disabled]="isLoading" class="btn btn-primary w-100">
              <span *ngIf="!isLoading">
                <i class="fas fa-sign-in-alt me-2"></i>Sign In
              </span>
              <span *ngIf="isLoading" class="spinner-border spinner-border-sm me-2"></span>
              <span *ngIf="isLoading">Signing in...</span>
            </button>
          </form>
          
          <div class="text-center mt-4">
            <small class="text-muted">Don't have an account? 
              <a (click)="navigateToRegister()" class="text-primary fw-semibold">Create Account</a>
            </small>
          </div>
        </div>
      </div>
    </div>
  `
})
export class LoginComponent {
  loginData: LoginRequest = {
    email: '',
    password: ''
  };
  isLoading = false;
  errorMessage = '';
  showPassword = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    if (!this.loginData.email || !this.loginData.password) {
      this.errorMessage = 'Please fill in all fields';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    // Add timeout to prevent infinite loading
    const timeout = setTimeout(() => {
      if (this.isLoading) {
        this.isLoading = false;
        this.errorMessage = 'Connection timeout. Backend server may not be running. Try mock login for testing.';
      }
    }, 5000); // 5 second timeout

    this.authService.login(this.loginData).subscribe({
      next: (response) => {
        clearTimeout(timeout);
        // Store token and user data
        this.authService.storeUserData(response.token, response);
        
        // Redirect based on role
        this.redirectBasedOnRole(response.role);
      },
      error: (error) => {
        clearTimeout(timeout);
        if (error.status === 0) {
          this.errorMessage = 'Cannot connect to backend. Try mock login below for testing.';
        } else if (error.status === 400) {
          this.errorMessage = 'Invalid email or password';
        } else if (error.status === 401) {
          this.errorMessage = 'Invalid credentials';
        } else {
          this.errorMessage = error.error?.error || 'Login failed. Please try again.';
        }
        this.isLoading = false;
      }
    });
  }

  mockLogin(role: string): void {
    const mockResponse = {
      token: 'mock-jwt-token-' + Date.now(),
      userId: 1,
      email: this.loginData.email,
      role: role
    };
    
    this.authService.storeUserData(mockResponse.token, mockResponse);
    this.redirectBasedOnRole(role);
  }

  private redirectBasedOnRole(role: string): void {
    switch (role) {
      case 'ADMIN':
        this.router.navigate(['/admin-dashboard']);
        break;
      case 'AUTHORITY':
        this.router.navigate(['/responder-dashboard']);
        break;
      case 'CITIZEN':
        this.router.navigate(['/client-dashboard']);
        break;
      default:
        this.router.navigate(['/login']);
    }
  }

  togglePassword(fieldId: string): void {
    const field = document.getElementById(fieldId) as HTMLInputElement;
    this.showPassword = !this.showPassword;
    field.type = this.showPassword ? 'text' : 'password';
  }

  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }
}
