import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RatingsListAllComponent } from './ratings-list-all.component';

describe('RatingsListAllComponent', () => {
  let component: RatingsListAllComponent;
  let fixture: ComponentFixture<RatingsListAllComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RatingsListAllComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RatingsListAllComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
