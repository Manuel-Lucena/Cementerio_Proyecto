import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MapaCementerio } from './mapa-cementerio';

describe('MapaCementerio', () => {
  let component: MapaCementerio;
  let fixture: ComponentFixture<MapaCementerio>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MapaCementerio]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MapaCementerio);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
