import { Injectable } from "@angular/core";
import { AppVersion } from '@ionic-native/app-version/ngx';
import { HTTP } from '@ionic-native/http/ngx';

@Injectable({
    providedIn: 'root'
})
export class UpdateService {
    // private _apiGoogleUrl = 'https://play.google.com/store/apps/details';
    private _apiAppleUrl = 'https://itunes.apple.com/lookup';

    constructor(private http: HTTP, public appVersion: AppVersion) { }

    async hasNewVersion() {
        const appId = await this.appVersion.getPackageName();
        const currentVersion = await this.appVersion.getVersionNumber();
        const url = `${this._apiAppleUrl}?bundleId=${appId}`;
        return new Promise((resolve, reject) => {
            this.http.get(url, {}, {}).then((res: any) => {
                const data = JSON.parse(res.data);
                if (data.resultCount === 1) {
                    const latestVersion = data.results[0].version;
                    if (currentVersion !== latestVersion) {
                        resolve(true);
                    }
                    resolve(false);
                }
            }).catch((err) => {
                console.error('Erro ao verificar a versão do aplicativo na Apple App Store', err);
                reject();
            });
        })
    }

    async getAppId() {
        return await this.appVersion.getPackageName();
    }
}