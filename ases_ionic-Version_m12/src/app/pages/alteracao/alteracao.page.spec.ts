import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { AlteracaoPage } from './alteracao.page';

describe('AlteracaoPage', () => {
  let component: AlteracaoPage;
  let fixture: ComponentFixture<AlteracaoPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlteracaoPage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(AlteracaoPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
