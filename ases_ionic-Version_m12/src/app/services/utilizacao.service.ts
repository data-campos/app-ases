import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class UtilizacaoService implements Resolve<any>{

    constructor(
        private _http: HttpClient
    ) { }

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<any> | Promise<any> | any {

    }

    public getPortalUtilizacao(filtro: any) {
        return this._http.post(`${environment.apiUrl}/portal/utilizacao`, filtro);
    }

}
