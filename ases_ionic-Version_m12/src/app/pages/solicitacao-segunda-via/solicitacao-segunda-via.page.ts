import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { SolicitaSegundaViaService } from 'src/app/services/solicita-segunda-via.service';
import { ToastController } from '@ionic/angular';

@Component({
  selector: 'app-solicitacao-segunda-via',
  templateUrl: './solicitacao-segunda-via.page.html',
  styleUrls: ['./solicitacao-segunda-via.page.scss'],
})
export class SolicitacaoSegundaViaPage implements OnInit {

  public motivo = new FormControl("");
  public motivos: any[] = [];
  public solicitacoes: any[] = [];
  public autenticando = false;

  constructor(
    private _segundaViaService: SolicitaSegundaViaService,
    private _toastController: ToastController
  ) { }

  ngOnInit() {
    this.updateVia();
  }

  solicitaVia() {
    this.autenticando = true;
    this._segundaViaService.solicitaVia(this.motivo.value).toPromise().then(async () => {
      const toast = await this._toastController.create({ message: 'Solicitação concluída com sucesso.', duration: 3000 });
      toast.present();
      this.autenticando = false;
      this.updateVia();
    });
  }

  updateVia() {
    this._segundaViaService.getVia().subscribe((resolve: any) => {
      this.motivos = resolve.listaMotivo;
      this.solicitacoes = resolve.listaSolicitacao;
    });
  }

}
