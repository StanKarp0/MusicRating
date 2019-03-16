import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformerEditorComponent } from './performer-editor.component';

describe('PerformerEditorComponent', () => {
  let component: PerformerEditorComponent;
  let fixture: ComponentFixture<PerformerEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PerformerEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PerformerEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
