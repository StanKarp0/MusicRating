import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RatingList } from 'src/app/rating';

@Injectable({
  providedIn: 'root'
})
export class RatingService {

  public API = '//localhost:8080';
  private URL_RATINGS = this.API + '/ratings';

  constructor(private http: HttpClient) { }

  findByPage(pageSize: number, pageIndex: number): Observable<RatingList> {
    return this.http.get<RatingList>(this.URL_RATINGS, {
      params:{
        size: pageSize.toString(),
        page: pageIndex.toString(),
      }});
  }

  // findByUser(User)
}
