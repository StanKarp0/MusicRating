import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../shared/user/user.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {

  user: User | null = null;
  subscription: Subscription;
  
  constructor(
    private route: ActivatedRoute,
    private userService: UserService
  ) { }

  ngOnInit() {

    this.subscription = this.route.params.subscribe(params => {
      const userName = params['userName'];
      if (userName) {
        this.userService.getByUserName(userName).subscribe((user: User) => {
          if (user) {
            this.user = user;
          } else {
            console.log(`User with id '${userName}' not found, returning to list`);
          }
        });
      }
    });

  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
