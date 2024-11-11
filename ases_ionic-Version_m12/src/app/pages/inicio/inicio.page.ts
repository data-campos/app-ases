import { AlertController, MenuController, ModalController, NavController } from '@ionic/angular';
import { AuthService } from 'src/app/services/auth.service';
import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, from } from 'rxjs';
import { menuList } from '../../navigation/navigation';
import { Router } from '@angular/router';
import { AlterarSenhaMenu } from '../alterar-senha-menu/alterar-senha-menu.page'
import { GuiaMedicoPage } from '../guia-medico/guia-medico.page';


@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.page.html',
  styleUrls: ['./inicio.page.scss'],
})
export class InicioPage implements OnInit {

  public menuList = new BehaviorSubject([]);
  public user;
  public ds_imagem_avatar;
  public possuiComunicado: BehaviorSubject<boolean> = new BehaviorSubject(null);
  public listaBanner: any[] = [];
  public qtComunicado;

  constructor(
    private _authService: AuthService,
    public _menuController: MenuController,
    public _navController: NavController,
    public _router: Router,
    public _modalController: ModalController,
    public _alertController: AlertController
  ) { }

  ngOnInit() {
    this._authService.userLogged.subscribe((user: any) => {
      this.user = user;
      this.listaBanner = user.listaBanners;
      const base64image = 'data:image/jpeg;base64,' + user.imagem;
      this.ds_imagem_avatar = base64image;
    });

    this._authService.possuiComunicado();

    this._authService.fl_comunicado.subscribe((resolve: any) => {
      this.possuiComunicado.next(resolve);
    });

    this._authService.getComunicados().subscribe((resolve: any) => {
      this.qtComunicado = resolve.length;
    });

    if (!this.user.user) {
      this._authService.logout();
    }

    this.filterMenuList();
  }

  public async filterMenuList() {
    try {
      const [user] = await this._authService.getUsersLogged();

      const filteredMenuList: Array<any> = menuList.filter((item) => {
        if (user.ieTipoContratacao === 'CE' && user.ieTipoPessoa === 'PJ') {
          switch (item.id) {
            case 'demonstrativo':
            case 'extrato-ir':
              return;
            default:
              return item;
          }
        }
  
        return item;
      });
  
      this.menuList.next(filteredMenuList);
    } catch (e) {
      this.menuList.next(menuList);
    }
  }

  public comunicados(possuiComunicado) {
    if (possuiComunicado) { this._navController.navigateForward('comunicado'); }
  }

  async menuClick(item) {
    switch (item.link){
      case 'alterar-senha':
        return await this.alterarSenha();
      case 'sair':
        return await this.logout();
      case 'excluir-conta':
        return await this.excluirConta();
      default:
        return this._router.navigate([`${item.link}`]);
    }
  }

  async alterarSenha() {
    const modal = await this._modalController.create({
      component: AlterarSenhaMenu,
      componentProps: { value: [] }
    });

    return await modal.present();
  }

  async logout() {
    this._authService.logout();
  }

  async excluirConta() {
    const modal = await this._alertController.create({
      backdropDismiss: false,
      header: 'Excluir conta',
      message: 'Você receberá um e-mail com link para efetuar a exclusão de seu usuário no aplicativo. Uma vez que o processo seja realizado, seu acesso ao aplicativo será perdido. Deseja continuar?',
      buttons: [
        {
          text: 'Não',
          role: 'cancel',
          handler: () => { }
        }, {
          text: 'Sim',
          role: 'confirm',
          handler: async () => {
            await this._authService.excluirConta().toPromise();
            this._modalLogout();
          },
        }
      ],
    });

    return await modal.present();
  }

  async _modalLogout(){
    const modal = await this._alertController.create({
      backdropDismiss: false,
      header: 'Excluir conta',
      message: 'Verifique seu e-mail para efetuar a exclusão de seu usuário no aplicativo. Você será redirecionado para a tela de login.',
      buttons: [
        {
          text: 'Ok',
          role: 'confirm',
          handler: () => {
            this._authService.logout()
          },
        }
      ],
    });

    return await modal.present();
  }

  async verificarPlano() {
    // this._authService.getUsersLogged()
    // TODO: verificar plano e remover caso empresarial
  }

}
