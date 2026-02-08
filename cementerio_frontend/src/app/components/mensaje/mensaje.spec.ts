import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MensajesModalComponent } from './mensaje';

describe('Mensaje', () => {
  let component: MensajesModalComponent;
  let fixture: ComponentFixture<MensajesModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MensajesModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MensajesModalComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
