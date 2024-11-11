import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class HomeService implements Resolve<any>{

    public users: BehaviorSubject<any[]> = new BehaviorSubject([]);

    constructor(
        private _http: HttpClient
    ) { }

    private URL = `${environment.apiUrl}`;

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<any> | Promise<any> | any {

    }

    public getDadosPortal() {
        return this._http.get(`${this.URL}/portal/basico`);
    }

}
