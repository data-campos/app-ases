import { ToastController, AlertController, NavController } from '@ionic/angular';
import { AlteracaoService } from './../../services/alteracao.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { EstadosService } from 'src/app/services/estados.service';

@Component({
  selector: 'app-alteracao',
  templateUrl: './alteracao.page.html',
  styleUrls: ['./alteracao.page.scss'],
})
export class AlteracaoPage implements OnInit {

  public form: FormGroup;
  public estadoCivil: any[] = [{ ds: 'Solteiro', ie: '1' }, { ds: 'Casado', ie: '2' }, { ds: 'Viúvo', ie: '3' }, { ds: 'Separado', ie: '4' }, { ds: 'Divorciado', ie: '5' }, { ds: 'União estável', ie: '6' }, { ds: 'Outros', ie: '7' }];
  public sexo: any[] = [{ ds: 'Feminino', sg: 'F' }, { ds: 'Masculino', sg: 'M' }, { ds: 'Indeterminado', sg: 'I' }, { ds: 'Diverso', sg: 'D' }];
  public estados: Array<any> = [];
  public cidades: Array<any> = [];
  public autenticando: boolean = false;
  public pesquisaIBGE: any;

  constructor(
    private _alteracaoService: AlteracaoService,
    private _formBuilder: FormBuilder,
    private _estadosService: EstadosService,
    private _toastController: ToastController,
    private _alertController: AlertController,
    private _navController: NavController,
  ) { }

  ngOnInit() {
    this.buildform();

    this._estadosService
      .getCidadeEstados().subscribe((response: any) => {
        this.estados = response.estados;
      });

    this._alteracaoService.getPortalAlteracao().subscribe((resolve: any) => {
      this.form.patchValue(resolve);
    });
  }

  public getCidadesFromEstado(event): void {
    const sigla = event.detail.value;
    const estado = this.estados.find(item => item.sigla == sigla);
    this.cidades = estado.cidades;
  }

  buildform() {
    this.form = this._formBuilder.group({
      dt_nascimento: [null, Validators.required],
      cd_cep: [null, Validators.required],
      ds_bairro: [null, Validators.required],
      nr_cpf: [null, Validators.required],
      nr_telefone: [null, Validators.required],
      sg_estado: [null, Validators.required],
      ds_email: [null, Validators.required],
      nr_telefone_celular: [null, Validators.required],
      nm_pessoa_fisica: [null, Validators.required],
      ds_cidade: [null, Validators.required],
      ds_sexo: [null, Validators.required],
      ds_estado_civil: [null, Validators.required],
      nm_mae: [null],
      nm_pai: [null],
      nr_endereco: [null, Validators.required],
      ds_endereco: [null, Validators.required],
      nr_identidade: [null, Validators.required],
      nr_cartao_nac_sus: [null, Validators.required],
      ie_estado_civil: [null, Validators.required],
      ie_sexo: [null, Validators.required],
      cd_municipio_ibge: [null]
    });
  }

  async solicitaAlteracao() {
    const alert = await this._alertController.create({
      header: 'Alteração',
      message: 'Deseja realmente alterar dados cadastrais?',
      buttons: [
        {
          text: 'Cancelar',
          role: 'cancel',
          cssClass: 'secondary',
          handler: () => {
            console.log('Confirm Cancel');
          }
        }, {
          text: 'Confirmar',
          handler: () => {
            this.autenticando = true;
            this._alteracaoService.solicitarAlteracao(this.form.getRawValue()).toPromise().then(async () => {
              const toast = await this._toastController.create({
                message: 'Alteração solicitada com sucesso.',
                duration: 3000
              });
              toast.present();
              this.autenticando = false;
            });
          }
        }
      ]
    });

    await alert.present();
  }

  async pesquisaCEP() {
    //Nova variável "cep" somente com dígitos.
    let cep = this.form.get('cd_cep').value.replace(/\D/g, '');
    const toast = await this._toastController.create({ message: 'CEP inválido.', duration: 3000 });

    //Verifica se campo cep possui valor informado.
    if (cep != "") {
      this._alteracaoService.getCEP(cep).subscribe((resolve: any) => {
        if (resolve.erro) {
          toast.present();
          this.form.get('ds_endereco').reset();
          this.form.get('ds_bairro').reset();
          this.form.get('sg_estado').reset();
          this.form.get('cd_municipio_ibge').reset();
          this.form.get('nr_endereco').reset();
        } else {
          this.form.get('ds_endereco').setValue(resolve.logradouro);
          this.form.get('ds_bairro').setValue(resolve.bairro);
          this.form.get('sg_estado').setValue(resolve.uf);

          const i = this.cidades.findIndex(c => c === resolve.localidade.toUpperCase());
          this.form.get('ds_cidade').setValue(this.cidades[i]);

          this.form.get('cd_municipio_ibge').setValue(resolve.ibge);
          this.form.get('nr_endereco').reset();
        }
      });
    }
  }

  public consultaSolicitacao(): void {
    const url = `alteracao/consulta-solicitacao`;
    this._navController.navigateForward(url);
  }

}
