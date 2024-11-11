import { AlertController, ModalController, NavParams, Platform } from '@ionic/angular';
import { FormControl } from '@angular/forms';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { GuiaMedicoService } from 'src/app/services/guia-medico.service';
import { Geolocation } from '@ionic-native/geolocation/ngx';
import { CallNumber } from '@ionic-native/call-number/ngx';
import { InfoMapaComponent } from './info-mapa/info-mapa.component';
import { Utils } from 'src/app/shared/utils'
import { BehaviorSubject } from 'rxjs';
import {
    SpeechRecognition,
    SpeechRecognitionListeningOptions,
    SpeechRecognitionListeningOptionsAndroid, SpeechRecognitionListeningOptionsIOS
} from "@ionic-native/speech-recognition/ngx";
import { first } from "rxjs/operators";

declare var google;

interface EspecialidadesDTO {
    nm_especialidade: string, 
    cd_especialidade: number
}

@Component({
    selector: 'app-guia-medico',
    templateUrl: './guia-medico.page.html',
    styleUrls: ['./guia-medico.page.scss'],
    providers: [CallNumber]
})
export class GuiaMedicoPage implements OnInit {
    public especialidade = new FormControl(0);
    public nm_prestador = new FormControl('');
    public nr_seq_tipo_guia = new FormControl('');

    public latAtual;
    public longAtual;
    public distancia;
    public searchMade = false;

    public itemMarker: any;
    public isModal = false;
    public abriuMapa = false;

    public especialidades: EspecialidadesDTO[] = [];
    public cordenadas: any[] = [];
    public autenticando = false;
    public isGravando = new BehaviorSubject(false);

    constructor(private _guiaMedicoService: GuiaMedicoService,
                private _navParams: NavParams,
                private _geolocation: Geolocation,
                private _modalController: ModalController,
                private _callNumber: CallNumber,
                private _alertController: AlertController,
                private platform: Platform,
                private _changeDetector: ChangeDetectorRef,
                private _speechRecognition: SpeechRecognition
    ) {}

    async ngOnInit() {        
        if (this._navParams.get('isModal')) {
            this.isModal = await this._navParams.get('isModal');
        }

        this.getEspecialidadesELocalizacao();
    }

    public filtrarGuia() {
        this.searchMade = true;
        this.autenticando = true;
        const dados = {
            nr_latitude: this.latAtual,
            nr_longitude: this.longAtual,
            qt_quilometros: this.distancia,
            cd_especialidade: this.especialidade.value,
            nm_prestador: this.nm_prestador.value,
            nr_seq_tipo_guia: this.nr_seq_tipo_guia.value
        };

        this._guiaMedicoService.filtrarGuia(dados).toPromise().then(async (resolve: any) => {
            this.cordenadas = resolve;
            this.autenticando = false;
        });
    }

    async getLocalizacao() {
        try {
            var options = {
                enableHighAccuracy: true,
                timeout: 5000,
                maximumAge: 0
            };
    
            const result = await this._geolocation.getCurrentPosition(options)
    
            this.latAtual = result.coords.latitude;
            this.longAtual = result.coords.longitude;
            this.distancia = 1000;
        } catch (e) {
            // Caso não funcione a geolocalização, retorna a latitude e longitude
            // da sede do Plano ASES (R. Conselheiro Otaviano, 130 - Centro, Campos dos Goytacazes - RJ, 28010-140)
            this.latAtual = -21.764012
            this.longAtual = -41.324999
            this.distancia = 99999;
        }
    }

    public close(): void {
        this._modalController.dismiss();
    }

    async abrirMapa(local) {
        if (!this.abriuMapa) {
            this.abriuMapa = true;
            const modal = await this._modalController.create({
                component: InfoMapaComponent,
                componentProps: {'local': local, 'latUser': this.latAtual, 'lngUser': this.longAtual},
            });

            modal.onDidDismiss().then(() => this.abriuMapa = false);

            return await modal.present();
        } else {
            return;
        }
    }

    private delay(ms: number) {
        return new Promise(res => setTimeout(res, ms));
    };

    private isIos(){
        return this.platform.is('ios');
    }

