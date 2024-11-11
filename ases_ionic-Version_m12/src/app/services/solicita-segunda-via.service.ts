import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class SolicitaSegundaViaService implements Resolve<any>{

    constructor(
        private _http: HttpClient
    ) { }

    private URL = `${environment.apiUrl}`;

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<any> | Promise<any> | any {

    }

    public getVia() {
        return this._http.get(`${this.URL}/nova-via/listar`);
    }

    public solicitaVia(nr_seq_motivo) {
        return this._http.post(`${this.URL}/nova-via/${nr_seq_motivo}/solicitar`, {});
    }

}
