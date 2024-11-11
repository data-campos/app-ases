import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { DemonstrativoPage } from './demonstrativo.page';
import { MatExpansionModule } from '@angular/material/expansion';
import { RouterModule, Routes } from '@angular/router';
import { DemonstrativoDetalheComponent, SegundaViaComponent } from './components';
import { NgxCurrencyModule } from 'ngx-currency';

const routes: Routes = [
  {
    path: '',
    component: DemonstrativoPage
  },
  {
    path: ':nr_sequencia',
    component: DemonstrativoDetalheComponent
  },
  {
    path: 'segunda-via/:nr_titulo/:nr_boleto/:dt_vencimento',
    component: SegundaViaComponent
  }
];

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
    ReactiveFormsModule,
    IonicModule,
    MatExpansionModule,
    NgxCurrencyModule.forRoot(customCurrencyMaskConfig),
    RouterModule.forChild(routes)
  ],
  declarations: [
    DemonstrativoPage,
    DemonstrativoDetalheComponent,
    SegundaViaComponent
  ]
})
export class DemonstrativoPageModule { }
