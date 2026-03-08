import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisasterDashboard } from './disaster-dashboard';

describe('DisasterDashboard', () => {
  let component: DisasterDashboard;
  let fixture: ComponentFixture<DisasterDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisasterDashboard],
    }).compileComponents();

    fixture = TestBed.createComponent(DisasterDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
