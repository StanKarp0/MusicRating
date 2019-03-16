import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PerformerList, Performer } from '../../performer';

@Injectable({
  providedIn: 'root'
})
export class PerformerService {

  public API = '//localhost:8080';
  private URL_PERFORMERS = this.API + '/performers';
  private URL_SEARCH = this.URL_PERFORMERS + '/search/findByQuery';

  constructor(private http: HttpClient) { }

  findByPage(pageSize: number, pageIndex: number): Observable<PerformerList> {
    return this.http.get<PerformerList>(this.URL_PERFORMERS, {
      params:{
        size: pageSize.toString(),
        page: pageIndex.toString(),
      }});
  }

  findByName(query: string, pageSize: number, pageIndex: number): Observable<PerformerList> {
    if (query == "") {
      return this.findByPage(pageSize, pageIndex);
    } else {
      return this.http.get<PerformerList>(this.URL_SEARCH, {
        params: {
          query: query,
          size: pageSize.toString(),
          page: pageIndex.toString()
        }
      })
    }
  }

  getById(id: string): Observable<Performer> {
    return this.http.get<Performer>(this.URL_PERFORMERS + '/' + id);
  }

  save(performerForm: Performer): Observable<Performer> {
    return this.http.post(this.URL_PERFORMERS, performerForm);
  }

  remove(href: string) {
    return this.http.delete(href);
  }
}
