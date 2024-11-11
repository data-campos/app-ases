import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { CarteiraVirtualPage } from './carteira-virtual.page';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from 'src/app/guards/auth.guard';
// import { OpcaoCarteiraComponent } from './components';

const routes: Routes = [
  {
    path: '',
    component: CarteiraVirtualPage,
    canActivate: [AuthGuard]
  },
  // {
  //   path: ':lado',
  //   component: CarteiraVirtualPage,
  //   canActivate: [AuthGuard]
  // }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ],
  declarations: [
    CarteiraVirtualPage,
    // OpcaoCarteiraComponent
  ]
})
export class CarteiraVirtualPageModule { }
