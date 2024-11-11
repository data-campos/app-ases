import { ToastController, NavController } from '@ionic/angular';
import { FormControl } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { RescisaoContratoService } from 'src/app/services/rescisao-contrato.service';

@Component({
  selector: 'app-rescisao-contrato',
  templateUrl: './rescisao-contrato.page.html',
  styleUrls: ['./rescisao-contrato.page.scss'],
})
export class RescisaoContratoPage implements OnInit {

  public fl_li = new FormControl(false);
  public autenticando = false;

  constructor(
    private _rescisaoContratoService: RescisaoContratoService,
    private _toastController: ToastController,
    private _navController: NavController
  ) { }

  ngOnInit() {
  }

  solicitaRescisao() {
    this.autenticando = true;
    this._rescisaoContratoService.solicitarRescisao().toPromise().then(async () => {
      const toast = await this._toastController.create({
        message: 'Rescisão solicitada com sucesso.',
        duration: 3000
      });
      toast.present();
      this.autenticando = false;
    });
  }

  consultaRescisao() {
    const url = `rescisao-contrato/rescisao-contrato-consulta`;
    this._navController.navigateForward(url);
  }

}
