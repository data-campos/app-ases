import { LoadingController } from '@ionic/angular';
import { Injectable } from '@angular/core';
// import 'rxjs/add/observable/fromPromise';
// import 'rxjs/add/operator/do';
// import 'rxjs/add/operator/catch';
// import 'rxjs/add/observable/throw';
import {
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest,
    HttpHeaders,
    HttpErrorResponse
} from '@angular/common/http';

import { BehaviorSubject, throwError, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable()
export class HTTPStatus {
    private requestInFlight$: BehaviorSubject<boolean>;

    constructor() {
        this.requestInFlight$ = new BehaviorSubject(false);
    }
    setHttpStatus(inFlight: boolean) {
        this.requestInFlight$.next(inFlight);
    }
    getHttpStatus(): Observable<boolean> {
        return this.requestInFlight$.asObservable();
    }
}

@Injectable()
export class Interceptor implements HttpInterceptor {

    constructor(
        private _loadingController: LoadingController,
        private _authService: AuthService,
        private _router: Router
    ) {

    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return this.handleAccess(request, next);
    }

    private handleAccess(request: HttpRequest<any>, next: HttpHandler):
        Observable<HttpEvent<any>> {
        const token = JSON.parse(localStorage.getItem('currentToken'));
        let changedRequest = request;
        // HttpHeader object immutable - copy values
        const headerSettings: { [name: string]: string | string[]; } = {};

        for (const key of request.headers.keys()) {
            headerSettings[key] = request.headers.getAll(key);
        }

        if (token && !(request.url.indexOf("viacep") != -1)) {
            headerSettings['Authorization'] = token;
        }
        //headerSettings['Content-Type'] = 'application/json';
        const newHeader = new HttpHeaders(headerSettings);

        changedRequest = request.clone({
            headers: newHeader
        });

        //console.log('Request', changedRequest);
        return next.handle(changedRequest).pipe(catchError(err => {
            this._authService.deuErro = true;
            let mensagem: string = 'Ocorreu um erro desconhecido ao tentar processar a operação!'
            switch (err.status){
                case 400: if (err instanceof HttpErrorResponse && err.error instanceof Blob && err.error.type === "application/json") {
                            mensagem = "Não existem dados para serem gerados. Erro: " + err.status;
                          } else {
                            mensagem = err.error.message;
                            this._loadingController.dismiss().catch(() => {});
                          }        
                          break;
                
                case 401: if (err.error.message === "Unauthorized"){
                            if (this._authService.userLogged.value.user) {
                               mensagem = 'Sua sessão foi expirada. Por gentileza, logue novamente! Erro: ' + err.status;
                               this._router.navigate(['auth/login']);
                            }else{
                               mensagem = mensagem + ' Erro: ' + err.status;
                            }
                          }
                          break;
                
                case 500: if (err.error.message === "INVALID_CREDENTIALS") {
                               mensagem = 'Usuario ou senha incorretos. Digite novamente para realizar o login'
                             }else{
                               mensagem = mensagem + ' Erro: ' + err.status;   
                             }
                          break;
                default:  mensagem = mensagem + ' (' + err.status + ')' + ' - ' + err.error.message
                          break;            
                }

            if (changedRequest.url.indexOf("/usuario/versao-app") != -1) {
                return;
            }
            this.exibir(mensagem)
            return [];
        }))
    }

    public exibir(msg) {
        this._authService.open(msg);
    }
}
