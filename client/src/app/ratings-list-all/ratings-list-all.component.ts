import { Component, OnInit, ViewChild } from '@angular/core';
import { Rating } from '../rating';
import { RatingService } from '../shared/rating/rating.service';
import {merge, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import { RatingsListComponent } from '../ratings-list/ratings-list.component';

@Component({
  selector: 'app-ratings-list-all',
  templateUrl: './ratings-list-all.component.html',
  styleUrls: ['./ratings-list-all.component.css']
})
export class RatingsListAllComponent implements OnInit {

  ratings: Rating[] = [];

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  @ViewChild(RatingsListComponent) ratingsList: RatingsListComponent;

  constructor(
    private ratingService: RatingService
  ) { }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    merge(this.ratingsList.changed)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.ratingService.findByPage(
            this.ratingsList.pageSize, this.ratingsList.pageIndex);
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

}
