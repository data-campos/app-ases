import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";
import {Utils} from "./utils";

export class CustomValidators {
    public static compareFieldMatch(firstField: string, secondField: string): ValidatorFn {
        return (control: AbstractControl): ValidationErrors | null => {
            const first = control.get(firstField);
            const second = control.get(secondField);

            if (!first) return null;
            if (!second) return null;

            if (first.value.trim() !== second.value.trim()) {
                return {compareFieldMatch: true};
            }

            return null;
        };
    }

    public static fullfilPasswordRequirement(): ValidatorFn {
        return (control: AbstractControl): ValidationErrors | null => {
            if (
                !Utils.hasLetter(control.value) &&
                !Utils.hasNumber(control.value) &&
                control.value.length >= 8
            ) {
                return {passwordRequirements: true};
            }

            return null;
        };
    }
}
