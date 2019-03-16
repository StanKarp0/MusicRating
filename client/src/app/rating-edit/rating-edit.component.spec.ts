import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RatingEditComponent } from './rating-edit.component';

describe('RatingEditComponent', () => {
  let component: RatingEditComponent;
  let fixture: ComponentFixture<RatingEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RatingEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RatingEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
