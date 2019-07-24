import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LicenseRtuMetaService {
  schemaUrl: string;
  exampleUrlBase: string;

  constructor(private http: HttpClient) {
    // tslint:disable-next-line:max-line-length
    this.schemaUrl = `assets/rtu-schema.json`;
    this.exampleUrlBase = `assets/exampledata/`;
  }

  getSchema(): any {
    return this
      .http
      .get(`${this.schemaUrl}`);
  }

  getSample(id: string): any {
    return this
      .http
      .get(`${this.exampleUrlBase}/${id}`)
  }
}
