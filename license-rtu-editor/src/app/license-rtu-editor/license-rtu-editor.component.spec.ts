import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LicenseRtuEditorComponent } from './license-rtu-editor.component';

describe('LicenseRtuEditorComponent', () => {
  let component: LicenseRtuEditorComponent;
  let fixture: ComponentFixture<LicenseRtuEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LicenseRtuEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LicenseRtuEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
