import {Status} from "./status";

export class Source {
  id: number;
  name?: string;
  status?: string;
  statusCode?: number;
  createdOn?: string;
  rejectedOn?: string;
  approvedOn?: string;
  updatedOn?: string;

  constructor() {
  this.name = '';
  this.status = '';
  this.statusCode =  0;
  this.createdOn = '';
  }
}
