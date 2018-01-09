import {Component, ViewContainerRef} from '@angular/core';
import {ToastsManager} from 'ng2-toastr';
/*import { TranslateService } from '@ngx-translate/core';*/

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'app';

  constructor(public toastr: ToastsManager, vcr: ViewContainerRef) {
    this.toastr.setRootViewContainerRef(vcr);
  }
/*
  constructor(private translate: TranslateService) {
    translate.setDefaultLang('es');
  }

  switchLanguage(language: string) {
    this.translate.use(language);
  }*/
}
