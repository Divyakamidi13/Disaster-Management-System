import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface DisasterAlert {
  id: number;
  title: string;
  description: string;
  location: string;
  date: string;
  severity: string;
  createdBy?: string;
}

@Injectable({
  providedIn: 'root'
})
export class DisasterService {
  private apiUrl = `${environment.apiUrl}/api`;

  constructor(private http: HttpClient) {}

  getAllAlerts(): Observable<DisasterAlert[]> {
    return this.http.get<DisasterAlert[]>(`${this.apiUrl}/alerts`);
  }

  createAlert(alert: Partial<DisasterAlert>): Observable<DisasterAlert> {
    return this.http.post<DisasterAlert>(`${this.apiUrl}/alerts`, alert);
  }

  deleteAlert(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/alerts/${id}`);
  }
}
