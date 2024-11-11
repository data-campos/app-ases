import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ModalController, ToastController} from "@ionic/angular";
import {AuthService} from "src/app/services/auth.service";
import {Utils} from "src/app/shared/utils";
import {CustomValidators} from "src/app/shared/custom-validators";

@Component({
    templateUrl: "./cadastrar-usuario.component.html",
    styleUrls: ["./cadastrar-usuario.component.scss"],
})
export class CadastrarUsuarioComponent implements OnInit {
    public form: FormGroup;
    public autenticando = false;
    public passwordCondition = [
        {message: "A senha deve conter no minimo 8 caracteres", color: ""},
        {message: "A senha deve conter pelo menos um número", color: ""},
        {message: "A senha deve conter pelo menos uma letra", color: ""},
    ];

    constructor(
        private _modalController: ModalController,
        private _formBuilder: FormBuilder,
        private _authService: AuthService,
        private _toastController: ToastController
    ) {
        this.form = this._formBuilder.group({
            nrCarteirinha: ["", Validators.required],
            dtNascimento: [null, Validators.required],
            nrCpf: ["", Validators.required],
            dsEmail: ["",
                [
                    Validators.email,
                    Validators.required,
                ]
            ],
            dsEmailConfirmacao: ["",
                [
                    Validators.email,
                    Validators.required,
                ]
            ],
            dsSenha: [
                "",
                [
                    Validators.required,
                    Validators.minLength(8),
                    CustomValidators.fullfilPasswordRequirement(),
                ],
            ],
            dsSenhaConfirmacao: [
                "",
                [
                    Validators.required,
                    Validators.minLength(8),
                    CustomValidators.fullfilPasswordRequirement(),
                ],
            ],
        },
            {
            validators: [
                CustomValidators.compareFieldMatch("dsSenha", "dsSenhaConfirmacao"),
                CustomValidators.compareFieldMatch("dsEmail", "dsEmailConfirmacao"),
            ]
        }
        );

        this.form.markAllAsTouched();
    }

    ngOnInit() {
    }

    public async close(): Promise<void> {
        await this._modalController.dismiss();
    }

    checkIfPasswordFulfillConditions(event): void {
        const value = event.target.value;

        const colorCondition = (condition: boolean) => condition ? "#00FFC9" : "orange";

        this.passwordCondition[0].color = colorCondition(value.length >= 8);
        this.passwordCondition[1].color = colorCondition(Utils.hasNumber(value));
        this.passwordCondition[2].color = colorCondition(Utils.hasLetter(value));
    }

    public matchFields(field1: string, field2: string): boolean {
        return (
            this.form.get(field1).value.trim() ==
            this.form.get(field2).value.trim()
        );
    }

    async handleSubmit() {
        let toast: HTMLIonToastElement;

        const cpf = this.form.get("nrCpf").value.replace(/\D/g, "");
        this.form.get("nrCpf").setValue(cpf);

        this.autenticando = true;

        try {
            const response = await this._authService
                .cadastrarConta(this.form.getRawValue())
                .toPromise();

            if (!this._authService.deuErro) {
                toast = await this._toastController.create({
                    message: response.mensagem,
                    duration: 5000,
                });
                await toast.present();

                await this.close();
            }
        } catch (error) {
            if (!this._authService.deuErro) {
                toast = await this._toastController.create({
                    message: "Ocorreu um erro desconhecido ( " + error + " )",
                    duration: 3000,
                });
                await toast.present();
            }
        }

        this.autenticando = false;
    }
}
