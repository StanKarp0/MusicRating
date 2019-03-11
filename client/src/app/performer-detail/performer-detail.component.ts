import { Component, OnInit, Input } from '@angular/core';
import { Performer } from '../performer';
import { AlbumService } from '../shared/album/album.service';
import { Album } from '../album';

@Component({
  selector: 'app-performer-detail',
  templateUrl: './performer-detail.component.html',
  styleUrls: ['./performer-detail.component.css']
})
export class PerformerDetailComponent implements OnInit {

  @Input() _performer: Performer;
  albums: Album[];

  resultsLength: number = 0;
  pageSize: number = 0;

  constructor(
    private albumService: AlbumService
  ) { }

  @Input() 
  set performer(performer: Performer) {
    this._performer = performer;
    this.getAlbums()
  }

  ngOnInit() { }

  getAlbums() {
    if (this._performer) {
      this.albumService.findByPerformer(this._performer).subscribe(data => {
        this.albums = data._embedded.albums;
        this.resultsLength = this.albums.length;
        this.pageSize = this.albums.length;
      });
    }
  }

}
