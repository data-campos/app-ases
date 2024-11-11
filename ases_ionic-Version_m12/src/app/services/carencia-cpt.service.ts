import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CarenciaCptService implements Resolve<any>{

    constructor(
        private _http: HttpClient
    ) { }

    private URL = `${environment.apiUrl}`;

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<any> | Promise<any> | any {

    }

    public getCarencia() {
        return this._http.get(`${this.URL}/portal/carencia`);
    }

    public getCpt() {
        return this._http.get(`${this.URL}/portal/cpt`);
    }

}
