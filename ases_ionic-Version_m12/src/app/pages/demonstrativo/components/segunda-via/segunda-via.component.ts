import { FileTransfer } from '@ionic-native/file-transfer/ngx';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Platform, ToastController } from '@ionic/angular';
import { File } from '@ionic-native/file/ngx';
import { DemonstrativoService } from 'src/app/services/demonstrativo.service';
import { Clipboard } from '@ionic-native/clipboard/ngx';
import { FormControl, FormGroup, FormBuilder, Validators } from '@angular/forms';
import * as moment from 'moment';
import { FileOpener } from '@ionic-native/file-opener/ngx';
moment.locale('pt-br');

@Component({
  selector: 'app-segunda-via',
  templateUrl: './segunda-via.component.html',
  styleUrls: ['./segunda-via.component.scss'],
})
export class SegundaViaComponent implements OnInit {

  public titulo;
  public boleto;
  public dt_vencimento;
  public dtVencimento;
  public boleto_pdf = "";
  public form: FormGroup;
  public autenticando = false;

  constructor(
    private _activatedRoute: ActivatedRoute,
    private _demonstrativoService: DemonstrativoService,
    private platform: Platform,
    private file: File,
    private fileOpener: FileOpener,
    private transfer: FileTransfer,
    public clipboard: Clipboard,
    private toast: ToastController,
    private _formBuilder: FormBuilder,
  ) {
    this.titulo = this._activatedRoute.snapshot.paramMap.get('nr_titulo');
    this.boleto = this._activatedRoute.snapshot.paramMap.get('nr_boleto');
    this.dt_vencimento = this._activatedRoute.snapshot.paramMap.get('dt_vencimento');
  }

  async ngOnInit() {
    this.buildForm();
    let data = moment(this.dt_vencimento).format('L').split("/");
    this.dtVencimento = `${data[0]}-${data[1]}-${data[2]}`;
    //GERAVA O BOLETO 1
    this.getDespesas(this.dtVencimento); 
  }

  buildForm() {
    this.form = this._formBuilder.group({
      juros: new FormControl({ value: '', disabled: true }),
      multa: new FormControl({ value: '', disabled: true }),
      dt_vencimento: [this.dt_vencimento, Validators.required]
    });
  }

  async downloadPDF() {
    this.autenticando = true;
    let path = null;
    const sucesso = await this.toast.create({ message: `Download boleto PDF concluído com sucesso.`, duration: 3000 });    

    await this.getSegundaVia(this.dtVencimento);
    if (this.platform.is('ios')) {
      path = this.file.documentsDirectory;
    } else {
      path = this.file.externalDataDirectory;
    }
    const transfer = this.transfer.create();
    const uri = `http://186.237.52.102:8080/storage/${this.boleto_pdf}`;

    try {
      transfer.download(uri, path + 'boleto.pdf').then(async entry => {
        sucesso.present();
        let url = entry.toURL();
        this.fileOpener.open(url, 'application/pdf')
          .then(() => console.log("File is opened"))
          .catch(e => console.log("Error opening file", e));
        this.autenticando = false;
      });
    } catch (error) {
      const ERRO = await this.toast.create({ message: `${error}`, duration: 9000 });
      ERRO.present();
    }
  }

  public despesas(event): void {
    let data = moment(event.detail.value).format('L').split("/");
    const dtVencimento = `${data[0]}-${data[1]}-${data[2]}`;
    this.getDespesas(dtVencimento);
  }

  async getSegundaVia(dtVencimento) {
    await this._demonstrativoService.getSegundaVia(this.titulo, dtVencimento).toPromise().then((resolve: any) => {
      if (resolve) {
        this.boleto_pdf = resolve.ds_retorno;
      }
    });
  }

  private getDespesas(dtVencimento): void {
    this._demonstrativoService.getDespesas(this.titulo, dtVencimento).subscribe((resolve: any) => {
      this.form.get('multa').setValue(`R$ ${resolve.vlMulta},00`);
      this.form.get('juros').setValue(`R$ ${resolve.vlJuros},00`);
    });
  }

}
