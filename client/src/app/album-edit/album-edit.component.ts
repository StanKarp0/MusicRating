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

  subscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private albumService: AlbumService) { }

  ngOnInit() {

    this.subscription = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.albumService.get(id).subscribe((album: Album) => {
          if (album) {
            this.album = album;
            this.album.href = album._links['self']['href'];
          } else {
            console.log(`Car with id '${id}' not found, returning to list`);
            this.gotoList();
          }
        });
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  gotoList() {
    this.router.navigate(['/albums']);
  }

  save(form: NgForm) {
    this.albumService.save(form).subscribe(result => {
      this.gotoList();
    }, error => console.error(error));
  }

  remove(href: string) {
    this.albumService.remove(href).subscribe(result => {
      this.gotoList();
    }, error => console.error(error));
  }
}
