import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {

  user = {
    email: '',
    password: ''
  };

  constructor(private http: HttpClient, private router: Router) {}

  login() {

    this.http.post('http://localhost:8080/api/auth/login', this.user)
      .subscribe({

        next: (res: any) => {
          alert("Login successful");
          this.router.navigate(['/dashboard']);
        },

        error: (err: any) => {
          alert("Invalid login");
        }

      });

  }

}