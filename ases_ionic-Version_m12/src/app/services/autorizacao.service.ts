import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Autorizacao } from '../shared/models/autorizacao.model';

@Injectable({
  providedIn: 'root'
})
export class AutorizacaoService {
  private URL = `${environment.apiUrl}`;

  constructor(private _http: HttpClient) { }

  public getAutorizacoes(dt_inicio, dt_fim) {
    const filtroData = dt_inicio + "," + dt_fim
    return this._http.get(`${this.URL}/autorizacao/buscarautorizacao/${filtroData}`)
  }
}
