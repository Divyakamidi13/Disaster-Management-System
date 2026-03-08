import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Api } from '../services/api';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-report',
  standalone: true,
  imports:[FormsModule,CommonModule],
  templateUrl: './report.html',
  styleUrl: './report.css'
})
export class ReportComponent {

disaster={
type:'',
location:'',
description:''
}

constructor(private api:Api){}

submitReport(){

this.api.reportDisaster(this.disaster).subscribe({
next:(res)=>{
alert("Disaster Report Submitted")
},
error:(err)=>{
alert("Error submitting report")
}
})

}

}