import {Injectable} from '@angular/core';
import {configApp} from './app.config';
import {Http, RequestOptions, Headers, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Source} from './interfaces/source';
import {ToastsManager} from 'ng2-toastr';

@Injectable()
export class AppService {

  constructor(private http: Http, private toastr: ToastsManager) { }


  public getAllCompanies(page, size, sort, sortproperty, filterField) {
    const url = configApp.serverUrl + '/companies';
    const params = new URLSearchParams();
    if (page != null && size != null) {
      params.set('page', page);
      const offset = page * size;
      params.set('offset', offset.toString());
      params.set('max', size);
    }
    if (sort != null && sortproperty != null) {
      params.set('sortOrder', sort);
      params.set('sortField', sortproperty);
    }
    if (filterField && filterField.name !== '') {
      params.set(filterField.name, filterField.value);
    }
    const options = new RequestOptions({headers: this.createHeaders().headers, params: params});

    return this.http.get(url, options)
      .map((response: Response) => {
        return response.json();
      });
  }

  public removeCompany(id) {
    const url = configApp.serverUrl + '/companies/' + id;
    const options = new RequestOptions({headers: this.createHeaders().headers});

    return this.http.delete(url, options)
      .map((response: Response) => {
        return response.json();
      });
  }

  public addCompany(company) {
    const url = configApp.serverUrl + '/companies';
    const options = new RequestOptions({headers: this.createHeaders().headers});

    return this.http.post(url, company, options)
      .map((response: Response) => {
        return response.json();
      });
  }

  public changeStatusCompany(id, company, action) {
    const url = configApp.serverUrl + '/companies/' + id;

    const options = new RequestOptions({headers: this.createHeaders().headers, params: {'command': action}});

    return this.http.post(url, company , options)
      .map((response: Response) => {
        return response.json();
      });

  }

  public updateCompany(id, company) {
    const url = configApp.serverUrl + '/companies/' + id;
    const options = new RequestOptions({headers: this.createHeaders().headers});

    return this.http.put(url, company, options)
      .map((response: Response) => {
        return response.json();
      });
  }

  public getAllSources(page, size, sort, sortproperty, filterField) {
    const url = configApp.serverUrl + '/sources';
    const params = new URLSearchParams();
    if (page != null && size != null) {
      params.set('page', page);
      const offset = page * size;
      params.set('offset', offset.toString());
      params.set('max', size);
    }
    if (sort != null && sortproperty != null) {
      params.set('sortOrder', sort);
      params.set('sortField', sortproperty);
    }
    if (filterField && filterField.name !== '') {
      params.set(filterField.name, filterField.value);
    }
    const options = new RequestOptions({headers: this.createHeaders().headers, params: params});

    return this.http.get(url, options)
      .map((response: Response) => {
        return response.json();
      });
  }

  public removeSource(id) {
    const url = configApp.serverUrl + '/sources/' + id;
    const options = new RequestOptions({headers: this.createHeaders().headers});

    return this.http.delete(url, options)
      .map((response: Response) => {
        return response.json();
      });
  }

  public addSource(source) {
    const url = configApp.serverUrl + '/sources';
    const options = new RequestOptions({headers: this.createHeaders().headers});

    return this.http.post(url, source, options)
      .map((response: Response) => {
        return response.json();
      });
  }

  public changeStatusSource(id, source, action) {
    const url = configApp.serverUrl + '/sources/' + id;

    const options = new RequestOptions({headers: this.createHeaders().headers, params: {'command': action}});

    return this.http.post(url, source , options)
      .map((response: Response) => {
        return response.json();
      });
  }

  public updateSource(id, source) {
    const url = configApp.serverUrl + '/sources/' + id;
    const options = new RequestOptions({headers: this.createHeaders().headers});

    return this.http.put(url, source, options)
      .map((response: Response) => {
        return response.json();
      });
  }

  createHeaders() {
    const headers = new Headers();
    headers.append('Content-Type', 'application/json');
    return new RequestOptions({headers: headers});
  }
}
