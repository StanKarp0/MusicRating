import { Component, OnInit, ViewChild, EventEmitter } from '@angular/core';
import { Album } from '../album';
import { AlbumService } from '../shared/album/album.service';
import { MatSort, MatPaginator } from '@angular/material';
import {merge, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import { AlbumListComponent } from '../album-list/album-list.component';

@Component({
  selector: 'app-album-list-all',
  templateUrl: './album-list-all.component.html',
  styleUrls: ['./album-list-all.component.css']
})
export class AlbumListAllComponent implements OnInit {

  albums: Album[];

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  query: string = '';
  queryChanged: EventEmitter<any> = new EventEmitter();

  @ViewChild(AlbumListComponent) albumList: AlbumListComponent;

  constructor(
    private albumService: AlbumService
  ) {}

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    merge(this.albumList.changed, this.queryChanged)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.albumService.findByQuery(this.query, 
            this.albumList.pageSize, 
            this.albumList.pageIndex);
        }),
        map(data => {
          this.isLoadingResults = false;
          this.isRateLimitReached = false;
          this.resultsLength = data.page.totalElements;
          return data._embedded.albums;
        }),
        catchError((e) => {
          console.error('Error', e)
          this.isLoadingResults = false;
          this.isRateLimitReached = true;
          return observableOf([]);
        })
      ).subscribe(albums => {
        this.albums = albums      
      });
  }

  applyFilter(filterValue: string) {
    this.query = filterValue;
    this.queryChanged.emit(null);
  }

}

