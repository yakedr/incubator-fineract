///<reference path="../../../node_modules/@angular/core/src/metadata/directives.d.ts"/>
import {Component, OnInit, ViewChild, ViewContainerRef, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {DataTable, DataTableResource} from 'angular-4-data-table-bootstrap-4';
import {AppService} from '../app.service';
import {configApp} from '../app.config';
import {Source} from '../interfaces/source';
import {ToastsManager} from 'ng2-toastr';
import * as moment from 'moment';

@Component({
  selector: 'app-company-source',
  templateUrl: './company-source.component.html',
  styleUrls: ['./company-source.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CompanySourceComponent implements OnInit {
  paginator = {
    pageTotal: configApp.paginator.limit,
    total: 0,
    currentPage: 1
  };

  sources = [];
  sourcesCount = 0;
  sourceResource = null;
  @ViewChild(DataTable) sourceTable: DataTable;
  selectedSource: Source = null;
  sourceToAdd: Source;
  sourceForm: FormGroup;
  sourceUpdateForm: FormGroup;

  constructor(private appService: AppService, private formBuilder: FormBuilder, public toastr: ToastsManager, vcr: ViewContainerRef) {
    this.toastr.setRootViewContainerRef(vcr);
  }

  ngOnInit() {
    this.CallServiceForSources();
    this.sourceResource = new DataTableResource(this.sources);
    this.sourceResource.count().then(count => this.sourcesCount = count);

    this.initSourceForm();
  }

  initSourceForm(): void {
    this.sourceForm = this.formBuilder.group({
      sourceName: new FormControl('', [Validators.required]),
      sourceStatus: new FormControl({value: 'PENDING_APPROVAL', disabled: true})
    });
  }

  resetForm(){
    this.sourceForm.reset();
    this.sourceForm.controls['sourceStatus'].reset('PENDING_APPROVAL');
  }

  private CallServiceForSources() {
    this.appService.getAllSources(
      null,
      null,
      null,
      null,
      null).subscribe(
      (value) => {
        this.sources = value;
        this.paginator.total = value.length;
        this.sourcesCount = value.length;
        this.selectedSource = value[0];
      },
      error => {
        console.log(error);
        this.toastr.error(error.statusText, 'Error Calling Service', {showCloseButton: true});
      }
    );
  }

  getStatusValue(_value: string): string {
    for (let i = 0; i <= configApp.statusOptions.length; i++) {
      if (configApp.statusOptions[i].value === _value) {
        return configApp.statusOptions[i].name;
      }
    }
  }

  rowClick(rowEvent) {
    this.selectedSource = rowEvent.row.item;
  }

  openUpdateModal(contentModal, item) {
    this.selectedSource = item;
    this.sourceForm.controls['sourceName'].patchValue(this.selectedSource.name);
    this.sourceForm.controls['sourceStatus'].patchValue(this.selectedSource.status);

    contentModal.show();
  }

  AddSource(contentModal, sourceFormValue) {
    this.sourceToAdd = new Source();
    this.sourceToAdd.status = 'PENDING_APPROVAL';
    this.sourceToAdd.statusCode = 100;
    this.sourceToAdd.name = sourceFormValue.sourceName;
    this.sourceToAdd.createdOn = moment(new Date()).format('YYYY-MM-DD');
    this.appService.addSource({
      'name': this.sourceToAdd.name,
      'createdOn': this.sourceToAdd.createdOn,
      'dateFormat': 'YYYY-MM-DD',
      'locale': 'es'
    }).subscribe(
      res => {
        this.toastr.success('Source Succesfully Created', 'Success', {showCloseButton: true});
        this.sourceToAdd = res;
        this.sources.push(this.sourceToAdd);

        this.sourceForm.reset();
        this.sourceForm.controls['sourceStatus'].reset('PENDING_APPROVAL');

        contentModal.hide();
      },
      (error: Response) => {
        console.log(error);
        this.toastr.error(error.statusText, 'Error Calling Service', {showCloseButton: true});
      }
    );
  }

  ChangeStatus(contentModal, action) {
    let body = {};
    if (action === 'reject') {
      this.selectedSource.rejectedOn = moment(new Date()).format('YYYY-MM-DD');
      body = {'rejectedOn': this.selectedSource.rejectedOn, 'dateFormat': 'YYYY-MM-DD', 'locale': 'es'};
    } else {
      this.selectedSource.approvedOn = moment(new Date()).format('YYYY-MM-DD');
      body = {'approvedOn': this.selectedSource.approvedOn, 'dateFormat': 'YYYY-MM-DD', 'locale': 'es'};
    }
    this.appService.changeStatusSource(this.selectedSource.id, body, action).subscribe(
      res => {
        this.selectedSource.id = res;
        const index = this.sources.findIndex(s => s.id === this.selectedSource.id);
        if (action === 'reject') {
          this.sources[index].status = 'REJECTED';
        } else {
          this.sources[index].status = 'APPROVED';
        }
        this.toastr.success('Source Succesfully Modified', 'Success', {showCloseButton: true});

        contentModal.hide();
      },
      (error: Response) => {
        console.log(error);
        this.toastr.error(error.statusText, 'Error Calling Service', {showCloseButton: true});
      }
    );
  }

  UpdateSource(contentModal, sourceFormValue) {
    let body = {};
    this.selectedSource.name = sourceFormValue.sourceName;
    this.selectedSource.updatedOn = moment(new Date()).format('YYYY-MM-DD');
    body = {
      'name': this.selectedSource.name,
      'updatedOn': this.selectedSource.updatedOn,
      'dateFormat': 'YYYY-MM-DD',
      'locale': 'es'
    };
    this.appService.updateSource(this.selectedSource.id, body).subscribe(
      res => {
        if (res.changes > 0) {
          this.toastr.success('Source Succesfully Modified', 'Success', {showCloseButton: true});
        } else {
          this.toastr.success('No Modify Needed', 'Success', {showCloseButton: true});
        }
        this.sourceForm.reset();
        this.sourceForm.controls['sourceStatus'].reset('PENDING_APPROVAL');

        contentModal.hide();
      },
      (error: Response) => {
        console.log(error);
        this.toastr.error(error.statusText, 'Error Calling Service', {showCloseButton: true});
      }
    );
  }

  DeleteSource(contentModal) {
    this.appService.removeSource(this.selectedSource.id).subscribe(
      res => {
        this.toastr.success('Source Succesfully Deleted', 'Success', {showCloseButton: true});
        this.sources.splice(this.sources.findIndex(c=> c.id == this.selectedSource.id),1);
        contentModal.hide();
      },
      (error) => {
        console.log(error);
        this.toastr.error(error.json().developerMessage, 'Error Deleting Source', {showCloseButton: true});
      }
    );
  }

  private handleError(error: Response) {
    this.toastr.clearAllToasts();
    this.toastr.error('An unknown error has occurred', null, {showCloseButton: true});
  }
}


