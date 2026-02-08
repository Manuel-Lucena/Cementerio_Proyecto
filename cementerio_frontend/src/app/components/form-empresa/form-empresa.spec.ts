import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormEmpresa } from './form-empresa';

describe('FormEmpresa', () => {
  let component: FormEmpresa;
  let fixture: ComponentFixture<FormEmpresa>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormEmpresa]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormEmpresa);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
