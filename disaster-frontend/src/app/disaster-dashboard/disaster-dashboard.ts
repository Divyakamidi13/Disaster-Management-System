import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-disaster-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './disaster-dashboard.html',
  styleUrls: ['./disaster-dashboard.css']
})
export class DisasterDashboardComponent implements OnInit {

  map:any;

  disasters:any[]=[];
  filteredDisasters:any[]=[];

  searchLocation:string='';
  selectedSeverity:string='';

  constructor(
    private http:HttpClient,
    @Inject(PLATFORM_ID) private platformId:object
  ){}

  async ngOnInit(){

    if(isPlatformBrowser(this.platformId)){

      const L = await import('leaflet');

      this.initializeMap(L);
      this.loadEarthquakes(L);

    }

  }

  initializeMap(L:any){

    this.map = L.map('map').setView([20.5937,78.9629],4);

    L.tileLayer(
      'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      {maxZoom:18}
    ).addTo(this.map);

  }

  loadEarthquakes(L:any){

    this.http.get<any>(
      'https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson'
    ).subscribe(data=>{

      data.features.forEach((quake:any)=>{

        const coords = quake.geometry.coordinates;

        const lat = coords[1];
        const lon = coords[0];

        const magnitude = quake.properties.mag;
        const location = quake.properties.place;

        let severity='Low';
        if(magnitude>=5) severity='High';
        else if(magnitude>=3) severity='Medium';

        L.marker([lat,lon])
        .addTo(this.map)
        .bindPopup(
          `<b>Earthquake</b><br>
          Location:${location}<br>
          Magnitude:${magnitude}`
        );

        this.disasters.push({
          type:'Earthquake',
          location:location,
          magnitude:magnitude,
          severity:severity
        });

      });

      this.filteredDisasters = this.disasters;

    });

  }

  applyFilters(){

    this.filteredDisasters = this.disasters.filter(d => {

      const locationMatch =
      this.searchLocation=='' ||
      d.location.toLowerCase().includes(this.searchLocation.toLowerCase());

      const severityMatch =
      this.selectedSeverity=='' ||
      d.severity==this.selectedSeverity;

      return locationMatch && severityMatch;

    });

  }

}