import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ModalController, ToastController } from "@ionic/angular";
import { AuthService } from "src/app/services/auth.service";

@Component({
    templateUrl: "./recuperar-senha.component.html",
    styleUrls: ["./recuperar-senha.component.scss"],
})
export class RecuperarSenhaComponent implements OnInit {
    public form: FormGroup;
    autenticando: boolean = false;

    constructor(
        private _modalController: ModalController,
        private _formBuilder: FormBuilder,
        private _authService: AuthService,
        private _toastController: ToastController
    ) {
        this.form = this._formBuilder.group({
            nrCarteirinha: ["", Validators.required],
            nrCpf: ["", Validators.required],
        });

        this.form.markAllAsTouched();
    }

    ngOnInit() {}

    public close(): void {
        this._modalController.dismiss();
    }

    async handleSubmit() {
        let toast: HTMLIonToastElement;

        const cpf = this.form.get("nrCpf").value.replace(/\D/g, "");
        this.form.get("nrCpf").setValue(cpf);
        this.autenticando = true;

        try {
            const response = await this._authService
                .solicitarAlteracaoSenha(this.form.getRawValue())
                .toPromise();

            if (!this._authService.deuErro) {
                toast = await this._toastController.create({
                    message: response.mensagem,
                    duration: 5000,
                });
                toast.present();
                this.close();
            }
        } catch (error) {
            if (!this._authService.deuErro) {
                toast = await this._toastController.create({
                    message: "Ocorreu um erro desconhecido ( " + error + " )",
                    duration: 3000,
                });
                toast.present();
            }
        }

        this.autenticando = false;
    }
}
