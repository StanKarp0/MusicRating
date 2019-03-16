import { Component, OnInit, OnDestroy } from '@angular/core';
import { Album } from '../album';
import { Subscription } from 'rxjs';
import { NgForm } from '@angular/forms';
import { AlbumService } from '../shared/album/album.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-album-edit',
  templateUrl: './album-edit.component.html',
  styleUrls: ['./album-edit.component.css']
})
export class AlbumEditComponent implements OnInit, OnDestroy {

  album: Album = new Album();
  newAlbum: boolean = true;
  subscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private albumService: AlbumService) { }

  ngOnInit() {

    this.subscription = this.route.params.subscribe(params => {
      const albumId = params['albumId'];
      const performerId = params['performerId']

      if (albumId) {
        this.albumService.get(albumId).subscribe((album: Album) => {
          if (album) {
            this.album = album;
            this.newAlbum = false;
          } else {
            console.log(`Album with id '${albumId}' not found, returning to list`);
            this.goBack()
          }
        });
      } else if (performerId) {
        this.album.performerId = performerId;
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  goBack() {
    this.router.navigate(['/album', this.album.albumId, 'details']);
  }

  save(form: NgForm) {
    console.log(form);
    this.albumService.save(form).subscribe(album => {
      this.album = album;
      this.goBack();
    }, error => console.error(error));
  }

}
