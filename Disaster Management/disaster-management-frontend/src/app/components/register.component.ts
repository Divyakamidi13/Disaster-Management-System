import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, RegisterRequest } from '../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="auth-container fade-in">
      <div class="auth-card">
        <div class="auth-header">
          <h4 class="mb-0">Create Account</h4>
          <small class="d-block mt-2">Join Disaster Alert System</small>
        </div>
        
        <div class="auth-body">
          <form (ngSubmit)="onSubmit()">
            <div class="mb-3 floating-label">
              <input 
                type="text" 
                id="name" 
                [(ngModel)]="registerData.name" 
                name="name" 
                required
                class="form-control"
                placeholder=" ">
              <label for="name">Full Name</label>
              <div *ngIf="errors.name" class="text-danger small mt-1">{{ errors.name }}</div>
            </div>
            
            <div class="mb-3 floating-label">
              <input 
                type="email" 
                id="email" 
                [(ngModel)]="registerData.email" 
                name="email" 
                required
                class="form-control"
                placeholder=" ">
              <label for="email">Email Address</label>
              <div *ngIf="errors.email" class="text-danger small mt-1">{{ errors.email }}</div>
            </div>
            
            <div class="mb-3 floating-label password-toggle">
              <input 
                type="password" 
                id="password" 
                [(ngModel)]="registerData.password" 
                name="password" 
                required
                class="form-control"
                placeholder=" ">
              <label for="password">Password</label>
              <button type="button" (click)="togglePassword('password')">
                <i class="fas" [ngClass]="showPassword ? 'fa-eye-slash' : 'fa-eye'"></i>
              </button>
              <div *ngIf="errors.password" class="text-danger small mt-1">{{ errors.password }}</div>
            </div>
            
            <div class="mb-3">
              <label for="role" class="form-label">Role</label>
              <select 
                id="role" 
                [(ngModel)]="registerData.role" 
                name="role" 
                required
                class="form-select">
                <option value="">Select Role</option>
                <option value="ADMIN">Administrator</option>
                <option value="AUTHORITY">Emergency Responder</option>
                <option value="CITIZEN">Citizen</option>
              </select>
              <div *ngIf="errors.role" class="text-danger small mt-1">{{ errors.role }}</div>
            </div>
            
            <div class="mb-3 floating-label">
              <input 
                type="text" 
                id="region" 
                [(ngModel)]="registerData.region" 
                name="region" 
                required
                class="form-control"
                placeholder=" ">
              <label for="region">Region/District</label>
              <div *ngIf="errors.region" class="text-danger small mt-1">{{ errors.region }}</div>
            </div>
            
            <div *ngIf="errorMessage" class="alert alert-danger">
              <i class="fas fa-exclamation-triangle me-2"></i>{{ errorMessage }}
            </div>
            
            <div *ngIf="successMessage" class="alert alert-success">
              <i class="fas fa-check-circle me-2"></i>{{ successMessage }}
            </div>
            
            <button type="submit" [disabled]="isLoading" class="btn btn-primary w-100">
              <span *ngIf="!isLoading">
                <i class="fas fa-user-plus me-2"></i>Create Account
              </span>
              <span *ngIf="isLoading" class="spinner-border spinner-border-sm me-2"></span>
              <span *ngIf="isLoading">Creating...</span>
            </button>
          </form>
          
          <div class="text-center mt-4">
            <small class="text-muted">Already have an account? 
              <a (click)="navigateToLogin()" class="text-primary fw-semibold">Sign In</a>
            </small>
          </div>
        </div>
      </div>
    </div>
  `
})
export class RegisterComponent {
  registerData: RegisterRequest = {
    name: '',
    email: '',
    password: '',
    role: '',
    region: ''
  };
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  errors: any = {};
  showPassword = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.errors = {};
    this.errorMessage = '';
    this.successMessage = '';

    // Validation
    if (!this.registerData.name) {
      this.errors.name = 'Name is required';
    }
    
    if (!this.registerData.email) {
      this.errors.email = 'Email is required';
    } else if (!this.isValidEmail(this.registerData.email)) {
      this.errors.email = 'Please enter a valid email';
    }
    
    if (!this.registerData.password) {
      this.errors.password = 'Password is required';
    } else if (this.registerData.password.length < 6) {
      this.errors.password = 'Password must be at least 6 characters';
    }
    
    if (!this.registerData.role) {
      this.errors.role = 'Role is required';
    }
    
    if (!this.registerData.region) {
      this.errors.region = 'Region is required';
    }

    if (Object.keys(this.errors).length > 0) {
      return;
    }

    this.isLoading = true;

    // Add timeout to prevent infinite loading
    const timeout = setTimeout(() => {
      if (this.isLoading) {
        this.isLoading = false;
        this.errorMessage = 'Registration taking too long. Please try again or check backend connection.';
      }
    }, 5000); // Reduced from 10 seconds to 5 seconds

    this.authService.register(this.registerData).subscribe({
      next: (response) => {
        clearTimeout(timeout);
        this.successMessage = 'Account created successfully! Redirecting to login...';
        this.isLoading = false;
        
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1000); // Reduced from 2000ms to 1000ms for faster redirect
      },
      error: (error) => {
        clearTimeout(timeout);
        if (error.status === 0) {
          this.errorMessage = 'Cannot connect to backend. Please ensure the server is running on http://localhost:8080';
        } else if (error.status === 400) {
          this.errorMessage = error.error?.error || 'Registration failed. Please check your details.';
        } else {
          this.errorMessage = error.error?.error || 'Registration failed. Please try again.';
        }
        this.isLoading = false;
      }
    });
  }

  isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  togglePassword(fieldId: string): void {
    const field = document.getElementById(fieldId) as HTMLInputElement;
    this.showPassword = !this.showPassword;
    field.type = this.showPassword ? 'text' : 'password';
  }

  navigateToLogin(): void {
    this.router.navigate(['/login']);
  }
}
