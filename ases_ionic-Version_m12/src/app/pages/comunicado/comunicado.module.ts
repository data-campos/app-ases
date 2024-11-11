import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { ComunicadoPage } from './comunicado.page';
import { RouterModule } from '@angular/router';
import { MatExpansionModule } from '@angular/material/expansion';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    MatExpansionModule,
    RouterModule.forChild([
      {
        path: '',
        component: ComunicadoPage
      }
    ])
  ],
  declarations: [ComunicadoPage]
})
export class ComunicadoPageModule { }
