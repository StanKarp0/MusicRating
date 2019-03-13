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
          name: query,
          size: pageSize.toString(),
          page: pageIndex.toString()
        }
      })
    }
  }

  getById(id: string): Observable<Performer> {
    return this.http.get<Performer>(this.URL_PERFORMERS + '/' + id);
  }

  save(performer: any): Observable<Object> {
    let result: Observable<Object>;
    if (performer['href']) {
      console.log('Put performer', performer.href, performer);
      result = this.http.put(performer.href, performer);
    } else {
      console.log('Post performer', this.URL_PERFORMERS, performer);
      result = this.http.post(this.URL_PERFORMERS, performer);
    }
    return result;
  }

  remove(href: string) {
    return this.http.delete(href);
  }
}
