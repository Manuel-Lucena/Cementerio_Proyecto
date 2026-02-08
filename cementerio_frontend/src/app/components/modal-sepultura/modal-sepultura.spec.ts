import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalSepultura } from './modal-sepultura';

describe('ModalSepultura', () => {
  let component: ModalSepultura;
  let fixture: ComponentFixture<ModalSepultura>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalSepultura]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalSepultura);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
