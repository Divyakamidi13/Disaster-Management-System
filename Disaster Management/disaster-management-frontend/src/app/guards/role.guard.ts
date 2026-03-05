import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const roleGuard = (allowedRoles: string[]): CanActivateFn => {
  return (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);
    
    const userRole = authService.getUserRole();
    
    if (allowedRoles.includes(userRole)) {
      return true;
    } else {
      router.navigate(['/login']);
      return false;
    }
  };
};
