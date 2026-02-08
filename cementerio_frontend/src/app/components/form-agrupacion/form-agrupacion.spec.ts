import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormAgrupacion } from './form-agrupacion';

describe('FormAgrupacion', () => {
  let component: FormAgrupacion;
  let fixture: ComponentFixture<FormAgrupacion>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormAgrupacion]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormAgrupacion);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
