import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { Platform, MenuController, ModalController } from '@ionic/angular';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';
import { AuthService } from './services/auth.service';
import { BehaviorSubject } from 'rxjs';
import { AlterarSenhaMenu } from './pages/alterar-senha-menu/alterar-senha-menu.page';
import { UpdateService } from './services/update.service';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss']
})
export class AppComponent {

  public userLogged: BehaviorSubject<any> = new BehaviorSubject({});
  public ds_imagem_avatar;
  menuList = [];

  constructor(
    public _authService: AuthService,
    private platform: Platform,
    private splashScreen: SplashScreen,
    private statusBar: StatusBar,
    public _menuController: MenuController,
    private _updateService: UpdateService
  ) {
    this._authService.userLogged.subscribe((user) => {
      if (user) {
        const base64image = 'data:image/jpeg;base64,' + user.imagem;
        this.ds_imagem_avatar = base64image;
        this.userLogged.next(user.user);
      }
    });
    this.initializeApp();
  }

  initializeApp() {
    this.platform.ready().then(() => {
      this.statusBar.styleDefault();
      this.splashScreen.hide();
      this._updateService.hasNewVersion();
    });
  }
}
