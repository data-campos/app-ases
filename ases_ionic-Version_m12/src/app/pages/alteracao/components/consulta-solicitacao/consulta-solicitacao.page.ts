import { AlteracaoService } from './../../../../services/alteracao.service';
import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-consulta-solicitacao',
  templateUrl: './consulta-solicitacao.page.html',
  styleUrls: ['./consulta-solicitacao.page.scss'],
})
export class ConsultaSolicitacaoPage implements OnInit {

  public listaItens: BehaviorSubject<any> = new BehaviorSubject([]);

  constructor(
    private _alteracaoService: AlteracaoService
  ) { }

  ngOnInit() {
    this._alteracaoService.getListaAlteracao().subscribe((resolve: any) => {
      this.listaItens.next(resolve);
    });
  }

}
