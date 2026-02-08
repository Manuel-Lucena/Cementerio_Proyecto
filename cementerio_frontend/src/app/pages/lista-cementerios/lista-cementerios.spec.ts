import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaCementerios } from './lista-cementerios';

describe('ListaCementerios', () => {
  let component: ListaCementerios;
  let fixture: ComponentFixture<ListaCementerios>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaCementerios]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaCementerios);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
