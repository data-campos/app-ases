import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AlteracaoService implements Resolve<any>{

    constructor(
        private _http: HttpClient
    ) { }

    private URL = `${environment.apiUrl}`;

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<any> | Promise<any> | any {

    }

    getPortalAlteracao() {
        return this._http.get(`${this.URL}/portal/alteracao/obter`);
    }

    solicitarAlteracao(dados: any) {
        return this._http.put(`${this.URL}/portal/alteracao/solicitar`, dados);
    }

    getCEP(cep) {
        return this._http.get(`https://viacep.com.br/ws/${cep}/json`);
    }

    getListaAlteracao() {
        return this._http.get(`${this.URL}/portal/alteracao/listar`);
    }
}
