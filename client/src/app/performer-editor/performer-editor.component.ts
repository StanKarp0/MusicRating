import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Validators, AbstractControl, FormBuilder, FormArray } from '@angular/forms';
import { Performer } from '../performer';
import { BehaviorSubject } from 'rxjs';
import { AlbumService } from '../shared/album/album.service';
import { Album } from '../album';

@Component({
  selector: 'app-performer-editor',
  templateUrl: './performer-editor.component.html',
  styleUrls: ['./performer-editor.component.css']
})
export class PerformerEditorComponent implements OnInit {

  performerForm = this.fb.group({
    name: ['', Validators.required],
    albums: this.fb.array([this.createAlbum()])
  });
  _performer: Performer | null;

  dataSource = new BehaviorSubject<AbstractControl[]>([]);
  displayColumns = ['title', 'year', 'remove'];

  @Input()
  set performer(performer: Performer) {
    this._performer = performer
    if (performer) {
      this.albumService.findByPerformer(performer, performer.albumCount, 0).subscribe(albums => {
        this.performerForm.patchValue({name: performer.name});
        this.albums.removeAt(0);
        albums._embedded.albums.forEach(album => this.albums.push(this.createExistingAlbum(album)));
        this.dataSource.next(this.albums.controls);
      })    
    }
  }

  @Output() commited = new EventEmitter<Object>();

  constructor(
    private fb: FormBuilder,
    private albumService: AlbumService
    ) { }

  ngOnInit() {
    this.dataSource.next(this.albums.controls);
  }

  onSubmit() {
    this.commited.emit(this.performerForm.value)
  }

  get albums() {
    return this.performerForm.get('albums') as FormArray;
  }

  addAlbum() {
    this.albums.push(this.createAlbum());
    this.dataSource.next(this.albums.controls);
  }

  createAlbum() {
    return this.fb.group({
      title: this.fb.control('', Validators.required),
      year: this.fb.control('', [Validators.required, Validators.pattern("[0-9]{4}")]),
    })
  }

  createExistingAlbum(album: Album) {
    return this.fb.group({
      title: this.fb.control(album.title, Validators.required),
      year: this.fb.control(album.year, [Validators.required, Validators.pattern("[0-9]{4}")]),
      album: this.fb.control(album),
    })
  }

  removeAlbum(index: number) {
    this.albums.removeAt(index)
    this.dataSource.next(this.albums.controls);
  }
}
