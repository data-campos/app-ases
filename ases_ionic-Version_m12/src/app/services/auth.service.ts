import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, BehaviorSubject} from 'rxjs';
import {map} from 'rxjs/operators';
import {Resolve, RouterStateSnapshot, ActivatedRouteSnapshot, Router} from '@angular/router';
import {ToastController, MenuController, AlertController, NavController} from '@ionic/angular';
import {environment} from 'src/environments/environment';
import {Storage} from '@ionic/storage';
import {promise} from 'protractor';
import {AppVersion} from '@ionic-native/app-version/ngx';

const API_STORAGE_KEY = 'spinopsstoragekey';

@Injectable({
    providedIn: 'root'
})
export class AuthService implements Resolve<any> {

    public userLogged: BehaviorSubject<any> = new BehaviorSubject({
        imagem: null
        , user: null
        , nrCarteirinha: null
        , listaBanners: null
    });
    public token: string;
    public users: BehaviorSubject<any[]> = new BehaviorSubject([]);
    public deuErro: boolean = false;
    public fl_comunicado: BehaviorSubject<boolean> = new BehaviorSubject(null);

    constructor(
        private _http: HttpClient,
        private toastController: ToastController,
        private _storage: Storage,
        private _navController: NavController,
        public _menuController: MenuController,
        public _router: Router,
        public alertController: AlertController,
        public appVersion: AppVersion
    ) {
        if (JSON.parse(localStorage.getItem('currentToken'))) {
            const currentUser = JSON.parse(localStorage.getItem('currentUser'));
            const currentToken = JSON.parse(localStorage.getItem('currentToken'));
            const currentNrCarteirinha = JSON.parse(localStorage.getItem('currentUserLogin'));
            const currentUserImage = JSON.parse(localStorage.getItem('currentUserImage'));
            const currentListaBanner = JSON.parse(localStorage.getItem('currentListaBanner'));

            this.token = currentUser && currentToken;

            this.userLogged.next({
                user: currentUser,
                imagem: currentUserImage,
                carteirinha: currentNrCarteirinha,
                listaBanners: currentListaBanner
            });
        }
    }

    private URL = `${environment.apiUrl}/login`;
    private User_URL = `${environment.apiUrl}/usuario`;

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<any> | Promise<any> | any {

    }

    login(dados: any, acessoRapido: boolean = false): Observable<any> {
        let logon = dados;
        if (acessoRapido) {
            logon = {ds_login: dados.dsLogin, ds_senha: dados.dsSenha, fl_salvar: dados.fl_salvar};
        }
        return this._http.post<any>(`${this.URL}`, logon)
            .pipe(map(async user => {
                const toast = await this.toastController.create({
                    message: 'Login realizado com sucesso.',
                    duration: 2000
                });

                if (user && user.token) {
                    localStorage.setItem('currentToken', JSON.stringify(user.token));
                    localStorage.setItem('currentUser', JSON.stringify(user.nomeUsuario));
                    localStorage.setItem('currentUserLogin', JSON.stringify(user.dsLogin));
                    localStorage.setItem('currentUserIdSegurado', JSON.stringify(user.idSegurado));
                    localStorage.setItem('currentUserImage', JSON.stringify(user.imagem));
                    localStorage.setItem('currentListaBanner', JSON.stringify(user.listaBanners));

                    this.userLogged.next({
                        user: user.nomeUsuario,
                        imagem: user.imagem,
                        carteirinha: user.dsLogin,
                        listaBanners: user.listaBanners
                    });

                    await this.setUserLogged(user);
                    await toast.present();
                }

                return user;
            }));
    }

    logout(): void {
        localStorage.removeItem('currentToken');
        localStorage.removeItem('currentUser');
        localStorage.removeItem('currentUserLogin');
        localStorage.removeItem('currentUserIdSegurado');
        localStorage.removeItem('currentUserImage');
        localStorage.removeItem('currentListaBanner');

        this._navController.navigateBack(['auth/login']).then(() => {
            this._menuController.enable(false);
            this.editUserLogged(null);
        });
    }

    cadastrarConta(dados: any): Observable<any> {
        this.deuErro = false;
        return this._http.post(`${environment.apiUrl}/beneficiario/novo`, dados);
    }

    solicitarAlteracaoSenha(dados: any): Observable<any> {
        this.deuErro = false;
        return this._http.put(`${environment.apiUrl}/beneficiario/solicitar-alteracao-senha`, dados);
    }

    public getUserRemember(): Promise<any> {
        return this._storage.get(`${API_STORAGE_KEY}-lembrarUsuarios`);
    }

    public async setUserRemember(user): Promise<any> {
        return this._storage.set(`${API_STORAGE_KEY}-lembrarUsuarios`, user);
    }

    public getUsersLogged(): Promise<any> {
        return this._storage.get(`${API_STORAGE_KEY}-usuarioAutenticado`);
    }

    public alterarSenha(dados: any): Observable<any> {
        this.deuErro = false;
        return this._http.put(`${environment.apiUrl}/beneficiario/alterar-senha`, dados, {
            observe: 'response'
        });
    }

    public setUserLogged(data: any) {
        if (data) {
            let users = [];
            users.push(data);
            return this._storage.set(`${API_STORAGE_KEY}-usuarioAutenticado`, users);
        }
    }

    public editUserLogged(data: any) {
        if (data && data.length > 0) {
            return this._storage.set(`${API_STORAGE_KEY}-usuarioAutenticado`, data);
        } else {
            return this._storage.set(`${API_STORAGE_KEY}-usuarioAutenticado`, []);
        }
    }


    async open(message, duration = 4000) {
        const toast = await this.toastController.create({message: `${message}`, duration: duration});
        toast.present();
    }

    possuiComunicado() {
        this._http.get(`${environment.apiUrl}/comunicado/possui`).subscribe((resolve: any) => {
            this.fl_comunicado.next(resolve);
        });
    }

    getComunicados() {
        return this._http.get(`${environment.apiUrl}/comunicado/listar`);
    }

    setComunicado(nr_seq) {
        return this._http.post(`${environment.apiUrl}/comunicado/${nr_seq}/ler`, {});
    }

    carregaImagem() {
        const currentUserId = JSON.parse(localStorage.getItem('currentUserId'));
        return this._http.get(`${this.User_URL}/carrega-imagem/${currentUserId}`, {
            responseType: 'blob' as 'json'
        });
    }

    getUserAvatar() {
        return new Promise((resolve, reject) => {

            this.carregaImagem().subscribe((res: any) => {

                const file = new Blob([res], {
                    type: res.type
                });

                if (file.size !== 0) {
                    const reader = new FileReader();
                    reader.readAsDataURL(file);
                    reader.onload = e => resolve(reader.result);
                } else {
                    resolve('assets/avatars/profile.jpg');
                }

            }, reject);
        });
    }

    excluirConta() {
        const ds_login = JSON.parse(localStorage.getItem('currentUserLogin'))

        return this._http.post(`${this.User_URL}/excluir-conta`, {
            ds_login
        });
    }

    async checkMismatchedVersion() {
        // get version from config.xml file
        try {
            const app_version = await this.appVersion.getVersionNumber();
            const backend_version = await this._http.post(`${this.User_URL}/versao-app`, {}).toPromise() as string[];

            return !backend_version.find(version => version === app_version);
        } catch (err) {
            console.log('checkMismatchedVersion', err);
        }
    }
}
