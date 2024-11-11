import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { RescisaoContratoConsultaPage } from './rescisao-contrato-consulta.page';

describe('RescisaoContratoConsultaPage', () => {
  let component: RescisaoContratoConsultaPage;
  let fixture: ComponentFixture<RescisaoContratoConsultaPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RescisaoContratoConsultaPage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(RescisaoContratoConsultaPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
