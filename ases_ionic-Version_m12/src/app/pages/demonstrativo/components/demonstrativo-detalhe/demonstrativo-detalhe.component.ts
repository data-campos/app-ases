import { Component, OnInit } from '@angular/core';
import { DemonstrativoService } from 'src/app/services/demonstrativo.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-demonstrativo-detalhe',
  templateUrl: './demonstrativo-detalhe.component.html',
  styleUrls: ['./demonstrativo-detalhe.component.scss'],
})
export class DemonstrativoDetalheComponent implements OnInit {

  public detalhes: any[] = [];

  constructor(
    private _demonstrativoService: DemonstrativoService,
    private _activatedRoute: ActivatedRoute,
  ) { }

  ngOnInit() {
    this._demonstrativoService.getMensalidadeDetalhe(this._activatedRoute.snapshot.paramMap.get('nr_sequencia')).subscribe((resolve: any) => {
      this.detalhes = resolve;
    });
  }

}
