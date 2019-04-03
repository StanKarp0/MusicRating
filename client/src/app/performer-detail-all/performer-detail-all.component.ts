import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AlbumService } from '../services/album/album.service';
import { PerformerService } from '../services/performer/performer.service';
import { Performer } from '../model/performer';
import { Album } from '../model/album';
import {Subscription, merge, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import { AlbumListComponent } from '../album-list/album-list.component';

@Component({
  selector: 'app-performer-detail-all',
  templateUrl: './performer-detail-all.component.html',
  styleUrls: ['./performer-detail-all.component.css']
})
export class PerformerDetailAllComponent implements OnInit, OnDestroy {

  performer: Performer | null = null;
  subscription: Subscription;
  
  constructor(
    private route: ActivatedRoute,
    private performerService: PerformerService
  ) { }

  ngOnInit() {
    this.subscription = this.route.params.subscribe(params => {
      const performerId = params['performerId'];
      if (performerId) {
        this.performerService.getById(performerId).subscribe((performer: Performer) => {
          if (performer) {
            this.performer = performer;
          } else {
            console.log(`User with id '${performerId}' not found, returning to list`);
          }
        });
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
