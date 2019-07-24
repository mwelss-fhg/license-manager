import { BrowserModule } from '@angular/platform-browser';
import { NgModule, Injector } from '@angular/core';

import { LicenseRtuEditorComponent } from './license-rtu-editor/license-rtu-editor.component';
import { createCustomElement } from '@angular/elements';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatGridListModule } from '@angular/material';
import { MaterialDesignFrameworkModule } from '@earlyster/angular6-json-schema-form';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    LicenseRtuEditorComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatCardModule, MatToolbarModule, MatFormFieldModule, MatInputModule, MatButtonModule,
    MatGridListModule,
    MaterialDesignFrameworkModule,
    HttpClientModule
  ],
  providers: [],
  entryComponents: [LicenseRtuEditorComponent]

})
export class AppModule {

  constructor(private injector: Injector) {}

  ngDoBootstrap() {
    // using createCustomElement from angular package it will convert angular component to stander web component
    const el = createCustomElement(LicenseRtuEditorComponent, {
      injector: this.injector
    });
    // using built in the browser to create your own custome element name
    customElements.define('license-rtu-editor', el);
  }
 }
