import { Component, OnInit, ViewChild, OnDestroy, EventEmitter } from '@angular/core';
import { User } from '../model/user';
import { ActivatedRoute } from '@angular/router';
import { Rating } from '../model/rating';
import { RatingsListComponent } from '../ratings-list/ratings-list.component';
import {Subscription, merge, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import { UserService } from '../services/user/user.service';
import { RatingService } from '../services/rating/rating.service';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit, OnDestroy {

  user: User | null = null;
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
    private userService: UserService,
    private ratingService: RatingService
  ) { }

  ngOnInit() {
    this.subscription = this.route.params.subscribe(params => {
      const username = params['username'];
      if (username) {
        this.userService.getByUserName(username).subscribe((user: User) => {
          if (user) {
            this.user = user;
            this.getRatings()
          } else {
            console.log(`User with id '${username}' not found, returning to list`);
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
          return this.ratingService.findByUser(
            this.user, this.ratingsList.pageSize, this.ratingsList.pageIndex);
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

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
