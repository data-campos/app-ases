import { Component, OnInit } from '@angular/core';
import { RescisaoContratoService } from 'src/app/services/rescisao-contrato.service';

@Component({
  selector: 'app-rescisao-contrato-consulta',
  templateUrl: './rescisao-contrato-consulta.page.html',
  styleUrls: ['./rescisao-contrato-consulta.page.scss'],
})
export class RescisaoContratoConsultaPage implements OnInit {

  public dados: any[] = [];

  constructor(
    private _rescisaoContratoService: RescisaoContratoService,
  ) { }

  ngOnInit() {
    this._rescisaoContratoService.consultaRescisao().subscribe((resolve: any) => {
      this.dados = resolve;
    });
  }

}
