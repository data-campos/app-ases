import { CarenciaCptService } from './../../services/carencia-cpt.service';
import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-carencia-cpt',
  templateUrl: './carencia-cpt.page.html',
  styleUrls: ['./carencia-cpt.page.scss'],
})
export class CarenciaCptPage implements OnInit {

  public carencias: BehaviorSubject<any[]> = new BehaviorSubject([]);
  public cpts: BehaviorSubject<any[]> = new BehaviorSubject([]);

  constructor(
    private _carenciaCptService: CarenciaCptService
  ) { }

  ngOnInit() {
    this.getCarencia();
    this.getCpt();
  }

  getCarencia() {
    this._carenciaCptService.getCarencia().subscribe((carencias: any) => {
      this.carencias.next(carencias);
    });
  }

  getCpt() {
    this._carenciaCptService.getCpt().subscribe((cpts: any) => {
      this.cpts.next(cpts);
    });
  }

}
