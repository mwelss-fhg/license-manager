import { TestBed } from '@angular/core/testing';

import { LicenseRtuMetaService } from './license-rtu-meta.service';

describe('LicenseRtuMetaService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: LicenseRtuMetaService = TestBed.get(LicenseRtuMetaService);
    expect(service).toBeTruthy();
  });
});
