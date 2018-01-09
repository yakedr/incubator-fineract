///<reference path="../../../node_modules/@angular/core/src/metadata/directives.d.ts"/>
import {Component, OnInit, ViewChild, ViewContainerRef, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {DataTable, DataTableResource} from 'angular-4-data-table-bootstrap-4';
import {AppService} from '../app.service';
import {configApp} from '../app.config';
import {Company} from '../interfaces/company';
import {ToastsManager} from 'ng2-toastr';
import * as moment from 'moment';
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CompanyComponent implements OnInit {

  companies = [];
  companyCount = 0;
  companyResource = null;
  @ViewChild(DataTable) companyTable: DataTable;
  selectedCompany: Company = null;
  companyToAdd: Company;
  companyForm: FormGroup;
  sources = [];
  dateModel;
  paginator = {
    pageTotal: configApp.paginator.limit,
    total: 0,
    currentPage: 1
  };

  constructor(private appService: AppService, private formBuilder: FormBuilder, public toastr: ToastsManager, vcr: ViewContainerRef) {
    this.toastr.setRootViewContainerRef(vcr);

  }

  ngOnInit() {
    this.CallServiceForCompanies();
    this.CallServiceForSources();
    this.companyResource = new DataTableResource(this.companies);
    this.companyResource.count().then(count => this.companyCount = count);

    this.initCompanyForm();

  }

  private initCompanyForm(): void {
    this.companyForm = this.formBuilder.group({
      companyName: new FormControl('', [Validators.required]),
      companyStatus: new FormControl({value: 'PENDING_APPROVAL', disabled: true}),
      ruc: new FormControl('', [Validators.required]),
      dateOperationsStart: new FormControl(null, [Validators.required]),
      cantEmployees: new FormControl('', [Validators.required]),
      source: new FormControl('', [Validators.required])
    });
  }

  resetForm(){
    this.companyForm.reset();
    this.companyForm.controls['companyStatus'].reset('PENDING_APPROVAL');
    this.dateModel = null;
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
      },
      error => {
        console.log(error);
      }
    );
  }

  private CallServiceForCompanies() {
    this.appService.getAllCompanies(
      null, // this.paginator.currentPage,
      null, // this.paginator.pageTotal,
      null,
      null,
      null).subscribe(
      (value) => {
        this.companies = value;
        this.paginator.total = value.length;
        this.companyCount = value.length;
        this.selectedCompany = value[0];
      },
      error => {
        console.log(error);
        this.toastr.error(error.statusText, 'Error Calling Service', {showCloseButton: true});
      }
    );
  }

  rowClick(rowEvent) {
    this.selectedCompany = rowEvent.row.item;

  }

  openUpdateModal(contentModal, item) {
    this.selectedCompany = item;
    this.companyForm.controls['companyName'].patchValue(this.selectedCompany.name);
    this.companyForm.controls['companyStatus'].patchValue(this.selectedCompany.status);
    this.companyForm.controls['ruc'].patchValue(this.selectedCompany.ruc);

    let dateOperStart = moment(this.selectedCompany.dateOperationsStart);
    this.dateModel = {year: dateOperStart.year(), month: dateOperStart.month() + 1, day: dateOperStart.date()};
    this.companyForm.controls['dateOperationsStart'].patchValue(this.dateModel);

    this.companyForm.controls['cantEmployees'].patchValue(this.selectedCompany.cantEmployees);

    let index = this.sources.findIndex(s => s.id == this.selectedCompany.sourceId);
    this.companyForm.controls['source'].patchValue(this.sources[index]);//TODO verify

    contentModal.show();
  }

  public AddCompany(contentModal, companyFormValue) {
    this.companyToAdd = new Company();
    this.companyToAdd.status = 'PENDING_APPROVAL';
    this.companyToAdd.statusCode = 100;
    this.companyToAdd.name = companyFormValue.companyName;
    this.companyToAdd.ruc = companyFormValue.ruc;
    this.companyToAdd.cantEmployees = companyFormValue.cantEmployees;
    this.companyToAdd.dateOperationsStart = this.dateModel.year + '-' + this.dateModel.month + '-' + this.dateModel.day;
    this.companyToAdd.sourceName = companyFormValue.source.name;
    this.companyToAdd.sourceId = companyFormValue.source.id;
    this.companyToAdd.createdOn = moment(new Date()).format('YYYY-MM-DD');

    const body = {
      'name': this.companyToAdd.name, 'ruc': this.companyToAdd.ruc,
      'cantEmployees': this.companyToAdd.cantEmployees,
      'dateOperationsStart': this.companyToAdd.dateOperationsStart,
      'sourceId': this.companyToAdd.sourceId,
      'createdOn': this.companyToAdd.createdOn, 'dateFormat': 'YYYY-MM-DD', 'locale': 'es'
    };

    this.appService.addCompany(body).subscribe(
      res => {
        this.toastr.success('Company Succesfully Created', 'Success', {showCloseButton: true});
        this.companyToAdd.id = res.id;
        this.companyToAdd.yearsOperating = res.yearsOperating;
        this.companies.push(this.companyToAdd);

        this.companyForm.reset();
        this.companyForm.controls['companyStatus'].reset('PENDING_APPROVAL');
        contentModal.hide();
      },
      (error: Response) => {
        console.log(error);
        this.toastr.error(error.statusText, 'Error Calling Service', {showCloseButton: true});
      }
    );
  }

  ChangeCompanyStatus(contentModal, action) {
    let body = {};
    if (action === 'reject') {
      this.selectedCompany.rejectedOn = moment(new Date()).format('YYYY-MM-DD');
      body = {'rejectedOn': this.selectedCompany.rejectedOn, 'dateFormat': 'YYYY-MM-DD', 'locale': 'es'};
    } else {
      this.selectedCompany.approvedOn = moment(new Date()).format('YYYY-MM-DD');
      body = {'approvedOn': this.selectedCompany.approvedOn, 'dateFormat': 'YYYY-MM-DD', 'locale': 'es'};
    }

    this.appService.changeStatusCompany(this.selectedCompany.id, body, action).subscribe(
      res => {
        this.selectedCompany.id = res;
        const index = this.companies.findIndex(s => s.id === this.selectedCompany.id);
        if (action === 'reject') {
          this.companies[index].status = 'REJECTED';
        } else {
          this.companies[index].status = 'APPROVED';
        }
        this.toastr.success('Company Succesfully Modified', 'Success', {showCloseButton: true});
        contentModal.hide();
      },
      (error: Response) => {
        console.log(error);
        this.toastr.error(error.statusText, 'Error Calling Service', {showCloseButton: true});
      }
    );
  }

  UpdateCompany(contentModal, formValue) {
    let body = {};
    this.selectedCompany.name = formValue.companyName;
    this.selectedCompany.ruc = formValue.ruc;
    this.selectedCompany.cantEmployees = formValue.cantEmployees;
    let currentOpStart = this.selectedCompany.dateOperationsStart;
    this.selectedCompany.dateOperationsStart = this.dateModel.year + '-' + this.dateModel.month + '-' + this.dateModel.day;

    let newYearOper = null;
    if(currentOpStart != this.selectedCompany.dateOperationsStart){
      newYearOper = moment(new Date()).diff(moment(this.selectedCompany.dateOperationsStart), 'years');
    }
    this.selectedCompany.sourceName = formValue.source.name;
    this.selectedCompany.sourceId = formValue.source.id;
    this.selectedCompany.updatedOn = moment(new Date()).format('YYYY-MM-DD');

    body = {
      'name': this.selectedCompany.name,
      'ruc': this.selectedCompany.ruc,
      'cantEmployees': this.selectedCompany.cantEmployees,
      'dateOperationsStart': this.selectedCompany.dateOperationsStart,
      'sourceId': this.selectedCompany.sourceId,
      'updatedOn': this.selectedCompany.updatedOn,
      'dateFormat': 'YYYY-MM-DD',
      'locale': 'es'
    };
    this.appService.updateCompany(this.selectedCompany.id, body).subscribe(
      res => {
        if (res.changes > 0) {
          this.toastr.success('Company Succesfully Modified', 'Success', {showCloseButton: true});
          if(newYearOper != null)
            this.selectedCompany.yearsOperating = newYearOper;
        } else {
          this.toastr.success('No Modify Needed', 'Success', {showCloseButton: true});
        }
        this.companyForm.reset();
        this.companyForm.controls['companyStatus'].reset('PENDING_APPROVAL');

        contentModal.hide();
      },
      (error: Response) => {
        console.log(error);
        this.toastr.error(error.statusText, 'Error Calling Service', {showCloseButton: true});
      }
    );
  }

  DeleteCompany(contentModal) {
    this.appService.removeCompany(this.selectedCompany.id).subscribe(
      res => {
        this.toastr.success('Company Succesfully Deleted', 'Success', {showCloseButton: true});
        this.companies.splice(this.companies.findIndex(c=> c.id == this.selectedCompany.id),1);
        contentModal.hide();
      },
      (error) => {
        console.log(error);
        this.toastr.error(error.json().developerMessage, 'Error Deleting Company', {showCloseButton: true});
      }
    );
  }

  private handleError(error: Response) {
    this.toastr.clearAllToasts();
    this.toastr.error('An unknown error has occurred', null, {showCloseButton: true});
  }
}


