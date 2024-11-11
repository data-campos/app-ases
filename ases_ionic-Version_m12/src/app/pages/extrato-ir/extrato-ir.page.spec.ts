import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { ExtratoIrPage } from './extrato-ir.page';

describe('ExtratoIrPage', () => {
  let component: ExtratoIrPage;
  let fixture: ComponentFixture<ExtratoIrPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExtratoIrPage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(ExtratoIrPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
