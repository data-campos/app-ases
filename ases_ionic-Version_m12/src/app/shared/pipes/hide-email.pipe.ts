import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'hideEmail'
})
export class HideEmailPipe implements PipeTransform {
  transform(email: String) : string{
    const emailQuebrado = email.split('@');
    const qtCaracterEsconde = emailQuebrado[0].length%2 == 0 ? emailQuebrado[0].length / 2 : emailQuebrado[0].length / 2 + 1;

    const dsUsuarioEmail = emailQuebrado[0].substring(0, qtCaracterEsconde)
    const emailFinal = `${dsUsuarioEmail}${"*".repeat(qtCaracterEsconde)}@${emailQuebrado[1]}` 

    return emailFinal;  
  }               
}
