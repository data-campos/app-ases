import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IonicModule, NavParams } from '@ionic/angular';
import { GuiaMedicoPage } from './guia-medico.page';
import { RouterModule } from '@angular/router';
import { InfoMapaComponent } from './info-mapa/info-mapa.component'
import { CallNumber } from '@ionic-native/call-number/ngx';
import { GoogleMaps } from '@ionic-native/google-maps';
import { ReconhecimentoVozService } from 'src/app/services/reconhecimento-voz.service';

const routes = [
  {
    path: '',
    component: GuiaMedicoPage
  },
  {
    path: '/info-mapa',
    component: InfoMapaComponent
  }
]

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ],
  declarations: [GuiaMedicoPage, InfoMapaComponent],
  providers: [NavParams, CallNumber, GoogleMaps, ReconhecimentoVozService],
  exports: [GuiaMedicoPage]
})
export class GuiaMedicoPageModule { }
