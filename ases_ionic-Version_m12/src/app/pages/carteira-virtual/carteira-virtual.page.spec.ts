import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { CarteiraVirtualPage } from './carteira-virtual.page';

describe('CarteiraVirtualPage', () => {
  let component: CarteiraVirtualPage;
  let fixture: ComponentFixture<CarteiraVirtualPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CarteiraVirtualPage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(CarteiraVirtualPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
