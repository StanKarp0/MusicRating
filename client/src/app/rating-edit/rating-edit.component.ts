import { Component, OnInit, OnDestroy } from '@angular/core';
import { Rating } from '../rating';
import { Subscription } from 'rxjs';
import { RatingService } from '../shared/rating/rating.service';
import { Router, ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';
import { AlbumService } from '../shared/album/album.service';
import { Album } from '../album';
import { TokenStorageService } from '../auth/token-storage.service';

@Component({
  selector: 'app-rating-edit',
  templateUrl: './rating-edit.component.html',
  styleUrls: ['./rating-edit.component.css']
})
export class RatingEditComponent implements OnInit, OnDestroy {

  album: Album | null = null;
  rating: Rating = new Rating();
  subscription: Subscription;
  
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ratingService: RatingService,
    private albumService: AlbumService,
    private token: TokenStorageService,
  ) { }

  ngOnInit() {

    this.subscription = this.route.params.subscribe(params => {
      const albumId = params['albumId'];
      const ratingId = params['ratingId']

      if (ratingId) {
        this.ratingService.get(ratingId).subscribe((rating: Rating) => {
          if (rating) {
            this.rating = rating;
          } else {
            console.log(`Rating with id '${ratingId}' not found, returning to list`);
            this.goBack()
          }
        });
      } else if (albumId) {
        this.rating.albumId = albumId;
        this.rating.userName = this.token.getUsername();
        this.albumService.get(albumId).subscribe((album: Album) => {
          if (album) {
            this.album = album;
          }
        });
      }
    });
  }

  

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  goBack() {
    this.router.navigate(['/album', this.rating.albumId, 'details']);
  }

  save(form: NgForm) {
    this.ratingService.save(form).subscribe(result => {
      this.goBack();
    }, error => console.error(error));
  }

}
