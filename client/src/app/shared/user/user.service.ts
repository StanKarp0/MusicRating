import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from 'src/app/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  public API = '//localhost:8080';
  private URL_USERS = this.API + '/users';
  private URL_FIND = this.URL_USERS + '/search/findByUsername';

  constructor(private http: HttpClient) { }

  getByUserName(username: string): Observable<User> {
    return this.http.get<User>(this.URL_FIND, {
      params: {
        username: username
      }
    })
  }

}
