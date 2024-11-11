import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RouterModule } from '@angular/router';
import { BrMaskerModule } from 'br-mask';
import { AlteracaoPage } from './alteracao.page';
import { ConsultaSolicitacaoPageModule } from './components/consulta-solicitacao/consulta-solicitacao.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    IonicModule,
    BrMaskerModule,
    ConsultaSolicitacaoPageModule,
    RouterModule.forChild([
      {
        path: '',
        component: AlteracaoPage
      }
    ])
  ],
  declarations: [AlteracaoPage]
})
export class AlteracaoPageModule { }
