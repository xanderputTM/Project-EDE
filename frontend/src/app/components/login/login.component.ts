import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router} from "@angular/router";
declare var google: any;

@Component({
  selector: 'app-login',
  standalone:true,
  imports:[
    CommonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  private router = inject(Router)
  ngOnInit(): void {
    google.accounts.id.initialize({
      client_id: '607776473005-ubl2v6ferci0e19pu0uc8e1kcgdp4d9g.apps.googleusercontent.com',
      callback: (resp: any) => {
        this.handleLogin(resp);
      }
    });

    google.accounts.id.renderButton(document.getElementById("google-btn"), {
      theme: 'filled_blue',
      size: 'large',
      shape: 'rectangle',
      width: 350
    })
  }

  handleLogin(response: any) {
    if (response) {
      sessionStorage.setItem("loggedInUser", response.credential)
      this.router.navigate(['/flights'])
    }
  }
}
