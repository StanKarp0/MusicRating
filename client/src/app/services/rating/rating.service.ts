import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RatingList, Rating } from 'src/app/model/rating';
import { User } from 'src/app/model/user';
import { Album } from 'src/app/model/album';
import { NgForm } from '@angular/forms';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RatingService {

  private URL_RATINGS = environment.api + '/ratings';
  private URL_RATINGS_SAVE = this.URL_RATINGS + '/save';
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

  save(rating: NgForm): any {
    return this.http.post(this.URL_RATINGS_SAVE, rating);
  }

  get(ratingId: string): Observable<Rating> {
    return this.http.get<Rating>(this.URL_RATINGS + '/' + ratingId);
  }

  remove(rating: Rating): Observable<{}> {
    const href = rating._links.rating.href; 
    return this.http.delete(href);
  }


}
