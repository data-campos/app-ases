import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import * as moment from 'moment';
import { AutorizacaoService } from 'src/app/services/autorizacao.service';
import { Autorizacao } from 'src/app/shared/models/autorizacao.model';

@Component({
  selector: 'app-autorizacao',
  templateUrl: './autorizacao.component.html',
  styleUrls: ['./autorizacao.component.scss'],
})

export class AutorizacaoComponent implements OnInit {

  public statusAutorizacao = new FormControl('i')
  public de = new FormControl(moment().startOf('month').toISOString())
  public ate = new FormControl(moment().endOf('month').toISOString())
  public tipoAutorizacao: any[] = [];
  public autorizacoes: any[] = [];

  autenticando = false;

  constructor(private _autorizacaoService: AutorizacaoService) { }

  ngOnInit() {
    this.tipoAutorizacao = [{nome: 'Autorizado', valor: 0}, 
                            {nome: 'Em análise', valor: 1}, 
                            {nome: 'Negado', valor: 2}]
    
  }

  filtrarAutorizacao(){
    this.autenticando = true;
    this.autorizacoes = [];
    const filtro = {dt_inicio: moment(this.de.value).format('DD-MM-YYYY').toString(), dt_fim: moment(this.ate.value).format('DD-MM-YYYY').toString(), status: this.statusAutorizacao.value.toString()}
    try{
      this._autorizacaoService.getAutorizacoes(filtro.dt_inicio, filtro.dt_fim).subscribe((result: any) => {
        this.autorizacoes = result;
      })
    }finally{
      this.autenticando = false;
    }
  }
}
