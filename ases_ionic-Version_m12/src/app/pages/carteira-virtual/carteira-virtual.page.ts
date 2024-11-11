import { FormControl } from '@angular/forms';
import { AuthService } from './../../services/auth.service';
import { Component, OnInit } from '@angular/core';
import { CarteiraVirtualService } from 'src/app/services/carteira-virtual.service';
import { slideFlipOtions } from 'src/app/shared/slide-options';
import { CarenciaCptService } from 'src/app/services/carencia-cpt.service';

@Component({
  selector: 'app-carteira-virtual',
  templateUrl: './carteira-virtual.page.html',
  styleUrls: ['./carteira-virtual.page.scss'],
})
export class CarteiraVirtualPage implements OnInit {

  public lado: string = "";
  public slideOptions = slideFlipOtions;
  public index: number = 0;
  public beneficiarios: any[] = [];
  public carencias: any[] = [];
  public beneficiario = new FormControl('');
  public link = "src/assets/ANS.png";
  public carteira = {
    ds_acomodacao: "",
    nm_empresa: null,
    ds_fator_moderador: "",
    nr_via: "",
    nr_registro_produto: "",
    dt_adesao: "",
    nm_pessoa_fisica: "",
    dt_nascimento: "",
    nr_carteirinha: "",
    dt_validade_carteira: "",
    ds_plano: "",
    nm_segmentacao: "",
    nm_tipo_contratacao: "",
  };

  constructor(
    private _authService: AuthService,
    private _carteiraVirtual: CarteiraVirtualService,
    private _carenciaCptService: CarenciaCptService
  ) { }

  ngOnInit() {
    this._authService.getUsersLogged().then((users: any[]) => {
      if (users.length > 1) {
        this.beneficiarios = users;
      } else {
        this.getCarteira(users[0].dsLogin);
      }
    });

    this._carenciaCptService.getCarencia().subscribe((carencias: any) => {
      this.carencias = carencias;
    });
  }

  selectBeneficiario(event) {
    this.getCarteira(event.detail.value);
  }

  getCarteira(nr_carteira) {
    this._carteiraVirtual.getCarteiraVirtual(nr_carteira).subscribe((resolve: any) => {
      this.carteira = resolve;
    });
  }

  public segmentChanged(event: any) {
    if (event.detail.value === 'frente') {
      this.index = 0;
    } else {
      this.index = 1;
    }
  }

}
