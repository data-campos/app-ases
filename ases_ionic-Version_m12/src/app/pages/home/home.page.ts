import { HomeService } from './../../services/home.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import * as moment from 'moment';
moment.locale('pt-br');

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage implements OnInit {

  public ds_imagem_avatar;
  public defaultPhoto: string = '../assets/user.png';
  public form: FormGroup;
  public dados = {
    nm_pessoa_fisica: "",
    nr_carteirinha: "   carregando dados...",
    ds_email: "",
    nm_produto: ""
  };

  constructor(
    private _homeService: HomeService,
    private _formBuilder: FormBuilder,
  ) { }

  ngOnInit() {
    this.buildform();
    this._homeService.getDadosPortal().subscribe((obj: any) => {
      const base64image = 'data:image/jpeg;base64,' + obj.imagem;
      this.ds_imagem_avatar = base64image;
      this.dados = obj.detalhe;
      this.form.patchValue(obj.detalhe);
    });
  }

  buildform() {
    this.form = this._formBuilder.group({
      nm_pessoa_fisica: new FormControl({ value: '', disabled: true }),
      cd_pessoa_fisica: new FormControl({ value: '', disabled: true }),
      ds_email: new FormControl({ value: '', disabled: true }),
      nm_produto: new FormControl({ value: '', disabled: true }),
      dt_nascimento: new FormControl({ value: '', disabled: true }),
      nr_carteirinha: new FormControl({ value: '', disabled: true }),
      nr_contrato: new FormControl({ value: '', disabled: true }),
      nr_controle_interno: new FormControl({ value: '', disabled: true }),
      ds_estipulante: new FormControl({ value: '', disabled: true }),
      nr_ans: new FormControl({ value: '', disabled: true }),
      nr_cartao_nac_sus: new FormControl({ value: '', disabled: true }),
      dt_validade_carteira: new FormControl({ value: '', disabled: true }),
      dt_contratacao: new FormControl({ value: '', disabled: true }),
      dt_inclusao_operadora: new FormControl({ value: '', disabled: true }),
      nm_pagador: new FormControl({ value: '', disabled: true }),
      ds_plano: new FormControl({ value: '', disabled: true }),
      ds_sit_plano: new FormControl({ value: '', disabled: true }),
      nm_segmentacao: new FormControl({ value: '', disabled: true }),
      nm_regulamentacao: new FormControl({ value: '', disabled: true }),
      ds_codigo_anterior: new FormControl({ value: '', disabled: true }),
      nr_ans_plano: new FormControl({ value: '', disabled: true }),
      nm_tipo_contratacao: new FormControl({ value: '', disabled: true }),
      nm_tipo_acomodacao: new FormControl({ value: '', disabled: true }),
      nm_formacao_preco: new FormControl({ value: '', disabled: true }),
      cd_scpa: new FormControl({ value: '', disabled: true }),
      ds_abrangencia: new FormControl({ value: '', disabled: true }),
    });
  }

}
