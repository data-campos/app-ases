import { NavController, Platform, LoadingController } from '@ionic/angular';
import { FormControl } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { DemonstrativoService } from 'src/app/services/demonstrativo.service';

@Component({
  selector: 'app-demonstrativo',
  templateUrl: './demonstrativo.page.html',
  styleUrls: ['./demonstrativo.page.scss'],
})
export class DemonstrativoPage implements OnInit {

  public situacao = new FormControl("a");
  public autenticando = false;
  public mensalidades: any[] = [];

  constructor(
    private _demonstrativoService: DemonstrativoService,
    private _navController: NavController,
    public loadingController: LoadingController
  ) { }

  ngOnInit() {
    this.filtrarDemonstrativo();
  }

  async filtrarDemonstrativo() {
    const loading = await this.loadingController.create({
      message: 'Carregando demonstrativos...',
      duration: 2000
    });
    await loading.present();

    this._demonstrativoService.getMensalidade(this.situacao.value).subscribe((resolve: any) => {
      this.mensalidades = resolve;
      loading.dismiss();
    });
  }

  detalhe(nr_sequencia) {
    const url = `demonstrativo/${nr_sequencia}`;
    this._navController.navigateForward(url);
  }

  segundaVia(nr_titulo, nr_boleto, dt_vencimento) {
    const url = `demonstrativo/segunda-via/${nr_titulo}/${nr_boleto}/${dt_vencimento}`;
    this._navController.navigateForward(url);
  }

}
