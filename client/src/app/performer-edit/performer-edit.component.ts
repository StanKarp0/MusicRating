import { Component, OnInit, OnDestroy } from '@angular/core';
import { Performer, PerformerForm } from '../model/performer';
import { Subscription } from 'rxjs';
import { Router, ActivatedRoute } from '@angular/router';
import { PerformerService } from '../services/performer/performer.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-performer-edit',
  templateUrl: './performer-edit.component.html',
  styleUrls: ['./performer-edit.component.css']
})
export class PerformerEditComponent implements OnInit, OnDestroy {

  performer: Performer = new Performer();
  subscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private performerService: PerformerService,
    ) { }

  ngOnInit() {

    this.subscription = this.route.params.subscribe(params => {
      const performerId = params['performerId'];

      if (performerId) {
        this.performerService.getById(performerId).subscribe((performer: Performer) => {
          if (performer) {
            this.performer = performer;
          } else {
            console.log(`Performer with id '${performerId}' not found, returning to list`);
          }
        });
      }
    });

  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  gotoPerformer() {
    this.router.navigate(['/performer', this.performer.performerId, 'details']);
  }

  save(form: NgForm) {
    const performer = new PerformerForm();
    performer.name = form['name']
    this.performerService.save(performer).subscribe(() => {
      this.gotoPerformer();
    }, error => console.error(error));
  }

}
