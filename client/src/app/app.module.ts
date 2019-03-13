import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { MatButtonModule, MatCardModule, MatInputModule, MatListModule, MatToolbarModule, MatMenuModule, MatProgressSpinnerModule, MatPaginatorModule, MatSortModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AlbumListComponent } from './album-list/album-list.component';
import { AlbumEditComponent } from './album-edit/album-edit.component';
import { PerformerListComponent } from './performer-list/performer-list.component';
import { PerformerDetailComponent } from './performer-detail/performer-detail.component';
import { AlbumListAllComponent } from './album-list-all/album-list-all.component';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatTableModule} from '@angular/material/table';
import { AlbumListRandomComponent } from './album-list-random/album-list-random.component';
import { RatingsListComponent } from './ratings-list/ratings-list.component';
import { RatingsListAllComponent } from './ratings-list-all/ratings-list-all.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { PerformerDetailAllComponent } from './performer-detail-all/performer-detail-all.component';
import { AlbumDetailComponent } from './album-detail/album-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    AlbumListComponent,
    AlbumEditComponent,
    PerformerListComponent,
    PerformerDetailComponent,
    AlbumListAllComponent,
    AlbumListRandomComponent,
    RatingsListComponent,
    RatingsListAllComponent,
    UserDetailComponent,
    PerformerDetailAllComponent,
    AlbumDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCardModule,
    MatInputModule,
    MatListModule,
    MatToolbarModule,
    FormsModule,
    MatMenuModule,
    MatExpansionModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    MatSortModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
