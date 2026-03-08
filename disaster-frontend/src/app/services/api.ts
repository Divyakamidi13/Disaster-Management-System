import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class Api {

  private baseUrl = "http://localhost:8080/api/auth";

  constructor(private http: HttpClient) {}

  register(data:any){
    return this.http.post(this.baseUrl + "/register", data);
  }

  login(data:any){
    return this.http.post(this.baseUrl + "/login", data);
  }

  reportDisaster(data:any){
    return this.http.post("http://localhost:8080/api/disasters",data);
  }

  getDisasters(){
    return this.http.get("http://localhost:8080/api/disasters");
  }

}