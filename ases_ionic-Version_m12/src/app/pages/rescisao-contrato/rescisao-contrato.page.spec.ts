import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { RescisaoContratoPage } from './rescisao-contrato.page';

describe('RescisaoContratoPage', () => {
  let component: RescisaoContratoPage;
  let fixture: ComponentFixture<RescisaoContratoPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RescisaoContratoPage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(RescisaoContratoPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
