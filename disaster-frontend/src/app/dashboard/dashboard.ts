import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent {

  disasters: any[] = [];

  constructor(private http: HttpClient) {
    this.loadDisasters();
  }

  loadDisasters() {

    this.http.get<any>('http://localhost:8080/api/disasters/all')
      .subscribe(data => {
        this.disasters = data;
      });

  }

}