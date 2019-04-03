import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { Rating } from '../model/rating';
import { MatPaginator, MatSort } from '@angular/material';
import {merge, of as observableOf} from 'rxjs';
import {animate, state, style, transition, trigger} from '@angular/animations';
import { Router } from '@angular/router';
import { TokenStorageService } from '../services/auth/token-storage.service';

@Component({
  selector: 'app-ratings-list',
  templateUrl: './ratings-list.component.html',
  styleUrls: ['./ratings-list.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0', display: 'none'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class RatingsListComponent implements OnInit {

  columnsToDisplay = ['date', 'userName', 'name', 'title', 'rate'];
  expandedElement: Rating | null;
  loggedUsername: string;
  authorities: string[] = [];

  constructor(
    private router: Router,
    private tokenStorage: TokenStorageService
    ) { }

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  @Input()
  ratings: Rating[];

  @Input()
  isLoadingResults: boolean = false;

  @Input()
  isRateLimitReached: boolean = false;

  @Input()
  resultsLength: number = 0;

  @Output() 
  changed = new EventEmitter<any>();

  @Output()
  get pageSize(): number {
    return this.paginator.pageSize;
  }

  @Output()
  get pageIndex(): number {
    return this.paginator.pageIndex;
  }

  @Output()
  deleteClicked = new EventEmitter<Rating>();

  ngOnInit() {
    if (this.tokenStorage.getToken()) {
      this.authorities = this.tokenStorage.getAuthorities();
      this.loggedUsername = this.tokenStorage.getUsername();
    }
  }

  ngAfterViewInit(): void {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page)
    .subscribe(() => {
      this.changed.emit(null);      
    });
  }

  remove(rating: Rating) {
    this.deleteClicked.emit(rating);
  }
}
