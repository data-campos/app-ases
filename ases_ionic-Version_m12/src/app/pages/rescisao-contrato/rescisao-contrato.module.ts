import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RescisaoContratoPage } from './rescisao-contrato.page';
import { RouterModule } from '@angular/router';
import { RescisaoContratoConsultaPageModule } from './components/rescisao-contrato-consulta/rescisao-contrato-consulta.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    IonicModule,
    RescisaoContratoConsultaPageModule,
    RouterModule.forChild([
      {
        path: '',
        component: RescisaoContratoPage
      }
    ])
  ],
  declarations: [RescisaoContratoPage]
})
export class RescisaoContratoPageModule { }
