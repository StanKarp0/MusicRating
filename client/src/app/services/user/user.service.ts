import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { User } from 'src/app/model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  public API = '//localhost:8080';
  private URL_USERS = this.API + '/users';
  private URL_FIND = this.URL_USERS + '/search/findByUsername';
  // private userUrl = 'http://localhost:8080/api/test/user';
  // private pmUrl = 'http://localhost:8080/api/test/pm';
  // private adminUrl = 'http://localhost:8080/api/test/admin';

  constructor(private http: HttpClient) { }

  getByUserName(username: string): Observable<User> {
    return this.http.get<User>(this.URL_FIND, {
      params: {
        username: username
      }
    })
  }
  
  getUserBoard(): Observable<string> {
    // return this.http.get(this.userUrl, { responseType: 'text' });
    return of('');
  }
 
  getPMBoard(): Observable<string> {
    // return this.http.get(this.pmUrl, { responseType: 'text' });
    return of('');
  }
 
  getAdminBoard(): Observable<string> {
    // return this.http.get(this.adminUrl, { responseType: 'text' });
    return of('');
  }

}
