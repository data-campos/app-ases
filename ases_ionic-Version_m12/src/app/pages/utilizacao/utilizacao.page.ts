import { UtilizacaoService } from './../../services/utilizacao.service';
import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';
import { BehaviorSubject } from 'rxjs';
import { FormControl, Validators, FormGroup, FormBuilder } from '@angular/forms';
moment.locale('pt-br');

@Component({
  selector: 'app-utilizacao',
  templateUrl: './utilizacao.page.html',
  styleUrls: ['./utilizacao.page.scss'],
})
export class UtilizacaoPage implements OnInit {

  public ds_prest = new FormControl("");
  public flFilter: boolean = false;
  public form: FormGroup;

  public rows: any[] = [];
  public filteredRows: BehaviorSubject<any[]> = new BehaviorSubject([]);

  constructor(
    private _utilizacaoService: UtilizacaoService,
    private _formBuilder: FormBuilder,
  ) { }

  ngOnInit() {
    const obj = { de: moment().startOf('month').toISOString(), ate: moment().endOf('month').toISOString() };
    this.buildForm();
    this.form.patchValue(obj);
    this.atualizaItens();
  }

  buildForm() {
    this.form = this._formBuilder.group({
      de: [null],
      ate: [null],
    });
  }

  async updateUtilizacao() {
    this.flFilter = !this.flFilter;
    this.buscaitens()
    this.flFilter = !this.flFilter;
  }

  atualizaItens() {
    //this.ds_prest.reset();
    this.buscaitens()
  }

  buscaitens(){
    const form = this.form.getRawValue();
    const filtro = { dt_inicio: moment(form.de).format('dd/MM/yyyy'), dt_fim: moment(form.ate).format('dd/MM/yyyy') };
    this._utilizacaoService.getPortalUtilizacao(filtro).subscribe((resolve: any) => {
      this.rows = resolve;
      this.filteredRows.next(resolve);
    });
  }

  filterPrestador(index, ds_prest: string) {
    return this.rows[index].lista.filter(item => item.ds_prest.toLowerCase()
      .indexOf(ds_prest.toLowerCase()) !== -1 && Number(item.ds_tipo_despesa) === index + 1);
  }

  public filter(index) {
    if (this.ds_prest.value !== "") {
      this.filteredRows.value[index].lista = this.filterPrestador(index, this.ds_prest.value);
    }
  }

  public procurar() {    
    this.updateUtilizacao();
  }

}
