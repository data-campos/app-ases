import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class GuiaMedicoService implements Resolve<any>{

    constructor(
        private _http: HttpClient,
    ) { }

    private URL = `${environment.apiUrl}`;

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<any> | Promise<any> | any {}

    public getEspecialidade() {
        return this._http.get(`${this.URL}/guia/lista-especialidade`);
    }

    public filtrarGuia(dados) {
        return this._http.post(`${this.URL}/guia/listar`, dados);
    }

}
