import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RatingList } from 'src/app/rating';
import { User } from 'src/app/user';
import { Album } from 'src/app/album';

@Injectable({
  providedIn: 'root'
})
export class RatingService {

  public API = '//localhost:8080';
  private URL_RATINGS = this.API + '/ratings';
  private URL_FIND_USER = this.URL_RATINGS + '/search/username';
  private URL_FIND_ALBUM = this.URL_RATINGS + '/search/findByAlbum';

  constructor(private http: HttpClient) { }

  findByPage(size: number, page: number): Observable<RatingList> {
    return this.http.get<RatingList>(this.URL_RATINGS, {
      params:{
        size: size.toString(),
        page: page.toString(),
      }});
  }

  findByUser(user: User, size: number, page: number): Observable<RatingList> {
    return this.http.get<RatingList>(this.URL_FIND_USER, {
      params: {
        username: user.username,
        size: size.toString(),
        page: page.toString()      
      }
    })
  }

  findByAlbum(album: Album, size: number, page: number): Observable<RatingList> {
    return this.http.get<RatingList>(this.URL_FIND_ALBUM, {
      params: {
        albumId: album.albumId.toString(),
        size: size.toString(),
        page: page.toString()      
      }
    })
  }

}
