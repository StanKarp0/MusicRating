import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Album, AlbumList } from '../../album'
import { Performer } from 'src/app/performer';

@Injectable({
  providedIn: 'root'
})
export class AlbumService {

  public API = '//localhost:8080';
  private URL_ALBUMS = this.API + '/albums';
  private URL_SEARCH = this.URL_ALBUMS + '/search/findByQuery';
  private URL_RANDOM = this.API

  constructor(private http: HttpClient) { }

  findByPage(pageSize: number, pageIndex: number): Observable<AlbumList> {
    return this.http.get<AlbumList>(this.URL_ALBUMS, {
      params:{
        size: pageSize.toString(),
        page: pageIndex.toString(),
      }});
  }

  findByPerformer(performer: Performer): Observable<AlbumList> {
    const url = performer._links.albums.href;
    return this.http.get<AlbumList>(url);
  }

  findByQuery(filterValue: string, size: number, page: number): Observable<AlbumList> {
    if (filterValue == "") {
      return this.findByPage(size, page);
    } else {
      return this.http.get<AlbumList>(this.URL_SEARCH, {
        params: {
          query: filterValue,
          size: size.toString(),
          page: page.toString()
        }
      });
    }
  }

  findRandom(): Observable<AlbumList> {
    return this.http.get<AlbumList>(this.URL_RANDOM);
  }

  findRandomHref(href: string): Observable<AlbumList> {
    return this.http.get<AlbumList>(href);
  }

  get(id: string): Observable<Album> {
    return this.http.get<Album>(this.URL_ALBUMS + '/' + id);
  }

  save(album: any): Observable<Object> {
    let result: Observable<Object>;
    if (album['href']) {
      console.log('Put album', album.href, album);
      result = this.http.put(album.href, album);
    } else {
      console.log('Post album', this.URL_ALBUMS, album);
      result = this.http.post(this.URL_ALBUMS, album);
    }
    return result;
  }

  remove(href: string) {
    return this.http.delete(href);
  }
}
