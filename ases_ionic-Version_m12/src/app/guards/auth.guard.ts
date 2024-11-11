import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { MenuController, AlertController } from '@ionic/angular';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  private user: "";

  constructor(
    private _alertController: AlertController,
    private _menuController: MenuController,
    private _authService: AuthService,
    private _router: Router) { }

  canActivate(): boolean {

    if (!JSON.parse(localStorage.getItem('currentUser'))) {
      this._router.navigate(['auth/login']);
      return false;
    }
    return true;
  }
}