import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformerDetailAllComponent } from './performer-detail-all.component';

describe('PerformerDetailAllComponent', () => {
  let component: PerformerDetailAllComponent;
  let fixture: ComponentFixture<PerformerDetailAllComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PerformerDetailAllComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PerformerDetailAllComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
