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

package org.acumos.licensemanager.client.rtu;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.acumos.licensemanager.client.model.ILicenseRtuVerifier;
import org.acumos.licensemanager.client.model.LicenseRtuVerification;
import org.acumos.licensemanager.client.model.VerifyLicenseRequest;
import org.acumos.licensemanager.exceptions.RightToUseException;
import org.acumos.lum.handler.ApiCallback;
import org.acumos.lum.handler.ApiException;
import org.acumos.lum.handler.AssetUsageApi;
import org.acumos.lum.handler.JSON;
import org.acumos.lum.model.AssetUsageResponse;
import org.acumos.lum.model.AssetUseageRequestTopMixin;
import org.acumos.lum.model.PutAssetUsageRequest;
import org.acumos.lum.model.PutAssetUsageSuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LicenseRtuVerifier will verify that user or site has the RTU for a solution id and revision Id
 * for a specific action.
 *
 * <p>In Boreas release the action we only have one RTU for all actions.
 */
public final class LicenseRtuVerifier implements ILicenseRtuVerifier {

  /** Logger for any exceptions that happen while creating a RTU with CDS. */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String lumServer;

  /** @param lumServer base url for lum */
  public LicenseRtuVerifier(String lumServer) {
    this.lumServer = lumServer;
  }

  @Override
  public final CompletableFuture<LicenseRtuVerification> verifyRtu(
      final VerifyLicenseRequest request) throws RightToUseException {

    if (request == null) {
      throw new IllegalArgumentException("request is not defined");
    }
    if (request.getSolutionId() == null) {
      throw new IllegalArgumentException("request solution id is not defined");
    }
    if (request.getRevisionId() == null) {
      throw new IllegalArgumentException("request revisionId is not defined");
    }
    if (request.getLoggedInUserName() == null) {
      throw new IllegalArgumentException("logged in user is not defined");
    }

    CompletableFuture<LicenseRtuVerification> completableFuture =
        new CompletableFuture<LicenseRtuVerification>();

    AssetUsageApi api = new AssetUsageApi();

    api.getApiClient().setDebugging(true);
    api.getApiClient().setWriteTimeout(300000);
    api.getApiClient().setBasePath(lumServer);

    PutAssetUsageRequest putRequest = new PutAssetUsageRequest();

    putRequest.setUserId(request.getLoggedInUserName());
    AssetUseageRequestTopMixin usageReq = new AssetUseageRequestTopMixin();
    usageReq.setAssetUsageId(request.getUsageRequestId().toString());
    usageReq.setSwTagId(request.getRevisionId().toString());
    usageReq.setAction(request.getAction().getAction());
    putRequest.setAssetUsageReq(usageReq);
    putRequest.setSwMgtSystemId("Acumos");

    try {
      api.requestAssetUsageAsync(
          request.getUsageRequestId().toString(),
          putRequest,
          new ApiCallback<PutAssetUsageSuccessResponse>() {

            @Override
            public void onDownloadProgress(long arg0, long arg1, boolean arg2) {
              // Do nothing
            }

            @Override
            public void onFailure(
                ApiException responseException, int arg1, Map<String, List<String>> arg2) {
              boolean allowed = false; // failed to communicate to lum
              LicenseRtuVerification licenseRtuVerification = new LicenseRtuVerification(allowed);
              licenseRtuVerification.setApiException(responseException);
              // if 402 response then we need to have special handling to get AssetUsageResponse
              if (responseException.getCode() == 402) {
                AssetUsageResponse assetUsageResponse =
                    new JSON()
                        .deserialize(responseException.getResponseBody(), AssetUsageResponse.class);
                licenseRtuVerification.setLumDenialResponse(assetUsageResponse);
              }

              //  assetUsageResponse.getAssetUsage().();
              completableFuture.complete(licenseRtuVerification);
            }

            @Override
            public void onSuccess(
                PutAssetUsageSuccessResponse response, int arg1, Map<String, List<String>> arg2) {
              boolean allowed = response.getUsageEntitled();
              LicenseRtuVerification licenseRtuVerification = new LicenseRtuVerification(allowed);
              licenseRtuVerification.setLumResponse(response);
              completableFuture.complete(licenseRtuVerification);
              ;
            }

            @Override
            public void onUploadProgress(long arg0, long arg1, boolean arg2) {}
          });
    } catch (ApiException e) {
      LOGGER.error("lum verify use failed {}", e);
      completableFuture.completeExceptionally(e);
    }

    return completableFuture;
  }
}
