import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormConcesion } from './form-concesion';

describe('FormConcesion', () => {
  let component: FormConcesion;
  let fixture: ComponentFixture<FormConcesion>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormConcesion]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormConcesion);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
