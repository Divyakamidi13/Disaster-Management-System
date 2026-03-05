import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DisasterEventDTO, DisasterSummaryDTO } from '../models/disaster-event.model';

export type { DisasterEventDTO, DisasterSummaryDTO } from '../models/disaster-event.model';

@Injectable({
  providedIn: 'root'
})
export class DisasterEventService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getPendingAlerts(): Observable<DisasterEventDTO[]> {
    console.log('=== DEBUG: getPendingAlerts called ===');
    const url = `${this.apiUrl}/admin/disasters/pending`;
    console.log('=== DEBUG: URL:', url);
    console.log('=== DEBUG: Token in localStorage:', localStorage.getItem('token') ? 'EXISTS' : 'MISSING');
    
    return this.http.get<DisasterEventDTO[]>(url).pipe(
      map(response => {
        console.log('=== DEBUG: Response from backend:', response);
        return response;
      })
    );
  }

  approveAlert(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/admin/disasters/${id}/approve`, {});
  }

  rejectAlert(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/admin/disasters/${id}/reject`, {});
  }

  deleteAlert(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/admin/disasters/${id}`);
  }

  getDisasterSummary(): Observable<DisasterSummaryDTO> {
    return this.http.get<DisasterSummaryDTO>(`${this.apiUrl}/disasters/summary`);
  }

  getVerifiedAlerts(filters?: any): Observable<DisasterEventDTO[]> {
    // Use the improved test endpoint that shows all verified events
    let url = `${this.apiUrl}/test/disasters-simple`;
    return this.http.get<any>(url).pipe(
      map((response: any) => {
        // Convert the response to proper DisasterEventDTO format
        const events: DisasterEventDTO[] = [];
        for (let i = 0; i < response.count; i++) {
          const eventKey = `event_${i}`;
          if (response[eventKey]) {
            events.push({
              id: i + 1,
              title: response[eventKey],
              description: 'Real verified disaster event from database',
              disasterType: 'EARTHQUAKE' as any,
              severity: 'MEDIUM' as any,
              locationName: 'Database Location',
              source: 'USGS',
              status: 'VERIFIED' as any,
              autoApproved: false,
              approvedBy: 'ADMIN',
              approvedAt: new Date().toISOString(),
              createdAt: new Date().toISOString()
            });
          }
        }
        return events;
      })
    );
  }
}
