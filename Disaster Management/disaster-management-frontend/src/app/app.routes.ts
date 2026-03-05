import { Routes } from '@angular/router';
import { LoginComponent } from './components/login.component';
import { RegisterComponent } from './components/register.component';
import { AdminDashboardComponent } from './components/admin-dashboard.component';
import { ResponderDashboardComponent } from './components/responder-dashboard.component';
import { ClientDashboardComponent } from './components/client-dashboard.component';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { 
    path: 'admin-dashboard', 
    component: AdminDashboardComponent,
    canActivate: [authGuard, roleGuard(['ADMIN'])]
  },
  { 
    path: 'responder-dashboard', 
    component: ResponderDashboardComponent,
    canActivate: [authGuard, roleGuard(['AUTHORITY'])]
  },
  { 
    path: 'client-dashboard', 
    component: ClientDashboardComponent,
    canActivate: [authGuard, roleGuard(['CITIZEN'])]
  },
  { path: '**', redirectTo: '/login' }
];
