import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { RouterModule } from '@angular/router';
import { IonicModule } from '@ionic/angular';
import { BrMaskerModule } from 'br-mask';
import { Market } from '@ionic-native/market/ngx';

import { HideEmailPipe } from '../shared/pipes/hide-email.pipe';

import { GuiaMedicoPageModule } from '../pages/guia-medico/guia-medico.module';

import { GuiaMedicoPage } from '../pages/guia-medico/guia-medico.page';
import { LoginComponent } from './login/login.component';
import { RecuperarSenhaComponent } from './recuperar-senha/recuperar-senha.component';
import { CadastrarUsuarioComponent } from './cadastrar-usuario/cadastrar-usuario.component';

@NgModule({
    imports: [
        CommonModule,
        IonicModule,
        FormsModule,
        ReactiveFormsModule,
        MatSelectModule,
        MatIconModule,
        MatFormFieldModule,
        BrMaskerModule,
        GuiaMedicoPageModule,
        RouterModule.forChild([{
                path: 'login',
                component: LoginComponent
            }])
    ],
    declarations: [
        LoginComponent,
        HideEmailPipe,
        RecuperarSenhaComponent,
        CadastrarUsuarioComponent
    ],
    providers: [Market, HideEmailPipe]
})
export class AuthModule { }
