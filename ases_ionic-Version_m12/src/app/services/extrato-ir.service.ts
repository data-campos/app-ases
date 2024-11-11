import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ExtratoIrService implements Resolve<any>{

    constructor(
        private _http: HttpClient,
    ) { }

    private URL = `${environment.apiUrl}`;

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<any> | Promise<any> | any {

    }

    public getImpostoAnosLista() {
        const teste = this._http.get(`${this.URL}/imposto-renda/listar-anos`); 
        return teste;
    }

    public getDemonstrativo(ds_ano) {
        return this._http.post(`${this.URL}/imposto-renda/${ds_ano}/gerar`, {});
    }

}
