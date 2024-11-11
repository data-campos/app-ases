import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';
      
const routes: Routes = [
  { path: 'auth', loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule) },
  { path: 'inicio', loadChildren: () => import('./pages/inicio/inicio.module').then(m => m.InicioPageModule), canActivate: [AuthGuard] },
  { path: 'home', loadChildren: () => import('./pages/home/home.module').then(m => m.HomePageModule), canActivate: [AuthGuard] },
  { path: 'utilizacao', loadChildren: () => import('./pages/utilizacao/utilizacao.module').then(m => m.UtilizacaoPageModule), canActivate: [AuthGuard] },
  { path: 'carencia-cpt', loadChildren: () => import('./pages/carencia-cpt/carencia-cpt.module').then(m => m.CarenciaCptPageModule), canActivate: [AuthGuard] },
  { path: 'alteracao', loadChildren: () => import('./pages/alteracao/alteracao.module').then(m => m.AlteracaoPageModule), canActivate: [AuthGuard] },
  { path: 'rescisao-contrato', loadChildren: () => import('./pages/rescisao-contrato/rescisao-contrato.module').then(m => m.RescisaoContratoPageModule), canActivate: [AuthGuard] },
  { path: 'comunicado', loadChildren: () => import('./pages/comunicado/comunicado.module').then(m => m.ComunicadoPageModule), canActivate: [AuthGuard] },
  { path: 'solicitacao-segunda-via', loadChildren: () => import('./pages/solicitacao-segunda-via/solicitacao-segunda-via.module').then(m => m.SolicitacaoSegundaViaPageModule), canActivate: [AuthGuard] },
  { path: 'guia-medico', loadChildren: () => import('./pages/guia-medico/guia-medico.module').then(m => m.GuiaMedicoPageModule), canActivate: [AuthGuard] },
  { path: 'demonstrativo', loadChildren: () => import('./pages/demonstrativo/demonstrativo.module').then(m => m.DemonstrativoPageModule), canActivate: [AuthGuard] },
  { path: 'carteira-virtual', loadChildren: () => import('./pages/carteira-virtual/carteira-virtual.module').then(m => m.CarteiraVirtualPageModule), canActivate: [AuthGuard] },
  { path: 'extrato-ir', loadChildren: () => import('./pages/extrato-ir/extrato-ir.module').then(m => m.ExtratoIrPageModule), canActivate: [AuthGuard] },
  { path: 'autorizacao', loadChildren: () => import('./pages/autorizacao/autorizacao.module').then(m => m.AutorizacaoModule), canActivate: [AuthGuard] },
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
