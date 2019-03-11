import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlbumListAllComponent } from './album-list-all.component';

describe('AlbumListAllComponent', () => {
  let component: AlbumListAllComponent;
  let fixture: ComponentFixture<AlbumListAllComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlbumListAllComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlbumListAllComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
