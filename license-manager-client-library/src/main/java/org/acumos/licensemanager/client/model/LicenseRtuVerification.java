/*-
 * ===============LICENSE_START================================================
 * Acumos Apache-2.0
 * ============================================================================
 * Copyright (C) 2019 Nordix Foundation.
 * ============================================================================
 * This Acumos software file is distributed by Nordix Foundation
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ===============LICENSE_END==================================================
 */

package org.acumos.licensemanager.client.model;

import org.acumos.lum.handler.ApiException;
import org.acumos.lum.model.AssetUsageDenialAssetUsageDenial;
import org.acumos.lum.model.AssetUsageResponse;
import org.acumos.lum.model.PutAssetUsageSuccessResponse;

/** Verified that there is a right to use for specified userId and solutionID. */
public class LicenseRtuVerification implements ILicenseRtuVerification {

  private ApiException apiException;
  private PutAssetUsageSuccessResponse lumResponse;
  private AssetUsageResponse lumDenialResponse;
  private final boolean allowedToUse;

  public LicenseRtuVerification(boolean isAllowed) {
    allowedToUse = isAllowed;
  }

  @Override
  public final boolean isAllowed() {
    return allowedToUse;
  }

  /** @param responseException if there is an LUM api exception capture that here */
  public void setApiException(ApiException responseException) {
    apiException = responseException;
  }

  /** @return the apiException LUM api exception object */
  public ApiException getApiException() {
    return apiException;
  }

  /** @param response original response from LUM */
  public void setLumResponse(PutAssetUsageSuccessResponse response) {
    lumResponse = response;
  }

  /** @return the lumResponse */
  public PutAssetUsageSuccessResponse getLumResponse() {
    return lumResponse;
  }

  /** @return the lumResponse */
  public AssetUsageResponse getLumDenialResponse() {
    return lumDenialResponse;
  }

  /** @param response original response from LUM */
  public void setLumDenialResponse(AssetUsageResponse response) {
    lumDenialResponse = response;
  }

  public String getDenialReason() {
    // if api exception not 402 code
    if (getLumDenialResponse() == null) {
      return getApiException().getLocalizedMessage();
    } else {
      String denialReasons = "";
      for (AssetUsageDenialAssetUsageDenial denial :
          getLumDenialResponse().getAssetUsage().getAssetUsageDenial()) {
        denialReasons += denial.getDenialReason() + ',';
      }
      return denialReasons.substring(0, denialReasons.length() - 1);
    }
  }
}
