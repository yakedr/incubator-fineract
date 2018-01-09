import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {CompanyComponent} from './company/company.component' ;
import {CompanySourceComponent} from './company-source/company-source.component';

const routes: Routes = [
  { path: '', redirectTo: 'company-list', pathMatch: 'full'},
  { path: 'company-list', component: CompanyComponent },
  { path: 'source-list', component: CompanySourceComponent }
 /* { path: 'login', loadChildren: './login/login.module#LoginModule' },
  { path: 'signup', loadChildren: './signup/signup.module#SignupModule' },
  { path: 'not-found', loadChildren: './not-found/not-found.module#NotFoundModule' },
  { path: '**', redirectTo: 'not-found' }*/
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
