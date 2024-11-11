import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { UtilizacaoPage } from './utilizacao.page';
import { RouterModule } from '@angular/router';
import { MatExpansionModule } from '@angular/material/expansion';
import { NgxCurrencyModule } from 'ngx-currency';

export const customCurrencyMaskConfig = {
  align: "right",
  allowNegative: true,
  allowZero: true,
  decimal: ".",
  precision: 2,
  prefix: "R$ ",
  suffix: "",
  thousands: ",",
  nullable: true
};

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    MatExpansionModule,
    ReactiveFormsModule,
    NgxCurrencyModule.forRoot(customCurrencyMaskConfig),
    RouterModule.forChild([
      {
        path: '',
        component: UtilizacaoPage
      }
    ])
  ],
  declarations: [UtilizacaoPage]
})
export class UtilizacaoPageModule { }
