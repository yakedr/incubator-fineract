import {Source} from './source';

export class Company {
  id?: number;
  name?: string;
  ruc?: string;
  yearsOperating?: number;
  dateOperationsStart?: string;
  cantEmployees?: number;
  createdOn?: string;
  rejectedOn?: string;
  approvedOn?: string;
  updatedOn?: string;

  sourceName?: string;
  sourceId?: number;

  status?: string;
  statusCode?: number;

  constructor(){
    this.name = '';

  }
}
