import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class RescisaoContratoService implements Resolve<any>{

    constructor(
        private _http: HttpClient
    ) { }

    private URL = `${environment.apiUrl}`;

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<any> | Promise<any> | any {

    }

    public solicitarRescisao() {
        return this._http.post(`${this.URL}/rescisao/solicitar`, {});
    }

    public consultaRescisao() {
        return this._http.get(`${this.URL}/rescisao/consultar`);
    }

}
