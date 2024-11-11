import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { CarenciaCptPage } from './carencia-cpt.page';

describe('CarenciaCptPage', () => {
  let component: CarenciaCptPage;
  let fixture: ComponentFixture<CarenciaCptPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CarenciaCptPage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(CarenciaCptPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
