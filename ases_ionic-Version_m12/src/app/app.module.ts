import { NgModule, LOCALE_ID } from '@angular/core';
import { registerLocaleData } from "@angular/common";
import { BrowserModule } from '@angular/platform-browser';
import { RouteReuseStrategy } from '@angular/router';

import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Interceptor } from './loader/interceptor';
import { IonicStorageModule } from '@ionic/storage';
import { MatSelectModule } from '@angular/material/select';

import { BrMaskerModule } from 'br-mask';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AlterarSenhaMenu } from './pages/alterar-senha-menu/alterar-senha-menu.page';
import { Geolocation } from '@ionic-native/geolocation/ngx';
import { File } from '@ionic-native/file/ngx';
import { FileOpener  } from '@ionic-native/file-opener/ngx';
import { Clipboard } from '@ionic-native/clipboard/ngx';
import { FileTransfer } from '@ionic-native/file-transfer/ngx';
import { DocumentViewer } from '@ionic-native/document-viewer/ngx';
import { SpeechRecognition } from '@ionic-native/speech-recognition/ngx'
import localePt from "@angular/common/locales/pt";
import { AppVersion } from '@ionic-native/app-version/ngx';
import { HTTP } from '@ionic-native/http/ngx';

@NgModule({
    declarations: [
        AppComponent,
        AlterarSenhaMenu
    ],
    imports: [
        FormsModule,
        BrowserModule,
        BrMaskerModule,
        MatSelectModule,
        AppRoutingModule,
        HttpClientModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
        IonicStorageModule.forRoot(),
        IonicModule.forRoot({ backButtonText: '' }),
    ],
    providers: [
        StatusBar,
        SplashScreen,
        Geolocation,
        SpeechRecognition,
        AppVersion,
        File,
        FileOpener,
        Clipboard,
        FileTransfer,
        DocumentViewer,
        HTTP,
        { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: Interceptor,
            multi: true
        },
        { provide: LOCALE_ID, useValue: 'pt' },
    ],
    bootstrap: [AppComponent]
}
)
export class AppModule { }

registerLocaleData(localePt, 'pt')
