import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AlbumEditComponent } from './album-edit/album-edit.component';
import { PerformerListComponent } from './performer-list/performer-list.component';
import { AlbumListAllComponent } from './album-list-all/album-list-all.component';
import { AlbumListRandomComponent } from './album-list-random/album-list-random.component';
import { RatingsListAllComponent } from './ratings-list-all/ratings-list-all.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { PerformerDetailAllComponent } from './performer-detail-all/performer-detail-all.component';
import { AlbumDetailComponent } from './album-detail/album-detail.component';
import { PerformerEditComponent } from './performer-edit/performer-edit.component';
import { PerformerAddComponent } from './performer-add/performer-add.component';
import { RatingEditComponent } from './rating-edit/rating-edit.component';

const routes: Routes = [
  { path: '', redirectTo: '/random', pathMatch: 'full' },
  // lists
  { path: 'performers', component: PerformerListComponent },
  { path: 'albums', component: AlbumListAllComponent },
  { path: 'ratings', component: RatingsListAllComponent },
  { path: 'random', component: AlbumListRandomComponent },
  
  // details
  { path: 'user/:username', component: UserDetailComponent},
  { path: 'performer/:performerId/details', component: PerformerDetailAllComponent},
  { path: 'album/:albumId/details', component: AlbumDetailComponent},

  // add
  { path: 'performer/:performerId/add', component: AlbumEditComponent },
  { path: 'performer/add', component: PerformerAddComponent },
  { path: 'album/:albumId/rate', component: RatingEditComponent },

  // edit
  { path: 'performer/:performerId/edit', component: PerformerEditComponent },
  { path: 'album/:albumId/edit', component: AlbumEditComponent },
  { path: 'rating/:ratingId/edit', component: RatingEditComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
