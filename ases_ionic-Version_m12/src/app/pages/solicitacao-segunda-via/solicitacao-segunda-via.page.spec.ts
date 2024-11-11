import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { SolicitacaoSegundaViaPage } from './solicitacao-segunda-via.page';

describe('SolicitacaoSegundaViaPage', () => {
  let component: SolicitacaoSegundaViaPage;
  let fixture: ComponentFixture<SolicitacaoSegundaViaPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SolicitacaoSegundaViaPage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(SolicitacaoSegundaViaPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
