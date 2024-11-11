import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { CarenciaCptPage } from './carencia-cpt.page';
import { RouterModule } from '@angular/router';
import { MatExpansionModule } from '@angular/material/expansion';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild([
      {
        path: '',
        component: CarenciaCptPage
      }
    ]),
    MatExpansionModule,
  ],
  declarations: [CarenciaCptPage]
})
export class CarenciaCptPageModule { }
