import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AppRoutingModule} from './app.routing.module';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

import {AppComponent } from './app.component';
import {CompanyComponent } from './company/company.component';
import {CompanySourceComponent} from './company-source/company-source.component';
import {DataTableModule} from 'angular-4-data-table-bootstrap-4';
import {AppService} from './app.service';
import {HttpModule} from '@angular/http';
import {ModalModule} from 'ngx-bootstrap/modal';
import {ToastModule} from 'ng2-toastr';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HashLocationStrategy, LocationStrategy} from "@angular/common";


/*import {HttpClientModule, HttpClient} from '@angular/common/http';
import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}*/

@NgModule({
  declarations: [
    AppComponent,
    CompanyComponent,
    CompanySourceComponent
  ],
  imports: [
    ModalModule.forRoot(),
    ToastModule.forRoot(),
    FormsModule,
    NgbModule.forRoot(),
    ReactiveFormsModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    BrowserModule,
    DataTableModule,
    HttpModule
    /*HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })*/
  ],
  providers: [AppService,
    {provide: LocationStrategy, useClass: HashLocationStrategy}
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
