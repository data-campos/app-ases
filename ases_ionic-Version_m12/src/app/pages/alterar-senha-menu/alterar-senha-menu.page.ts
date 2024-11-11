import { AuthService } from 'src/app/services/auth.service';
import { Component, OnInit } from '@angular/core';
import { ModalController, ToastController } from '@ionic/angular';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-alterar-senha-menu',
  templateUrl: './alterar-senha-menu.page.html',
  styleUrls: ['./alterar-senha-menu.page.scss'],
})
export class AlterarSenhaMenu implements OnInit {

  public form: FormGroup;
  public autenticando = false;

  constructor(
    private _modalController: ModalController,
    private _formBuilder: FormBuilder,
    private _authService: AuthService,
    private _toastController: ToastController
  ) { }

  ngOnInit() {
    this.form = this._formBuilder.group({
      dsSenhaAtual: ['', Validators.required],
      dsSenhaNova: ['', [Validators.required, Validators.minLength(8)]],
      dsSenhaConfirmacao: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  public close(): void {
    this._modalController.dismiss();
  }

  public getError(field: string): boolean {
    return (
      this.form.controls[field].invalid && (
        this.form.controls[field].dirty || 
        this.form.controls[field].touched
      ) && !!this.form.controls[field].errors
    );
  }

  public async alterarSenha(): Promise<void> {
    const [user] = await this._authService.getUsersLogged();

    this.autenticando = true;

    try {
      const response = await this._authService.alterarSenha({
        dsLogin: user.dsLogin,
        ...this.form.getRawValue()
      }).toPromise();

      if (response.status && response.status !== 200) return;

      const toast = await this._toastController.create({
        message: response.body.message,
        duration: 3000
      });

      toast.present();
    } catch (e) {
      console.log(e.message);
    }
    
    this.autenticando = false;
  }
}
