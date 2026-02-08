import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormCementerio } from './form-cementerio';

describe('FormCementerio', () => {
  let component: FormCementerio;
  let fixture: ComponentFixture<FormCementerio>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormCementerio]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormCementerio);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
