import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { MatButtonModule, MatCardModule, MatInputModule, MatListModule, MatToolbarModule, MatMenuModule, MatProgressSpinnerModule, MatPaginatorModule, MatSortModule, MatSliderModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatExpansionModule } from '@angular/material/expansion';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AlbumListComponent } from './album-list/album-list.component';
import { AlbumEditComponent } from './album-edit/album-edit.component';
import { PerformerListComponent } from './performer-list/performer-list.component';
import { PerformerDetailComponent } from './performer-detail/performer-detail.component';
import { AlbumListAllComponent } from './album-list-all/album-list-all.component';
import { AlbumListRandomComponent } from './album-list-random/album-list-random.component';
import { RatingsListComponent } from './ratings-list/ratings-list.component';
import { RatingsListAllComponent } from './ratings-list-all/ratings-list-all.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { PerformerDetailAllComponent } from './performer-detail-all/performer-detail-all.component';
import { AlbumDetailComponent } from './album-detail/album-detail.component';
import { PerformerEditComponent } from './performer-edit/performer-edit.component';
import { PerformerEditorComponent } from './performer-editor/performer-editor.component';
import { PerformerAddComponent } from './performer-add/performer-add.component';
import { RatingEditComponent } from './rating-edit/rating-edit.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AdminComponent } from './admin/admin.component';
import { httpInterceptorProviders } from './services/auth/auth-interceptor';

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
    AlbumDetailComponent,
    PerformerEditComponent,
    PerformerEditorComponent,
    PerformerAddComponent,
    RatingEditComponent,
    LoginComponent,
    RegisterComponent,
    AdminComponent
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
    ReactiveFormsModule,
    MatSliderModule,
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
