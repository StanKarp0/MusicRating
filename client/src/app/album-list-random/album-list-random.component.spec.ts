import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlbumListRandomComponent } from './album-list-random.component';

describe('AlbumListRandomComponent', () => {
  let component: AlbumListRandomComponent;
  let fixture: ComponentFixture<AlbumListRandomComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlbumListRandomComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlbumListRandomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
