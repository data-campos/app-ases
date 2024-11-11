import { MenuController, ToastController, NavController, AlertController, ModalController } from '@ionic/angular';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, Form } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { GuiaMedicoPage } from '../../pages/guia-medico/guia-medico.page'
import { RecuperarSenhaComponent } from '../recuperar-senha/recuperar-senha.component';
import { CadastrarUsuarioComponent } from '../cadastrar-usuario/cadastrar-usuario.component';
import { UpdateService } from 'src/app/services/update.service';
import { Market } from '@ionic-native/market/ngx';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

  public autenticando = false;
  public form: FormGroup;
  public users: any[] = [];
  public exibirSenha: boolean = false;
  public tipoCampoSenha: string = "password"
  public abrindoGuiaMedico: boolean = false;

  constructor(
    private _formBuilder: FormBuilder,
    private _authService: AuthService,
    private _navController: NavController,
    private _menuController: MenuController,
    private _toastController: ToastController,
    private _alertController: AlertController,
    private _modalController: ModalController,
    private _updateService: UpdateService,
    private _market: Market,
  ) { }

  async ngOnInit() {
    this._menuController.enable(false);
    this.form = this._formBuilder.group({
      ds_login: ['', Validators.required],
      ds_senha: ['', Validators.required],
      fl_salvar: [false]
    });

    await this._authService.editUserLogged([]);

    this._authService.getUserRemember().then((users: any) => {
      if (users.length > 0) { 
        this.users = users;
        this.form.patchValue(users[0]); 
      }
    });

    this.checkVersionMismatch();
  }

  public close() {
    this._navController.navigateBack(['home']);
  }

  public async login(event: any = null): Promise<void> {
    this.autenticando = true;
    console.log('entrou')
    const loginRapido = event != null
    const formValue = loginRapido ? event : this.form.getRawValue();

    try {
      const userLogged = await this._authService.login(formValue, loginRapido).toPromise();

      if (formValue.fl_salvar && !loginRapido && userLogged){
        await this.GravacaoListaUsuario(userLogged);
      }
      await this._menuController.enable(true);
      await this._authService._router.navigate(['inicio']);
    } catch (error) {
      const toast = await this._toastController.create({
        message: 'erro: ' + error,
        duration: 5000
      });
      await toast.present();
    } finally {
      this.autenticando = false;
    }
  }
  

  removerUserRemember(user: any) {
    const index = this.users.findIndex(u => u.dsLogin === user.dsLogin);
    this.users.splice(index, 1);
    this._authService.setUserRemember(this.users);
  }

  async alterarSenha() {
    const alert = await this._alertController.create({
      header: 'Alterar senha',
      message: 'Deseja realmente alterar sua senha?',
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
          handler: () => this.modalRecuperarSenha(),
        }
      ]
    });

    await alert.present();
  }

  async modalRecuperarSenha() {
    const modal = await this._modalController.create({
      component: RecuperarSenhaComponent,
    });

    return await modal.present();
  }

  async modalCadastrarConta() {
    const modal = await this._modalController.create({      
      component: CadastrarUsuarioComponent,
    });
    
    return await modal.present();
  }

  async GravacaoListaUsuario(userLogged){
    const indiceUserRepetido = this.nrIndiceUsuarioRepetido(userLogged)
    if (indiceUserRepetido > -1) {
      this.users[indiceUserRepetido] = userLogged //substituir
    } else{
      this.users.push(userLogged)
      await this._authService.setUserRemember(this.users) //gravar um novo usuário
    }
  }

  nrIndiceUsuarioRepetido(userLogged): number{    
    let result = -1;
    this.users.map((user, index) => {
      if ((user.dsLogin === userLogged.dsLogin) && result == -1) {
        result = index;
      }
    })
    return result;    
  }
  
  async abrirGuiaMedico() {
    this.abrindoGuiaMedico = true;
    const modal = await this._modalController.create({      
      component: GuiaMedicoPage,      
      componentProps: { "isModal": true },
    });

    modal.onDidDismiss().then(() => {this.abrindoGuiaMedico = false;})

    return await modal.present();
    
  }

  async checkVersionMismatch() {
    const isMismatched = await this._updateService.hasNewVersion();

    if (!isMismatched) return;

    const modal = await this._alertController.create({
      backdropDismiss: false,
      header: 'Sua versão do aplicativo está desatualizada.',
      message: 'Por favor, atualize o aplicativo para continuar utilizando.',
      buttons: [
        {
          text: 'Atualizar',
          role: 'cancel',
          handler: async () => {
            const appId = await this._updateService.getAppId();
            this._market.open(appId);
            navigator['app'].exitApp();
          }
        },
        {
          text: 'Sair',
          role: 'cancel',
          handler: () => {
            navigator['app'].exitApp();
          }
        }
      ],
    });

    return await modal.present();
  }
}
