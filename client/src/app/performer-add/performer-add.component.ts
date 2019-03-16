import { Component, OnInit, ViewChild } from '@angular/core';
import { PerformerEditorComponent } from '../performer-editor/performer-editor.component';
import { PerformerService } from '../shared/performer/performer.service';
import { merge, of as observableOf, Subscription, forkJoin } from 'rxjs';
import { catchError, map, startWith, switchMap } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { Performer } from '../performer';
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
          this.albums = data['albums'];
          const performer = new Performer();
          performer.name = data['name'];
          return this.performerService.save(performer);
        }),
        switchMap(performer => {
          this.performer = performer;
          return forkJoin(...this.albums.map(albumForm => {
            const album = new Album();
            album.title = albumForm['title'];
            album.year = albumForm['year'];
            album.performerId = performer.performerId;
            return this.albumService.save(album);
          }));
        }),
        catchError((e) => {
          console.error('Error', e)
          return observableOf([]);
        })
      ).subscribe(albums => {
        this.router.navigate(['/performer', this.performer.performerId, 'details']);
      });
  }

}