    async recordVoice(){
        this.isGravando.next(true);

        try {
            let value = '';

            const speechOptionsIOS: SpeechRecognitionListeningOptionsIOS = {};
            const speechOptionsAndroid: SpeechRecognitionListeningOptionsAndroid = {
                showPopup: false,
            };
            
            const speechOptions: SpeechRecognitionListeningOptions = {
                language: 'pt-BR',
                ...(this.platform.is('ios') && speechOptionsIOS),
                ...(this.platform.is('android') && speechOptionsAndroid)
            };
            
            const bool = await this._speechRecognition.hasPermission();
            if (!bool) await this._speechRecognition.requestPermission();

            if(this.isIos()){
                this._speechRecognition.startListening(speechOptions).pipe(first()).subscribe(speech => {
                    value = speech[0];
                });
            } else {
                this._speechRecognition.startListening(speechOptions).subscribe(speech => {
                    value = speech[0];
                    this._changeDetector.detectChanges()
                })
            }

            await this.delay(4000);
            await this._speechRecognition.stopListening();
            await this.delay(500);

            const IncludesFormula = this.especialidades.filter(especialidade => {
                if (value.length > 0 && especialidade.nm_especialidade.toLowerCase().includes(value.toLowerCase())) {
                    return especialidade
                }
            });

            const LevenshteinFormula = Utils.filterByApproximation(
                this.especialidades,
                'nm_especialidade',
                value
            );

            const uniqueValues = new Set([ ...LevenshteinFormula, ...IncludesFormula ]);

            const matches: EspecialidadesDTO[] = Array.from(uniqueValues).sort((a, b) => {
                const distA = a.nm_especialidade && Utils.similarityPercent(a.nm_especialidade, value);
                const distB = b.nm_especialidade && Utils.similarityPercent(b.nm_especialidade, value);

                return distB - distA;
            });

            if (!!value && matches.length) {
                this.especialidade.setValue(matches[0].cd_especialidade)
            }
        } catch (e) {
            await this._speechRecognition.stopListening();

            const alert = await this._alertController.create({
                cssClass: 'alertTelefone',
                header: 'Aviso',
                message: `Ocorreu um erro: ${JSON.stringify(e.message)}.`,
                buttons: [{
                    text: 'Fechar',
                    role: 'cancel',
                }]
            });

            await alert.present();
        } finally {
            this.isGravando.next(false);
        }
    }

    async ligar(numero: string) {
        if (numero.trim() != '') {
            if (this.platform.is('android')) {
                const alert = await this._alertController.create({
                        cssClass: 'alertTelefone',
                        header: 'Confirmar',
                        message: 'Deseja realmente ligar para o número ' + numero + '?',
                        buttons: [
                            {
                                text: 'Cancelar',
                                role: 'cancel',
                            }, {
                                text: `Ligar`,
                                handler: () => {
                                    this._callNumber.callNumber(numero, true)
                                        .then(res => {
                                            return;
                                        })
                                        .catch(err => console.log('Ocorreu um erro ao telefonar (' + err + ')'));
                                }
                            }
                        ]
                    }
                );

                await alert.present();
            } else {
                this._callNumber.callNumber(numero, true)
                    .then(res => {
                        return;
                    })
                    .catch(err => console.log('Ocorreu um erro ao telefonar (' + err + ')'));
            }
        } else {
            const alert = await this._alertController.create({
                cssClass: 'alertTelefone',
                header: 'Aviso',
                message: 'O estabelecimento selecionado não possuí um número cadastrado no sistema',
                buttons: [
                    {
                        text: 'Fechar',
                        role: 'cancel',
                    }]
            });

            await alert.present();
        }
    }

    private getEspecialidadesELocalizacao(){
        this.autenticando = true;
        this._guiaMedicoService.getEspecialidade().subscribe(async (resolve: any) => {
            this.especialidades = resolve;
            await this.getLocalizacao();
            this.autenticando = false;
        });
    }

    public getCoberturaPlano(planos: string): string[] {
        let array = []
        if(planos.includes('1')) array.push('ASES Junior') 
        if(planos.includes('2')) array.push('ASES Convencional')

        return array;
    }
}
