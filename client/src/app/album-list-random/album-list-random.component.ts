import { Component, OnInit, EventEmitter, ViewChild } from '@angular/core';
import { Album, AlbumList } from '../album';
import { AlbumService } from '../shared/album/album.service';
import {merge, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import { AlbumListComponent } from '../album-list/album-list.component';


@Component({
  selector: 'app-album-list-random',
  templateUrl: './album-list-random.component.html',
  styleUrls: ['./album-list-random.component.css']
})
export class AlbumListRandomComponent implements OnInit {

  albums: Album[];
  decades: Object;
  years: Object;
  href: string | null = null;

  isLoadingResults = true;
  isRateLimitReached = false;

  reloadClicked: EventEmitter<any> = new EventEmitter();

  @ViewChild(AlbumListComponent) albumList: AlbumListComponent;

  constructor(
    private albumService: AlbumService
  ) { }

  ngOnInit() {
  }


  ngAfterViewInit(): void {
    merge(this.reloadClicked)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          if (this.href) {
            return this.albumService.findRandomHref(this.href)
          } else {
            return this.albumService.findRandom();
          }
        }),
        map(data => {
          this.isLoadingResults = false;
          this.isRateLimitReached = false;
          return data
        }),
        catchError((e) => {
          console.error('Error', e)
          this.isLoadingResults = false;
          this.isRateLimitReached = true;
          return observableOf(new AlbumList());
        })
      ).subscribe(data => {
        if (data._embedded) {
          this.albums = data._embedded.albums;
        } else {
          this.albums = [];
        }

        this.decades = {all: {
          name: 'All',
          href: null
        }};
        this.years = {};
        
        for (let key in data._links) {
          if (key.startsWith("decade_")) {
            this.decades[key] = {
              name: key.slice(7) + "0's",
              href: data._links[key]['href']
            }
          } else if (key.startsWith('year_')) {
            this.years[key] = {
              name: key.slice(5),
              href: data._links[key]['href']
            }
          }
        }
    });
  }

  refresh() {
    this.reloadClicked.emit(null);
  }

  loadHref(href: string | null): void {
    this.href = href;
    this.reloadClicked.emit(null);
  }
}
