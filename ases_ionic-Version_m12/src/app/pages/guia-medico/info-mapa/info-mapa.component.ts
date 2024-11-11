import { ModalController, NavParams } from '@ionic/angular';
import { Component, Input, OnInit, ViewChild, ElementRef } from '@angular/core';
import { GoogleMapOptions, GoogleMaps, GoogleMapsAnimation } from '@ionic-native/google-maps';
import { Geolocation } from '@ionic-native/geolocation/ngx';

declare var google;

@Component({
  selector: 'app-info-mapa',
  templateUrl: './info-mapa.component.html',
  styleUrls: ['./info-mapa.component.scss'],
})

export class InfoMapaComponent implements OnInit {

  @ViewChild('map', { static: false }) mapElement: ElementRef;
  map;

  public lat: string;
  public lng: string;
  public local: any = {};
  public latUser: string;
  public lngUser: string;
  public myLocation;
    
  public myLat: string; //Localização atual
  public myLng: string;
  
  constructor(private _modalController: ModalController,
              private _navParams: NavParams,
              private _geolocation: Geolocation) {}
  
  ngOnInit() {
    this.local = this._navParams.get('local')
    
    this.latUser = this._navParams.get('latUser')
    this.lngUser = this._navParams.get('lngUser')
    
    this.loadMap();

    var options = {
      enableHighAccuracy: true,
      timeout: 5000,
      maximumAge: 0
    };

    this._geolocation.getCurrentPosition(options).then((result) => {
      this.myLocation = new google.maps.LatLng(result.coords.latitude, result.coords.longitude);
    });
  }

  close(): void {
    this._modalController.dismiss();
  }
  
  public async loadMap() {
    let textoFlag = ""
    const directionsDisplay = new google.maps.DirectionsRenderer();
    const LatLong = new google.maps.LatLng(this.local.nr_latitude, this.local.nr_longitude);
    const image = {
      url: "https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png",
      scaledSize: new google.maps.Size(22, 34),
      origin: new google.maps.Point(0, 0),
      anchor: new google.maps.Point(0, 15),
    };

    const shape = {
      coords: [1, 1, 1, 20, 18, 20, 18, 1],
      type: 'poly'
    };

    if (!this.map){
      this.map = new google.maps.Map(document.getElementById('map'), {
              zoom: 13, center: LatLong
      });
    }

    directionsDisplay.setMap(this.map);
    directionsDisplay.setPanel(this.map);

    textoFlag = await this.montarTexto(this.local)
    const marker = new google.maps.Marker({
      position: LatLong,
      icon: image,
      shape: shape,
      title: textoFlag
    })
          
    await marker.setMap(this.map);

    this.addInfoWindowToMarker(marker);
   }

    addInfoWindowToMarker(marker) {        
      var infoWindowContent = '<div id="content"><h6 id="firstHeading" class="firstHeading">' + marker.title + '</h6></div>'; 
      var infoWindow = new google.maps.InfoWindow({
        content: infoWindowContent
      });
      marker.addListener('click', () => {
        infoWindow.open(this.map, marker);
        
      });
    }

   async montarTexto(mapInfo: any){
    const ds_endereco_local = mapInfo.ds_local
    
    let textoRetorno = ""
    
    if (mapInfo.nm_prestador || mapInfo.ds_contato){
      textoRetorno += `<h4 style="color:black; font-weight: bold; font-size: 18px">Informações do estabelecimento</h4>`
     
      if (mapInfo.nm_prestador){
        textoRetorno += `<h6 style="color:black;">${mapInfo.nm_prestador}</h6>`
      }
      if (mapInfo.ds_contato){
        textoRetorno += `<h6 style="color:black;">${mapInfo.ds_contato}</h6>`
      }
    }

    textoRetorno += `<h4 style="color:black; font-weight: bold">Localização</h4>
                     <span style = "color: black">${ds_endereco_local}</span> 
                     <br>`
    return textoRetorno;
  }

}
