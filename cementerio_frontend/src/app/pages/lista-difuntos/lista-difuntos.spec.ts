import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaDifuntos } from './lista-difuntos';

describe('ListaDifuntos', () => {
  let component: ListaDifuntos;
  let fixture: ComponentFixture<ListaDifuntos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaDifuntos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaDifuntos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
