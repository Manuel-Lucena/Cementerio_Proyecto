import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormContratarServicios } from './form-contratar-servicios';

describe('FormContratarServicios', () => {
  let component: FormContratarServicios;
  let fixture: ComponentFixture<FormContratarServicios>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormContratarServicios]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormContratarServicios);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
