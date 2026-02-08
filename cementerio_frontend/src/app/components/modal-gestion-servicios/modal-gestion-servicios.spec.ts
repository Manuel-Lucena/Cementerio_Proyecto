import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalGestionServicios } from './modal-gestion-servicios';

describe('ModalGestionServicios', () => {
  let component: ModalGestionServicios;
  let fixture: ComponentFixture<ModalGestionServicios>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalGestionServicios]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalGestionServicios);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
