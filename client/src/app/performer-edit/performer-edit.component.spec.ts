import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformerEditComponent } from './performer-edit.component';

describe('PerformerEditComponent', () => {
  let component: PerformerEditComponent;
  let fixture: ComponentFixture<PerformerEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PerformerEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PerformerEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
