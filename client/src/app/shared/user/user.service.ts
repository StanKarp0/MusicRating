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

  constructor(private http: HttpClient) { }

  getByUserName(userName: string): Observable<User> {
    throw new Error("Method not implemented.");
  }

}
