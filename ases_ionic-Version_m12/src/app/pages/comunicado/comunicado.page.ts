import { AuthService } from 'src/app/services/auth.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-comunicado',
  templateUrl: './comunicado.page.html',
  styleUrls: ['./comunicado.page.scss'],
})

export class ComunicadoPage implements OnInit {

  public comunicados: any[] = [];
  public item;

  constructor(
    private _authService: AuthService
  ) { }

  ngOnInit() {
    this._authService.getComunicados().subscribe((resolve: any) => {
      this.comunicados = resolve;
    });
  }

  public lerComunicado(nr_seq): void {
    this._authService.setComunicado(nr_seq).toPromise().then(() => {
      console.log('lido');
    });
  }

  async voltar() {
    this._authService.possuiComunicado();
  }

}
