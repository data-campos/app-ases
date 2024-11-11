import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RescisaoContratoConsultaPage } from './rescisao-contrato-consulta.page';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild([
      {
        path: 'rescisao-contrato-consulta',
        component: RescisaoContratoConsultaPage
      }
    ])
  ],
  declarations: [RescisaoContratoConsultaPage]
})
export class RescisaoContratoConsultaPageModule { }
