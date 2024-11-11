import { ConsultaSolicitacaoPage } from './consulta-solicitacao.page';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RouterModule } from '@angular/router';
import { BrMaskerModule } from 'br-mask';
import { MatExpansionModule } from '@angular/material/expansion';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        IonicModule,
        BrMaskerModule,
        MatExpansionModule,
        RouterModule.forChild([
            {
                path: 'consulta-solicitacao',
                component: ConsultaSolicitacaoPage
            }
        ])
    ],
    declarations: [ConsultaSolicitacaoPage]
})
export class ConsultaSolicitacaoPageModule { }
