import { Injectable } from '@angular/core';
import { Platform } from '@ionic/angular';
import { SpeechRecognition, SpeechRecognitionListeningOptions } from '@ionic-native/speech-recognition/ngx'
import { BehaviorSubject } from 'rxjs';
import { first } from 'rxjs/operators'
@Injectable()
export class ReconhecimentoVozService {
  public isRecording = new BehaviorSubject(false);

  private options: SpeechRecognitionListeningOptions = { language: 'pt-BR' };

  constructor(
    private _platform: Platform,
    private _speechRecognition: SpeechRecognition
  ){}

  private async getPermissions(){
    const hasPermission = await this._speechRecognition.hasPermission();
    if (!hasPermission) await this._speechRecognition.requestPermission();
  }

  private isIos(){
    return this._platform.is('ios');
  }

  private delay(ms: number) {
    return new Promise(res => setTimeout(res, ms));
  };

  public async stopMicrophoneManually(){
    await this._speechRecognition.stopListening();
    this.isRecording.next(false);
  }

  public async enableMicrophone(){
    const match = new BehaviorSubject('');
    
    await this.getPermissions();

    this.isRecording.next(true);

    if(this.isIos()){
      this._speechRecognition.startListening(this.options).pipe(first()).subscribe(matches => {
        match.next(matches[0]);
      });
    } else {
      this._speechRecognition.startListening(this.options).subscribe(matches => {
        match.next(matches[0])
      })
    }

    await this.delay(4000);
    await this._speechRecognition.stopListening();

    await this.delay(500);

    this.isRecording.next(false);

    return match.value;
  }
}
