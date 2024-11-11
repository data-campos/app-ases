import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class DemonstrativoService implements Resolve<any>{

    constructor(
        private _http: HttpClient,
    ) { }

    private URL = `${environment.apiUrl}`;

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<any> | Promise<any> | any {

    }

    public getMensalidade(ie_sit) {
        return this._http.get(`${this.URL}/mensalidade/listar/${ie_sit}`);
    }

    public getMensalidadeDetalhe(nr_sequencia) {
        return this._http.get(`${this.URL}/mensalidade/${nr_sequencia}/detalhe`);
    }

    public getSegundaVia(nr_titulo, dtVencimento) {
        return this._http.post(`${this.URL}/boleto/${nr_titulo}/gerar/${dtVencimento}`, {});
    }

    public getDespesas(nr_titulo, dt_vencimento) {
        return this._http.get(`${this.URL}/boleto/${nr_titulo}/calcular/${dt_vencimento}`);
    }

}
