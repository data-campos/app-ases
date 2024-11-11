import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { SolicitacaoSegundaViaPage } from './solicitacao-segunda-via.page';
import { RouterModule } from '@angular/router';
import { MatExpansionModule } from '@angular/material/expansion';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ReactiveFormsModule,
    MatExpansionModule,
    RouterModule.forChild([
      {
        path: '',
        component: SolicitacaoSegundaViaPage
      }
    ])
  ],
  declarations: [SolicitacaoSegundaViaPage]
})
export class SolicitacaoSegundaViaPageModule { }
