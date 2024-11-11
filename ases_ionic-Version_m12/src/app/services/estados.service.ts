import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class EstadosService {

    public cidadeEstados = 'assets/estados-cidades.json';

    constructor(private _httpCliente: HttpClient) {

    }

    public getCidadeEstados(): Observable<Object> {
        return this._httpCliente.get(this.cidadeEstados);
    }

}