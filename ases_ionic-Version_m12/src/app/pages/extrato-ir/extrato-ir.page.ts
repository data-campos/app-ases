import { FormControl } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { ExtratoIrService } from 'src/app/services/extrato-ir.service';
import { Platform, ToastController } from '@ionic/angular';
import { File } from '@ionic-native/file/ngx';
import { FileTransfer } from '@ionic-native/file-transfer/ngx';
import { DocumentViewerOptions } from '@ionic-native/document-viewer/ngx';
import { FileOpener } from '@ionic-native/file-opener/ngx';

@Component({
  selector: 'app-extrato-ir',
  templateUrl: './extrato-ir.page.html',
  styleUrls: ['./extrato-ir.page.scss'],
})
export class ExtratoIrPage implements OnInit {

  public list: any[] = [];
  public ano = new FormControl("")
  public autenticando = false;
  public extrato_pdf = "";
  public textoAno = "";

  constructor(
    private _extratoIrService: ExtratoIrService,
    private platform: Platform,
    private file: File,
    private transfer: FileTransfer,
    private toast: ToastController,
    private fileOpener: FileOpener
  ) {}

  ngOnInit() {
    this._extratoIrService.getImpostoAnosLista().subscribe((resolve: any) => {
      this.list = resolve;
      this.ano.setValue(this.list[0].ds_ano);
    });
  }

  gerarDemonstrativo() {
    
    var options: DocumentViewerOptions = {
      title: 'Extrato de Imposto de Renda - Plano Ases',
      documentView:{closeLabel:''},
      navigationView:{closeLabel:''},
      email:{enabled:true},
      print:{enabled:true},
      openWith:{enabled:true},
      bookmarks:{enabled:false},
      search:{enabled:false},
      autoClose:{onPause:false}
    }    

    this.autenticando = true;
    this._extratoIrService.getDemonstrativo(this.ano.value).toPromise().then(async (resolve: any) => {
      this.extrato_pdf = resolve.ds_retorno;

      let path = null;
      const sucesso = await this.toast.create({ message: `Download demonstrativo PDF concluído com sucesso.`, duration: 3000 });

      if (this.platform.is('ios')) {
        path = this.file.documentsDirectory;
      } else {
        path = this.file.externalDataDirectory;
      }

      const transfer = this.transfer.create();
      const uri = `http://186.237.52.102:8080/storage/${this.extrato_pdf}`;

      try {
        transfer.download(uri, path + 'demonstrativo.pdf').then(async entry => {
          
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
    });
  }

}
