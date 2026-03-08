import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {

  user = {
    name: '',
    email: '',
    password: '',
    role: '',
    phone: '',
    region: ''
  };

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  register() {

    this.http.post('http://localhost:8080/api/auth/register', this.user)
      .subscribe({

        next: () => {
          alert("Registration Successful");
          this.router.navigate(['/login']);
        },

        error: () => {
          alert("Registration Failed");
        }

      });

  }

}