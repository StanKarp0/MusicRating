import { Component, OnInit, ViewChild } from '@angular/core';
import { PerformerEditorComponent } from '../performer-editor/performer-editor.component';
import { PerformerService } from '../shared/performer/performer.service';
import { merge, of as observableOf, Subscription, forkJoin } from 'rxjs';
import { catchError, map, startWith, switchMap } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { PerformerForm, Performer } from '../performer';
import { Album } from '../album';
import { AlbumService } from '../shared/album/album.service';

@Component({
  selector: 'app-performer-add',
  templateUrl: './performer-add.component.html',
  styleUrls: ['./performer-add.component.css']
})
export class PerformerAddComponent implements OnInit {

  @ViewChild(PerformerEditorComponent) performerEditor: PerformerEditorComponent;

  performer: Performer | null;
  albums: {title:string, year: number, album?: Album}[];
  
  constructor(
    private router: Router,
    private performerService: PerformerService,
    private albumService: AlbumService,
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
