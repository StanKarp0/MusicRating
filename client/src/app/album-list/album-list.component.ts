import { Component, OnInit, Input, ViewChild, AfterViewInit, Output, EventEmitter } from '@angular/core';
import { Album } from '../album';
import {animate, state, style, transition, trigger} from '@angular/animations';
import { MatPaginator, MatSort } from '@angular/material';
import {merge, of as observableOf} from 'rxjs';

@Component({
  selector: 'app-album-list',
  templateUrl: './album-list.component.html',
  styleUrls: ['./album-list.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0', display: 'none'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class AlbumListComponent implements OnInit {
  // https://material.angular.io/components/table/examples

  columnsToDisplay = ['title', 'name', 'year', 'average'];
  expandedElement: Album | null;

  constructor() { }

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  @Input()
  albums: Album[];

  @Input()
  resultsLength: number = 0;

  @Input()
  size: number = 0;

  @Input()
  isLoadingResults: boolean = false;

  @Input()
  isRateLimitReached: boolean = false;

  @Output() 
  changed = new EventEmitter<any>();

  @Output() 
  deleteClicked = new EventEmitter<Album>();

  @Output()
  get pageSize(): number {
    return this.paginator.pageSize;
  }

  @Output()
  get pageIndex(): number {
    return this.paginator.pageIndex;
  }

  ngOnInit() {
    this.paginator.pageSize = 10;
  }

  ngAfterViewInit(): void {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page)
    .subscribe(() => {
      this.changed.emit(null);      
    });
  }

  remove(album: Album) {
    this.deleteClicked.emit(album);
  }
}
