import { Component, OnInit, ViewChild, AfterViewInit, EventEmitter } from '@angular/core';
import { Album } from '../model/album';
import { Rating } from '../model/rating';
import {Subscription, merge, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import { RatingsListComponent } from '../ratings-list/ratings-list.component';
import { ActivatedRoute, Router } from '@angular/router';
import { AlbumService } from '../services/album/album.service';
import { RatingService } from '../services/rating/rating.service';

@Component({
  selector: 'app-album-detail',
  templateUrl: './album-detail.component.html',
  styleUrls: ['./album-detail.component.css']
})
export class AlbumDetailComponent implements OnInit, AfterViewInit {

  album: Album | null = null;
  ratings: Rating[];
  subscription: Subscription;

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  pageSize = 10
  pageIndex = 0

  @ViewChild(RatingsListComponent) ratingsList: RatingsListComponent;

  listChanged = new EventEmitter<any>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private albumService: AlbumService,
    private ratingService: RatingService
  ) { }

  ngOnInit() {
    this.subscription = this.route.params.subscribe(params => {
      const albumId = params['albumId'];
      if (albumId) {
        this.albumService.get(albumId).subscribe((album: Album) => {
          if (album) {
            this.album = album;
            this.getRatings()
          } else {
            console.log(`Album with id '${albumId}' not found, returning to list`);
          }
        });
      }
    });
  }

  getRatings() {
    merge(this.ratingsList.changed, this.listChanged).pipe(
      startWith({}),
      switchMap(() => {
        this.isLoadingResults = true;
        return this.ratingService.findByAlbum(
          this.album, this.ratingsList.pageSize, this.ratingsList.pageIndex);
      }),
      map(data => {
        this.isLoadingResults = false;
        this.isRateLimitReached = false;
        this.resultsLength = data.page.totalElements;
        return data._embedded.ratings;
      }),
      catchError((e) => {
        console.error('Error', e)
        this.isLoadingResults = false;
        this.isRateLimitReached = true;
        return observableOf([]);
      })
    ).subscribe(ratings => {
      this.ratings = ratings      
    });
  }

  ngAfterViewInit() {
    this.ratingsList.deleteClicked.pipe(
      switchMap((rating: Rating) => {
        return this.ratingService.remove(rating);
      })
    ).subscribe(result => {
      this.listChanged.emit(null)
    })
  }

  remove() {
    this.albumService.remove(this.album).subscribe(result => {
      this.gotoPerformer()
    });
  }

  gotoPerformer() {
    this.router.navigate(['/performer', this.album.performerId, 'details']);
  }

}
