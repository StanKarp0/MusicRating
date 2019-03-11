import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AlbumEditComponent } from './album-edit/album-edit.component';
import { PerformerListComponent } from './performer-list/performer-list.component';
import { AlbumListAllComponent } from './album-list-all/album-list-all.component';
import { AlbumListRandomComponent } from './album-list-random/album-list-random.component';
import { RatingsListAllComponent } from './ratings-list-all/ratings-list-all.component';
import { UserDetailComponent } from './user-detail/user-detail.component';

const routes: Routes = [
  { path: '', redirectTo: '/random', pathMatch: 'full' },
  // lists
  { path: 'performers', component: PerformerListComponent },
  { path: 'albums', component: AlbumListAllComponent },
  { path: 'ratings', component: RatingsListAllComponent },
  { path: 'random', component: AlbumListRandomComponent },
  
  // details
  { path: 'user/:userName', component: UserDetailComponent},

  // add
  { path: 'album/add', component: AlbumEditComponent },

  // edit
  { path: 'album/:id/edit', component: AlbumEditComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
