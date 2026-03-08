import { Routes } from '@angular/router';

import { HomeComponent } from './home/home';
import { LoginComponent } from './login/login';
import { RegisterComponent } from './register/register';
import { DashboardComponent } from './dashboard/dashboard';
import { ReportComponent } from './report/report';
import { DisasterDashboardComponent } from './disaster-dashboard/disaster-dashboard';

export const routes: Routes = [

  { path: '', component: HomeComponent },

  { path: 'login', component: LoginComponent },

  { path: 'register', component: RegisterComponent },

  { path: 'dashboard', component: DashboardComponent },

  { path: 'disasters', component: DisasterDashboardComponent },

  { path: 'report', component: ReportComponent }

];