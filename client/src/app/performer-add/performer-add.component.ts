import { Component, OnInit, ViewChild } from '@angular/core';
import { PerformerEditorComponent } from '../performer-editor/performer-editor.component';
import { PerformerService } from '../services/performer/performer.service';
import { merge, of as observableOf } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { PerformerForm, Performer } from '../model/performer';

@Component({
  selector: 'app-performer-add',
  templateUrl: './performer-add.component.html',
  styleUrls: ['./performer-add.component.css']
})
export class PerformerAddComponent implements OnInit {

  @ViewChild(PerformerEditorComponent) performerEditor: PerformerEditorComponent;

  private performer: Performer | null;
  
  constructor(
    private router: Router,
    private performerService: PerformerService,
  ) { }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    merge(this.performerEditor.commited)
      .pipe(
        switchMap(data => {
          const performer = new PerformerForm();
          performer.name = data['name'];
          performer.albums = data['albums']
          console.log('switchMap', data)
          return this.performerService.save(performer);
        }),
        catchError((e) => {
          console.error('Error', e)
          return observableOf({});
        })
      ).subscribe(performer => {
        this.performer = performer;
        this.router.navigate(['/performer', this.performer.performerId, 'details']);
      });
  }

}
