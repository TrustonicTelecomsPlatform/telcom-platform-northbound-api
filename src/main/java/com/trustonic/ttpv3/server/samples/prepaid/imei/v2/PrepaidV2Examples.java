/*
 * Copyright (c) 2021-2024 TRUSTONIC LIMITED
 *   All rights reserved
 *
 * The present software is the confidential and proprietary information of
 * TRUSTONIC LIMITED. You shall not disclose the present software and shall
 * use it only in accordance with the terms of the license agreement you
 * entered into with TRUSTONIC LIMITED. This software may be subject to
 * export or import laws in certain countries.
 */
package com.trustonic.ttpv3.server.samples.prepaid.imei.v2;

import com.trustonic.ttpv3.client.client.PrepaidOperationsV2;
import com.trustonic.ttpv3.client.client.impl.PrepaidOperationsV2Impl;
import com.trustonic.ttpv3.client.enums.CommonEnums;
import com.trustonic.ttpv3.client.model.prepaid.v2.request.*;
import com.trustonic.ttpv3.client.model.prepaid.v2.request.item.*;
import com.trustonic.ttpv3.client.model.prepaid.v2.response.*;
import com.trustonic.ttpv3.client.model.prepaid.v2.response.item.*;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class describes examples and usage of V2 Trustonic NorthBound API
 */
class PrepaidV2Examples {
    private static final Properties props;
    private static final PrepaidOperationsV2 prepaidV2Operations;
    private static final Logger log = Logger.getLogger(PrepaidV2Examples.class.getSimpleName());
    private static final String PROP_TENANT_NAME = "tenant_name";
    private static final String PROP_API_KEY = "api_key";
    private static final String PROP_BASE_FOLDER_NAME = "base-folder";

    static {
        props = loadProperties();
        try {
            LogManager.getLogManager().readConfiguration(PrepaidV2Examples.class.getClassLoader().getResource("logging-client.properties").openStream());
        } catch (Throwable ex) {
            log.severe("[V2] Logging file error " + Arrays.toString(ex.getStackTrace()));
        }
        log.info("[V2] Configured Properties for project " + props);
        prepaidV2Operations = PrepaidOperationsV2Impl.getInstance(props.getProperty(PROP_TENANT_NAME));
    }

    public static void main(String[] args) {
        if (null == prepaidV2Operations || (null == props || 0 == props.size())) {
            log.severe("[V2] Mandatory data not exist. Existing program");
            return;
        }

        // This method contains sample code to query device's information for V2 API
        queryDevicesExampleV2();

        // This method contains sample code for lock message device's for V2 API
        lockMessageDevicesExampleV2();

        // This method contains sample code for lock device's for V2 API
        lockDevicesExampleV2();

        // This method contains sample code for notify device's for V2 API
        notifyDevicesExampleV2();

        // This method contains sample code for reload device's for V2 API
        reloadDevicesExampleV2();

        // This method contains sample code for update IMEI2 for device's for V2 API
        updateDevicesExampleV2();

        // This method contains sample code for unlock device's for V2 API
        unlockDevicesExampleV2();

        // This method contains sample code for release device's for V2 API
        releaseDevicesExampleV2();

        // This method contains sample code for upload prepaid device's for V2 API
        uploadDevicesExampleV2();

        // This method contains sample code for activating prepaid service for device's for V2 API
        activateServiceDevicesExampleV2();

        // This method contains sample code for de-activating prepaid service for device's for V2 API
        deActivateServiceDevicesExampleV2();
    }

    private static void deActivateServiceDevicesExampleV2() {
        String csvFile =
                props.getProperty(PROP_BASE_FOLDER_NAME)
                        + File.separator
                        + "DeActivateServiceDevices_V2.csv";
        List<PrepaidDeActivateV2ServiceItem> deActivateServiceDevicesList = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidDeActivateV2ServiceItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidDeActivateV2ServiceItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID"));
                }
                if (null != record.get("ADDITIONAL_REMARKS")
                        && 0 < record.get("ADDITIONAL_REMARKS").length()) {
                    imeiInfo.setAdditionalRemarks(record.get("ADDITIONAL_REMARKS"));
                }
                if (null != record.get("DEACTIVATE_REASON")
                        && 0 < record.get("DEACTIVATE_REASON").length()) {
                    imeiInfo.setDeactivateReason(
                            CommonEnums.DeactiveReasonV2.valueOf(
                                    record.get("DEACTIVATE_REASON").toLowerCase(Locale.ROOT)));
                }
                deActivateServiceDevicesList.add(imeiInfo);
            }
        } catch (Exception ex) {
            log.severe(
                    "[V2] Exception occurred while forming deactivate service for device. Exception " + ex);
        }
        if (0 < deActivateServiceDevicesList.size()) {
            // Step-1 Getting List of IMEI details to be deactivated service and forming the request.
            PrepaidDeActivateServicesV2Request prepaidActivateServiceDevicesRequest =
                    PrepaidDeActivateServicesV2Request.builder()
                            .deviceList(deActivateServiceDevicesList)
                            .build();
            List<String> noRespDevicesList =
                    prepaidActivateServiceDevicesRequest.getDeviceList().stream()
                            .map(PrepaidDeActivateV2ServiceItem::getDeviceUid)
                            .collect(Collectors.toList());

            // Step-2 Creating interfaces and do the deactivate service  devices operation
            ResponseEntity<PrepaidDeActivateServicesV2Response> deActivateServiceDevicesRespList =
                    prepaidV2Operations.deActivateServiceV2(
                            props.getProperty(PROP_API_KEY), prepaidActivateServiceDevicesRequest);

            // Step-3 Response Object processing. This describes responseCode, body parameters and
            // response object
            log.info(
                    "[V2] Prepaid Deactivating Service Devices Response Code "
                            + deActivateServiceDevicesRespList.getStatusCode());
            log.info(
                    "[V2] Prepaid Deactivating Service  Response Headers "
                            + deActivateServiceDevicesRespList.getHeaders());
            if (null != deActivateServiceDevicesRespList.getBody()
                    && HttpStatus.OK.equals(deActivateServiceDevicesRespList.getStatusCode())) {
                PrepaidDeActivateServicesV2Response resp = deActivateServiceDevicesRespList.getBody();
                log.info("[V2] Prepaid Deactivating Service  Devices Response Body " + resp);
                if (!resp.getServiceOrderStatus().equalsIgnoreCase("FAILED")) {
                    getServiceUploadStatus(resp.getServiceOrderId(), noRespDevicesList);
                }
            }
            noRespDevicesList.forEach(
                    device ->
                            log.info(
                                    "[V2] No Processing or Processing Failed for Prepaid Deactivate Service for Device "
                                            + device));
        } else {
            log.info("[V2] No devices for Prepaid Deactivate Service");
        }
    }

    private static void activateServiceDevicesExampleV2() {
        String csvFile =
                props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "ActivateServiceDevices_V2.csv";
        List<PrepaidActivateV2ServiceItem> activateServiceDevicesList = new ArrayList<>();
        List<String> existingFieldNames =
                new ArrayList<>(
                        List.of(
                                "DEVICE_ID",
                                "SERVICE_TENURE",
                                "IMEI2",
                                "EXPIRY",
                                "RELOAD_DAYS",
                                "CONTRACT_ID",
                                "ASSIGNED_CONTRACT_TIME",
                                "RELOAD_TIME",
                                "RELOAD_TIME_UNIT"));

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidActivateV2ServiceItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidActivateV2ServiceItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("SERVICE_TENURE") && 0 < record.get("SERVICE_TENURE").length()) {
                    imeiInfo.setServiceTenureInMonths(record.get("SERVICE_TENURE"));
                }
                if (null != record.get("RELOAD_DAYS") && 0 < record.get("RELOAD_DAYS").length()) {
                    imeiInfo.setReloadDays(Integer.valueOf(record.get("RELOAD_DAYS")));
                }
                if (null != record.get("EXPIRY") && 0 < record.get("EXPIRY").length()) {
                    imeiInfo.setExpiry(Integer.valueOf(record.get("EXPIRY")));
                }
                if (null != record.get("IMEI2") && 0 < record.get("IMEI2").length()) {
                    imeiInfo.setIMEI2(record.get("IMEI2"));
                }
                Map<String, String> additionalProps = new HashMap<>();
                for (Map.Entry<String, String> entryVal : record.toMap().entrySet()) {
                    if (null != entryVal.getValue()
                            && 0 < entryVal.getValue().length()
                            && (!existingFieldNames.contains(entryVal.getKey().toUpperCase(Locale.ROOT)))) {
                        additionalProps.put(entryVal.getKey(), entryVal.getValue());
                    }
                }
                if (0 < additionalProps.size()) {
                    imeiInfo.setAdditionalInfo(additionalProps);
                }
                log.info("[V2] Prepaid Activate Service Device request " + imeiInfo.toString());
                activateServiceDevicesList.add(imeiInfo);
            }
        } catch (Exception ex) {
            log.severe(
                    "[V2] Exception occurred while forming activate service for device. Exception " + ex);
        }
        if (0 < activateServiceDevicesList.size()) {
            // Step-1 Getting List of IMEI details to be activated service and forming the request.
            PrepaidActivateServicesV2Request prepaidActivateServiceDevicesRequest =
                    PrepaidActivateServicesV2Request.builder().deviceList(activateServiceDevicesList).build();
            List<String> noRespDevicesList =
                    prepaidActivateServiceDevicesRequest.getDeviceList().stream()
                            .map(PrepaidActivateV2ServiceItem::getDeviceUid)
                            .collect(Collectors.toList());

            // Step-2 Creating interfaces and do the activate service  devices operation
            ResponseEntity<PrepaidActivateServicesV2Response> activateServiceDevicesRespList =
                    prepaidV2Operations.activateServiceV2(
                            props.getProperty(PROP_API_KEY), prepaidActivateServiceDevicesRequest);

            // Step-3 Response Object processing. This describes responseCode, body parameters and
            // response object
            log.info(
                    "[V2] Prepaid Upload Active Devices Response Code "
                            + activateServiceDevicesRespList.getStatusCode());
            log.info(
                    "[V2] Prepaid Upload Active Devices Response Headers "
                            + activateServiceDevicesRespList.getHeaders());
            if (null != activateServiceDevicesRespList.getBody()
                    && HttpStatus.OK.equals(activateServiceDevicesRespList.getStatusCode())) {
                PrepaidActivateServicesV2Response resp = activateServiceDevicesRespList.getBody();
                log.info("Prepaid Activate Service Devices Response Body " + resp);
                if (!resp.getServiceOrderStatus().equalsIgnoreCase("FAILED")) {
                    getServiceUploadStatus(resp.getServiceOrderId(), noRespDevicesList);
                }
            }
            noRespDevicesList.forEach(
                    device ->
                            log.severe(
                                    "[V2] No Processing or Processing Failed for Prepaid Activate Service for Device "
                                            + device));
        } else {
            log.info("[V2] No devices for Prepaid Activate Service for Device");
        }
    }

    private static void getServiceUploadStatus(String uploadId, List<String> devices) {
        boolean isProcessingInProgress = true;
        PrepaidServiceStatusV2Request uploadServiceStatusReq;
        ResponseEntity<PrepaidServiceStatusV2Response> uploadServiceStatusResp;
        while (isProcessingInProgress) {
            uploadServiceStatusReq =
                    PrepaidServiceStatusV2Request.builder().serviceOrderId(uploadId).build();
            uploadServiceStatusResp =
                    prepaidV2Operations.getServiceStatusV2(
                            props.getProperty(PROP_API_KEY), uploadServiceStatusReq);
            log.info(
                    "[V2] Prepaid Upload Service Status Response Code "
                            + uploadServiceStatusResp.getStatusCode());
            log.info(
                    "[V2] Prepaid Upload Service Status Response Response Headers "
                            + uploadServiceStatusResp.getHeaders());
            if (null != uploadServiceStatusResp.getBody()) {
                PrepaidServiceStatusV2Response uploadItemResp = uploadServiceStatusResp.getBody();
                log.info(
                        "[V2] Prepaid Upload Service Status Response Response Data " + uploadItemResp);
                if (null != uploadItemResp.getOrderStatus()
                        && (!uploadItemResp.getOrderStatus().equalsIgnoreCase("INPROGRESS"))) {
                    log.info("[V2] Service Status Result is " + uploadItemResp.getOrderStatus());
                    isProcessingInProgress = false;
                    log.info(
                            "[V2] Prepaid Upload Service Status Device List is "
                                    + uploadItemResp.getDeviceList());
                    for (PrepaidServiceStatusV2ResponseItem item : uploadItemResp.getDeviceList()) {
                        log.info(
                                "[V2] Prepaid Upload Service Status Result for Device "
                                        + item.getDeviceUid()
                                        + " Info "
                                        + item);
                        devices.remove(item.getDeviceUid());
                    }
                }
            }
        }
    }

    private static void updateDevicesExampleV2() {
        String csvFile =
                props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "UpdateDevices_V2.csv";
        List<PrepaidUpdateDeviceV2RequestItem> updateDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidUpdateDeviceV2RequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidUpdateDeviceV2RequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("IMEI2") && 0 < record.get("IMEI2").length()) {
                    imeiInfo.setImei2(record.get("IMEI2"));
                }
                updateDeviceItems.add(imeiInfo);
                log.info("[V2] Prepaid Update Device request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe(
                    "[V2] Exception occurred while forming update device request. Exception " + ex);
        }
        if (0 < updateDeviceItems.size()) {
            // Step-1 Getting Update of IMEI details for forming the request.
            PrepaidUpdateDevicesV2Request prepaidUpdateDevicesRequest =
                    PrepaidUpdateDevicesV2Request.builder().deviceList(updateDeviceItems).build();
            List<String> noRespDevicesList =
                    prepaidUpdateDevicesRequest.getDeviceList().stream()
                            .map(PrepaidUpdateDeviceV2RequestItem::getDeviceUid)
                            .collect(Collectors.toList());

            // Step-2 Perform update device operation for devices
            ResponseEntity<PrepaidUpdateDevicesV2Response> updatePrepaidDevices =
                    prepaidV2Operations.updateDevicesV2(
                            props.getProperty(PROP_API_KEY), prepaidUpdateDevicesRequest);

            // Step-3 Response Object processing. This describes responseCode, body parameters and
            // response object
            log.info(
                    "[V2] Prepaid Update device Response Code " + updatePrepaidDevices.getStatusCode());
            log.info(
                    "[V2] Prepaid Update Message Response Headers " + updatePrepaidDevices.getHeaders());
            if (null != updatePrepaidDevices.getBody()) {
                PrepaidUpdateDevicesV2Response resp = updatePrepaidDevices.getBody();
                log.info("[V2] Prepaid Update device Response Body " + resp);
                if (null != resp.getBatchActionId() && resp.getBatchActionStatus().equals("INPROGRESS")) {
                    getBatchActionStatus(resp.getBatchActionId(), noRespDevicesList);
                } else {
                    for (PrepaidUpdateDeviceV2ResponseItem item : resp.getDeviceResponseList()) {
                        log.info(
                                "[V2] Prepaid Update Device for " + item.getDeviceUid() + " Response Is " + item);
                        noRespDevicesList.remove(item.getDeviceUid());
                    }
                    noRespDevicesList.forEach(
                            device ->
                                    log.severe("[V2] No Processing for Prepaid Update Device " + device));
                }
            }
        } else {
            log.info("No devices for Prepaid Update Device");
        }
    }

    private static void unlockDevicesExampleV2() {
        String csvFile =
                props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "UnlockDevices_V2.csv";
        List<PrepaidUnLockDeviceV2RequestItem> unlockDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidUnLockDeviceV2RequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidUnLockDeviceV2RequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("UNLOAD_DAYS") && 0 < record.get("UNLOAD_DAYS").length()) {
                    imeiInfo.setValidityDays(Integer.valueOf(record.get("UNLOAD_DAYS")));
                }
                if (null != record.get("NEXT_DUE") && 0 < record.get("NEXT_DUE").length()) {
                    imeiInfo.setNextDue(Long.valueOf(record.get("NEXT_DUE")));
                }
                unlockDeviceItems.add(imeiInfo);
                log.info("[V2] Prepaid Unlock Device request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe(
                    "[V2] Exception occurred while forming unlock device request. Exception " + ex);
        }
        if (0 < unlockDeviceItems.size()) {
            // Step-1 Getting Unlock of IMEI details for forming the request.
            PrepaidUnLockDevicesV2Request prepaidUnlockDevicesRequest =
                    PrepaidUnLockDevicesV2Request.builder().unLockList(unlockDeviceItems).build();

            List<String> noRespDevicesList =
                    prepaidUnlockDevicesRequest.getUnLockList().stream()
                            .map(PrepaidUnLockDeviceV2RequestItem::getDeviceUid)
                            .collect(Collectors.toList());

            // Step-2 Perform unlock device operation for devices
            ResponseEntity<PrepaidUnLockDevicesV2Response> unLockPrepaidDevices =
                    prepaidV2Operations.unlockPrepaidDevicesV2(
                            props.getProperty(PROP_API_KEY), prepaidUnlockDevicesRequest);

            // Step-3 Response Object processing. This describes responseCode, body parameters and
            // response object
            log.info(
                    "[V2] Prepaid Unlock device Response Code " + unLockPrepaidDevices.getStatusCode());
            log.info(
                    "[V2] Prepaid Unlock Message Response Headers " + unLockPrepaidDevices.getHeaders());
            if (null != unLockPrepaidDevices.getBody()) {
                PrepaidUnLockDevicesV2Response resp = unLockPrepaidDevices.getBody();
                log.info("[V2] Prepaid Unlock device Response Body " + resp);
                if (null != resp.getBatchActionId() && resp.getBatchActionStatus().equals("INPROGRESS")) {
                    getBatchActionStatus(resp.getBatchActionId(), noRespDevicesList);
                } else {
                    for (PrepaidUnLockDeviceV2ResponseItem item : resp.getUnlockResponseList()) {
                        log.info(
                                "[V2] Prepaid Unlock Device for " + item.getDeviceUid() + " Response Is " + item);
                        noRespDevicesList.remove(item.getDeviceUid());
                    }
                    noRespDevicesList.forEach(
                            device ->
                                    log.severe("[V2] No Processing or Processing Failed for Prepaid Unlock Device " + device));
                }
            }
        } else {
            log.info("[V2] No devices for Prepaid Unlock Device");
        }
    }

    private static void reloadDevicesExampleV2() {
        String csvFile =
                props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "ReloadDevices_V2.csv";
        List<PrepaidReloadDeviceV2RequestItem> reloadDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidReloadDeviceV2RequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidReloadDeviceV2RequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("RELOAD_DAYS") && 0 < record.get("RELOAD_DAYS").length()) {
                    imeiInfo.setReloadDays(Integer.valueOf(record.get("RELOAD_DAYS")));
                }
                if (null != record.get("NEXT_DUE") && 0 < record.get("NEXT_DUE").length()) {
                    imeiInfo.setNextDue(Long.valueOf(record.get("NEXT_DUE")));
                }
                reloadDeviceItems.add(imeiInfo);
                log.info("[V2] Prepaid Reload Device request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe(
                    "[V2] Exception occurred while forming reload device request. Exception " + ex);
        }
        if (0 < reloadDeviceItems.size()) {
            // Step-1 Getting Reload of IMEI details for forming the request.
            PrepaidReloadDevicesV2Request prepaidReloadDevicesRequest =
                    PrepaidReloadDevicesV2Request.builder().reloadList(reloadDeviceItems).build();

            List<String> noRespDevicesList =
                    prepaidReloadDevicesRequest.getReloadList().stream()
                            .map(PrepaidReloadDeviceV2RequestItem::getDeviceUid)
                            .collect(Collectors.toList());

            // Step-2 Perform reload device operation for devices
            ResponseEntity<PrepaidReloadDevicesV2Response> reloadPrepaidDevices =
                    prepaidV2Operations.reloadPrepaidDevicesV2(
                            props.getProperty(PROP_API_KEY), prepaidReloadDevicesRequest);

            // Step-3 Response Object processing. This describes responseCode, body parameters and
            // response object
            log.info(
                    "[V2] Prepaid Reload device Response Code " + reloadPrepaidDevices.getStatusCode());
            log.info(
                    "[V2] Prepaid Reload Message Response Headers " + reloadPrepaidDevices.getHeaders());
            if (null != reloadPrepaidDevices.getBody()) {
                PrepaidReloadDevicesV2Response resp = reloadPrepaidDevices.getBody();
                log.info("[V2] Prepaid Reload device Response Body " + resp);
                if (null != resp.getBatchActionId() && resp.getBatchActionStatus().equals("INPROGRESS")) {
                    getBatchActionStatus(resp.getBatchActionId(), noRespDevicesList);
                } else {
                    for (PrepaidReloadDeviceV2ResponseItem item : resp.getReloadResponseList()) {
                        log.info(
                                "[V2] Prepaid Reload Device for " + item.getDeviceUid() + " Response Is " + item);
                        noRespDevicesList.remove(item.getDeviceUid());
                    }
                    noRespDevicesList.forEach(
                            device ->
                                    log.severe("[V2] No Processing or Processing Failed for Prepaid Reload Device " + device));
                }
            }
        } else {
            log.info("[V2] No devices for Prepaid Reload Device");
        }
    }

    private static void releaseDevicesExampleV2() {
        String csvFile =
                props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "ReleaseDevices_V2.csv";
        List<PrepaidReleaseDeviceV2RequestItem> releaseDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidReleaseDeviceV2RequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidReleaseDeviceV2RequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("REASON") && 0 < record.get("REASON").length()) {
                    imeiInfo.setReason(record.get("REASON"));
                }
                releaseDeviceItems.add(imeiInfo);
                log.info("[V2] Prepaid Release Device request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe(
                    "[V2] Exception occurred while forming release device request. Exception " + ex);
        }
        if (0 < releaseDeviceItems.size()) {
            // Step-1 Getting Release of IMEI details for forming the request.
            PrepaidReleaseDevicesV2Request prepaidReleaseDevicesRequest =
                    PrepaidReleaseDevicesV2Request.builder().deviceReleaseList(releaseDeviceItems).build();
            List<String> noRespDevicesList =
                    prepaidReleaseDevicesRequest.getDeviceReleaseList().stream()
                            .map(PrepaidReleaseDeviceV2RequestItem::getDeviceUid)
                            .collect(Collectors.toList());

            // Step-2 Perform release device operation for devices
            ResponseEntity<PrepaidReleaseDevicesV2Response> releasePrepaidDevices =
                    prepaidV2Operations.releasePrepaidDevicesV2(
                            props.getProperty(PROP_API_KEY), prepaidReleaseDevicesRequest);

            // Step-3 Response Object processing. This describes responseCode, body parameters and
            // response object
            log.info(
                    "[V2] Prepaid Release device Response Code " + releasePrepaidDevices.getStatusCode());
            log.info(
                    "[V2] Prepaid Release Message Response Headers " + releasePrepaidDevices.getHeaders());
            if (null != releasePrepaidDevices.getBody()) {
                PrepaidReleaseDevicesV2Response resp = releasePrepaidDevices.getBody();
                log.info("Prepaid Release device Response Body " + resp);
                if (null != resp.getBatchActionId() && resp.getBatchActionStatus().equals("INPROGRESS")) {
                    getBatchActionStatus(resp.getBatchActionId(), noRespDevicesList);
                } else {
                    for (PrepaidReleaseDeviceV2ResponseItem item : resp.getReleaseResponseList()) {
                        log.info(
                                "[V2] Prepaid Release Device for " + item.getDeviceUid() + " Response Is " + item);
                        noRespDevicesList.remove(item.getDeviceUid());
                    }
                    noRespDevicesList.forEach(
                            device ->
                                    log.severe(
                                            "[V2] No Processing or Processing Failed for Prepaid Release Device for " + device));
                }
            }
        } else {
            log.info("No devices for Prepaid Release Device");
        }
    }

    private static void notifyDevicesExampleV2() {
        String csvFile =
                props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "NotifyDevices_V2.csv";
        List<PrepaidNotifyDeviceV2RequestItem> notifyDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidNotifyDeviceV2RequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidNotifyDeviceV2RequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("NOTIFICATION_TITLE")
                        && 0 < record.get("NOTIFICATION_TITLE").length()) {
                    imeiInfo.setNotificationTitle(record.get("NOTIFICATION_TITLE"));
                }
                if (null != record.get("NOTIFICATION_CONTENT")
                        && 0 < record.get("NOTIFICATION_CONTENT").length()) {
                    imeiInfo.setNotificationContent(record.get("NOTIFICATION_CONTENT"));
                }
                if (null != record.get("MSG_TEMPLATE_ID") && 0 < record.get("MSG_TEMPLATE_ID").length()) {
                    imeiInfo.setMessageTemplateId(record.get("MSG_TEMPLATE_ID"));
                }
                if (null != record.get("NOTIFICATION_TYPE")
                        && 0 < record.get("NOTIFICATION_TYPE").length()) {
                    imeiInfo.setNotificationType(
                            PrepaidNotifyDeviceV2RequestItem.NotificationType.valueOf(
                                    record.get("NOTIFICATION_TYPE").toLowerCase(Locale.ROOT)));
                }
                notifyDeviceItems.add(imeiInfo);
                log.info("[V2] Prepaid Notify Device request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe(
                    "[V2] Exception occurred while forming notify device request. Exception " + ex);
        }
        if (0 < notifyDeviceItems.size()) {
            // Step-1 Getting List of IMEI details to send notify device request and forming the
            PrepaidNotifyDevicesV2Request prepaidNotifyDevicesRequest =
                    PrepaidNotifyDevicesV2Request.builder().messageList(notifyDeviceItems).build();

            List<String> noRespDevicesList =
                    prepaidNotifyDevicesRequest.getMessageList().stream()
                            .map(PrepaidNotifyDeviceV2RequestItem::getDeviceUid)
                            .collect(Collectors.toList());

            // Step-2 Perform notify device operation for devices
            ResponseEntity<PrepaidNotifyDevicesV2Response> notifyPrepaidDevices =
                    prepaidV2Operations.notifyPrepaidDevicesV2(
                            props.getProperty(PROP_API_KEY), prepaidNotifyDevicesRequest);

            // Step-3 Response Object processing. This describes responseCode, body parameters and
            // response object
            log.info(
                    "[V2] Prepaid Notify device Response Code " + notifyPrepaidDevices.getStatusCode());
            log.info(
                    "[V2] Prepaid Notify Message Response Headers " + notifyPrepaidDevices.getHeaders());
            if (null != notifyPrepaidDevices.getBody()) {
                PrepaidNotifyDevicesV2Response resp = notifyPrepaidDevices.getBody();
                log.info("[V2] Prepaid Notify device Response Body " + resp);
                if (null != resp.getBatchActionId() && resp.getBatchActionStatus().equals("INPROGRESS")) {
                    getBatchActionStatus(resp.getBatchActionId(), noRespDevicesList);
                } else {
                    for (PrepaidNotifyDeviceV2ResponseItem item : resp.getMessageResponseList()) {
                        log.info(
                                "[V2] Prepaid Notify Device for " + item.getDeviceUid() + " Response Is " + item);
                        noRespDevicesList.remove(item.getDeviceUid());
                    }
                    noRespDevicesList.forEach(
                            device ->
                                    log.severe("[V2] No Processing or Processing Failed for Query Device for Device " + device));
                }
            }
        } else {
            log.info("[V2] No devices for Prepaid Notify Device");
        }
    }

    private static void lockDevicesExampleV2() {
        String csvFile =
                props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "LockDevices_V2.csv";
        List<PrepaidLockDeviceV2RequestItem> lockDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidLockDeviceV2RequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidLockDeviceV2RequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("LOCK_MESSAGE_TITLE")
                        && 0 < record.get("LOCK_MESSAGE_TITLE").length()) {
                    imeiInfo.setLockMsgTitle(record.get("LOCK_MESSAGE_TITLE"));
                }
                if (null != record.get("LOCK_MESSAGE_CONTENT")
                        && 0 < record.get("LOCK_MESSAGE_CONTENT").length()) {
                    imeiInfo.setLockMsgContent(record.get("LOCK_MESSAGE_CONTENT"));
                }
                if (null != record.get("LOCK_TYPE") && 0 < record.get("LOCK_TYPE").length()) {
                    imeiInfo.setLockType(CommonEnums.DeviceLockType.valueOf(record.get("LOCK_TYPE")));
                }
                if (null != record.get("LOCK_DURATION") && 0 < record.get("LOCK_DURATION").length()) {
                    imeiInfo.setLockDuration(Integer.valueOf(record.get("LOCK_DURATION")));
                }
                if (null != record.get("LOCK_MESSAGE_CONTENT")
                        && 0 < record.get("LOCK_MESSAGE_CONTENT").length()) {
                    imeiInfo.setLockMsgContent(record.get("LOCK_MESSAGE_CONTENT"));
                }
                lockDeviceItems.add(imeiInfo);
                log.info("Prepaid Lock Device request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe(
                    "[V2] Exception occurred while forming lock message request. Exception " + ex);
        }
        if (0 < lockDeviceItems.size()) {
            // Step-1 Getting List of IMEI details to send lock request and forming the request.
            PrepaidLockDevicesV2Request prepaidLockDevicesRequest =
                    PrepaidLockDevicesV2Request.builder().lockList(lockDeviceItems).build();

            List<String> noRespDevicesList =
                    prepaidLockDevicesRequest.getLockList().stream()
                            .map(PrepaidLockDeviceV2RequestItem::getDeviceUid)
                            .collect(Collectors.toList());

            // Step-2 Perform lock device operation for devices
            ResponseEntity<PrepaidLockDevicesV2Response> lockPrepaidDevices =
                    prepaidV2Operations.lockPrepaidDevicesV2(
                            props.getProperty(PROP_API_KEY), prepaidLockDevicesRequest);

            // Step-3 Response Object processing. This describes responseCode, body parameters and
            // response object
            log.info(
                    "[V2] Prepaid Lock device Response Code " + lockPrepaidDevices.getStatusCode());
            log.info(
                    "[V2] Prepaid Lock Message Response Headers " + lockPrepaidDevices.getHeaders());
            if (null != lockPrepaidDevices.getBody()) {
                PrepaidLockDevicesV2Response resp = lockPrepaidDevices.getBody();
                log.info("[V2] Prepaid Lock device Response Body " + resp);
                if (null != resp.getBatchActionId() && resp.getBatchActionStatus().equals("INPROGRESS")) {
                    getBatchActionStatus(resp.getBatchActionId(), noRespDevicesList);
                } else {
                    for (PrepaidLockDeviceV2ResponseItem item : resp.getLockResponseList()) {
                        log.info(
                                "[V2] Prepaid Lock Device for " + item.getDeviceUid() + " Response Is " + item);
                        noRespDevicesList.remove(item.getDeviceUid());
                    }
                    noRespDevicesList.forEach(
                            device ->
                                    log.severe("[V2] No Processing or Processing Failed for Prepaid Lock for Device " + device));
                }
            }
        } else {
            log.info("[V2] No devices for Prepaid Lock Devices");
        }
    }

    private static void getBatchActionStatus(String batchId, List<String> allDeviceIds) {
        boolean isProcessingInProgress = true;
        PrepaidBatchActionStatusV2Request batchActionUploadStatusReq;
        ResponseEntity<PrepaidBatchActionStatusV2Response> batchActionUploadStatusResp;
        while (isProcessingInProgress) {
            batchActionUploadStatusReq =
                    PrepaidBatchActionStatusV2Request.builder().batchActionId(batchId).build();
            batchActionUploadStatusResp =
                    prepaidV2Operations.getPrepaidDeviceActionStatusV2(
                            props.getProperty(PROP_API_KEY), batchActionUploadStatusReq);
            log.info(
                    "[V2] Prepaid Batch Action Device Status Response Code "
                            + batchActionUploadStatusResp.getStatusCode());
            log.info(
                    "[V2] Prepaid Batch Action Device Status  Response Headers "
                            + batchActionUploadStatusResp.getHeaders());
            if (null != batchActionUploadStatusResp.getBody()) {
                batchActionUploadStatusResp =
                        prepaidV2Operations.getPrepaidDeviceActionStatusV2(
                                props.getProperty(PROP_API_KEY), batchActionUploadStatusReq);
                PrepaidBatchActionStatusV2Response serviceUploadItemResp =
                        batchActionUploadStatusResp.getBody();
                log.info(
                        "[V2] Prepaid Batch Action Device Status Response Data " + serviceUploadItemResp);
                if (null != serviceUploadItemResp.getBatchActionStatus()
                        && !serviceUploadItemResp.getBatchActionStatus().equalsIgnoreCase("INPROGRESS")) {
                    log.info(
                            "[V2] Batch Action Upload Result is " + serviceUploadItemResp.getBatchActionStatus());
                    isProcessingInProgress = false;
                    log.info(
                            "[V2] Prepaid Batch Action Device Status Device List is "
                                    + serviceUploadItemResp.getDeviceResponseList());
                    for (PrepaidBatchActionStatusV2ResponseItem item :
                            serviceUploadItemResp.getDeviceResponseList()) {
                        log.info(
                                "[V2] Prepaid Batch Action Device Status Result for Device "
                                        + item.getDeviceUid()
                                        + " Info "
                                        + item);
                        allDeviceIds.remove(item.getDeviceUid());
                    }
                }
            }
        }
    }

    private static void lockMessageDevicesExampleV2() {
        String csvFile =
                props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "LockMessageDevices_V2.csv";
        List<PrepaidLockMessageDeviceV2RequestItem> lockMessageDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidLockMessageDeviceV2RequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidLockMessageDeviceV2RequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("MESSAGE_TITLE") && 0 < record.get("MESSAGE_TITLE").length()) {
                    imeiInfo.setMessageTitle(record.get("MESSAGE_TITLE"));
                }
                if (null != record.get("MESSAGE_CONTENT") && 0 < record.get("MESSAGE_CONTENT").length()) {
                    imeiInfo.setMessageContent(record.get("MESSAGE_CONTENT"));
                }
                lockMessageDeviceItems.add(imeiInfo);
                log.info("[V2] Prepaid Lock message request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe(
                    "[V2] Exception occurred while forming Prepaid lock message request. Exception " + ex);
        }
        if (0 < lockMessageDeviceItems.size()) {
            // Step-1 Getting List of IMEI details to send lock message and forming the request.
            PrepaidLockMessageDevicesV2Request prepaidLockMessageDevicesRequest =
                    PrepaidLockMessageDevicesV2Request.builder()
                            .lockMessageList(lockMessageDeviceItems)
                            .build();
            List<String> noRespDevicesList =
                    prepaidLockMessageDevicesRequest.getLockMessageList().stream()
                            .map(PrepaidLockMessageDeviceV2RequestItem::getDeviceUid)
                            .collect(Collectors.toList());
            // Step-2 Perform lock message operation for devices
            ResponseEntity<PrepaidLockMessageDevicesV2Response> lockMessagePrepaidDevices =
                    prepaidV2Operations.lockMessagePrepaidDevicesV2(
                            props.getProperty(PROP_API_KEY), prepaidLockMessageDevicesRequest);

            // Step-3 Response Object processing. This describes responseCode, body parameters and
            // response object
            log.info(
                    "[V2] Prepaid Lock Message Response Code " + lockMessagePrepaidDevices.getStatusCode());
            log.info(
                    "[V2] Prepaid Lock Message Response Headers " + lockMessagePrepaidDevices.getHeaders());
            if (null != lockMessagePrepaidDevices.getBody()) {
                PrepaidLockMessageDevicesV2Response resp = lockMessagePrepaidDevices.getBody();
                log.info("[V2] Prepaid Lock Message Response Body " + resp);
                if (null != resp.getBatchActionId() && resp.getBatchActionStatus().equals("INPROGRESS")) {
                    getBatchActionStatus(resp.getBatchActionId(), noRespDevicesList);
                } else {
                    for (PrepaidLockMessageDeviceV2ResponseItem item : resp.getMessageResponseList()) {
                        log.info(
                                "[V2] Prepaid Lock Message Device " + item.getDeviceUid() + " Response Is " + item);
                        noRespDevicesList.remove(item.getDeviceUid());
                    }
                    noRespDevicesList.forEach(
                            device ->
                                    log.severe(
                                            "[V2] No Processing or Processing Failed for Prepaid Lock Message Device for Device " + device));
                }
            }
        } else {
            log.info("[V2] No devices for Prepaid Lock Message Device");
        }
    }

    /**
     * This method contains details for upload prepaid device's
     */
    private static void uploadDevicesExampleV2() {
        String csvFile =
                props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "UploadDevices_V2.csv";
        List<PrepaidUploadV2RequestItem> uploadDevicesList = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidUploadV2RequestItem imeiInfo;
            List<String> fieldNames =
                    new ArrayList<>(
                            List.of(
                                    "DEVICE_ID",
                                    "ID_TYPE",
                                    "DEVICE_TYPE",
                                    "IMEI2",
                                    "EXPIRY",
                                    "RELOAD_DAYS",
                                    "CONTRACT_ID",
                                    "ASSIGNED_CONTRACT_TIME",
                                    "RELOAD_TIME",
                                    "RELOAD_TIME_UNIT"));
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidUploadV2RequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("ID_TYPE") && 0 < record.get("ID_TYPE").length()) {
                    imeiInfo.setIdType(CommonEnums.IdType.valueOf(record.get("ID_TYPE")));
                }
                if (null != record.get("DEVICE_TYPE") && 0 < record.get("DEVICE_TYPE").length()) {
                    imeiInfo.setDeviceType(CommonEnums.DeviceType.valueOf(record.get("DEVICE_TYPE")));
                }
                if (null != record.get("IMEI2") && 0 < record.get("IMEI2").length()) {
                    imeiInfo.setIMEI2(record.get("IMEI2"));
                }
                if (null != record.get("EXPIRY") && 0 < record.get("EXPIRY").length()) {
                    imeiInfo.setExpiry(Integer.valueOf(record.get("EXPIRY")));
                }
                if (null != record.get("RELOAD_DAYS") && 0 < record.get("RELOAD_DAYS").length()) {
                    imeiInfo.setReloadDays(Integer.valueOf(record.get("RELOAD_DAYS")));
                }
                if (null != record.get("CONTRACT_ID") && 0 < record.get("CONTRACT_ID").length()) {
                    imeiInfo.setContractId(Integer.valueOf(record.get("CONTRACT_ID")));
                }
                if (null != record.get("ASSIGNED_CONTRACT_TIME")
                        && 0 < record.get("ASSIGNED_CONTRACT_TIME").length()) {
                    imeiInfo.setAssignedContractDate(record.get("ASSIGNED_CONTRACT_TIME"));
                }
                if (null != record.get("RELOAD_TIME") && 0 < record.get("RELOAD_TIME").length()) {
                    imeiInfo.setReloadTime(Long.valueOf(record.get("RELOAD_TIME")));
                }
                if (null != record.get("RELOAD_TIME_UNIT") && 0 < record.get("RELOAD_TIME_UNIT").length()) {
                    imeiInfo.setReloadTimeUnit(
                            CommonEnums.ReloadTimeUnit.valueOf(record.get("RELOAD_TIME_UNIT")));
                }

                Map<String, String> addProps = new HashMap<>();
                for (Map.Entry<String, String> entry : record.toMap().entrySet()) {
                    if (null != entry.getValue()
                            && 0 < entry.getValue().length()
                            && (!fieldNames.contains(entry.getKey().toUpperCase(Locale.ROOT)))) {
                        addProps.put(entry.getKey(), entry.getValue());
                    }
                }
                if (0 < addProps.size()) {
                    imeiInfo.setAdditionalInfo(addProps);
                }
                uploadDevicesList.add(imeiInfo);
                log.info("[V2] Upload Prepaid Device Details for " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe(
                    "[V2] Exception occurred while forming upload device request. Exception " + ex);
        }
        if (0 < uploadDevicesList.size()) {
            // Step-1 Getting List of IMEI details to be uploaded and forming the request.
            PrepaidUploadDevicesV2Request prepaidUploadDevicesRequest =
                    PrepaidUploadDevicesV2Request.builder().deviceList(uploadDevicesList).build();

            // Step-2 Creating interfaces and do the upload prepaid devices operation
            ResponseEntity<PrepaidUploadDevicesV2Response> uploadDevicesRespList =
                    prepaidV2Operations.uploadPrepaidDevicesV2(
                            props.getProperty(PROP_API_KEY), prepaidUploadDevicesRequest);
            List<String> noRespDevicesList =
                    prepaidUploadDevicesRequest.getDeviceList().stream()
                            .map(PrepaidUploadV2RequestItem::getDeviceUid)
                            .collect(Collectors.toList());
            // Step-3 Response Object processing. This describes responseCode, body parameters and
            // response object
            log.info(
                    "[V2] Prepaid Upload Devices Response Code " + uploadDevicesRespList.getStatusCode());
            log.info(
                    "[V2] Prepaid Upload Devices Response Headers " + uploadDevicesRespList.getHeaders());
            if (null != uploadDevicesRespList.getBody()) {
                PrepaidUploadDevicesV2Response resp = uploadDevicesRespList.getBody();
                log.info("[V2] Prepaid Upload Devices Response Body " + resp);
                if (null != resp.getUploadStatus() && resp.getUploadStatus().equalsIgnoreCase("INPROGRESS"))
                    getUploadDevicesStatusV2(resp.getUploadId(), noRespDevicesList);
            }
            noRespDevicesList.forEach(
                    device ->
                            log.severe(
                                    "[V2] No Processing or Processing Failed for Upload Prepaid Device for Device " + device));
        }
    }

    private static void getUploadDevicesStatusV2(Integer uploadId, List<String> allDeviceIds) {
        boolean isProcessingInProgress = true;
        PrepaidUploadStatusV2Request uploadStatusReq;
        ResponseEntity<PrepaidUploadStatusV2Response> uploadStatusResp;
        while (isProcessingInProgress) {
            uploadStatusReq =
                    PrepaidUploadStatusV2Request.builder().uploadId(uploadId.toString()).build();
            uploadStatusResp =
                    prepaidV2Operations.getPrepaidUploadStatusV2(
                            props.getProperty(PROP_API_KEY), uploadStatusReq);
            log.info(
                    "[V2] Prepaid Upload Devices Status Response Code " + uploadStatusResp.getStatusCode());
            log.info(
                    "[V2] Prepaid Upload Devices Status Response Response Headers "
                            + uploadStatusResp.getHeaders());
            if (null != uploadStatusResp.getBody()) {
                PrepaidUploadStatusV2Response uploadItemResp = uploadStatusResp.getBody();
                log.info(
                        "[V2] Prepaid Upload Devices Status Response Response Data " + uploadItemResp);
                if (null != uploadItemResp
                        && null != uploadItemResp.getUploadStatus()
                        && !uploadItemResp.getUploadStatus().equalsIgnoreCase("INPROGRESS")) {
                    log.info("[V2] IMEI Upload Result is " + uploadItemResp.getUploadStatus());
                    isProcessingInProgress = false;
                    log.info(
                            "[V2] Prepaid Upload Devices Status Device List is "
                                    + uploadItemResp.getDeviceList());
                    for (PrepaidUploadStatusV2ResponseItem item : uploadItemResp.getDeviceList()) {
                        log.info(
                                "[V2] Prepaid Upload Devices Status Result for Device "
                                        + item.getDeviceUid()
                                        + " Info "
                                        + item);
                        allDeviceIds.remove(item.getDeviceUid());
                    }
                }
            }
        }
    }

    /**
     * This method query information for mentioned devices using V2 API
     */
    private static void queryDevicesExampleV2() {
        String csvFile =
                props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "QueryDevices_V2.csv";
        List<PrepaidListDeviceV2RequestItem> queryDeviceList = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidListDeviceV2RequestItem imeiInfo;
            for (CSVRecord record : parser) {
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo =
                            PrepaidListDeviceV2RequestItem.builder()
                                    .deviceUid(record.get("DEVICE_ID").trim())
                                    .build();
                    queryDeviceList.add(imeiInfo);
                    log.info("[V2] Query Device Details for " + imeiInfo.toString());
                }
            }
        } catch (Throwable ex) {
            log.severe(
                    "[V2] Exception occurred while forming device query request. Exception " + ex);
        }
        if (0 < queryDeviceList.size()) {
            // Step-1 Getting List of IMEI details to be queried and forming the request.
            PrepaidListDevicesV2Request prepaidListDevicesRequest =
                    PrepaidListDevicesV2Request.builder().deviceList(queryDeviceList).build();

            // Step-2 Creating interfaces and do the query/list devices operation
            ResponseEntity<PrepaidListDevicesV2Response> devicesList =
                    prepaidV2Operations.listDevicesInfoV2(
                            props.getProperty(PROP_API_KEY), prepaidListDevicesRequest);
            List<String> noRespDevicesList =
                    prepaidListDevicesRequest.getDeviceList().stream()
                            .map(PrepaidListDeviceV2RequestItem::getDeviceUid)
                            .collect(Collectors.toList());

            // Step-3 Response Object processing. This describes responseCode, body parameters and
            // response object
            log.info("[V2] Query Device Response Code " + devicesList.getStatusCode());
            log.info("[V2] Query Device Response Headers " + devicesList.getHeaders());
            if (null != devicesList.getBody()) {
                PrepaidListDevicesV2Response resp = devicesList.getBody();
                log.info("[V2] Query Device Response Body " + resp);
                for (PrepaidListDeviceV2ResponseItem item : resp.getDeviceResponseList()) {
                    if (null == item.getResultCode() && null == item.getResultMessage()) {
                        log.info(
                                "[V2] Query Device " + item.getDeviceUid() + " Information Is " + item);
                    } else {
                        log.info(
                                "[V2] Query Device "
                                        + item.getDeviceUid()
                                        + "  Error/Invalid. Result Code Is: "
                                        + item.getResultCode()
                                        + " Result Message Is: "
                                        + item.getResultMessage());
                    }
                    noRespDevicesList.remove(item.getDeviceUid());
                }
            }
            noRespDevicesList.forEach(
                    device ->
                            log.severe(
                                    "[V2] No Processing or Processing Failed for Query Device for Device " + device));
        } else {
            log.info("[V2] No devices for Query Device");
        }
    }

    private static Properties loadProperties() {
        String fileName = "project.properties";
        Properties props = new Properties();

        try (InputStream inputStream =
                     PrepaidV2Examples.class.getClassLoader().getResourceAsStream(fileName)) {
            props.load(inputStream);
        } catch (Exception ex) {
            System.out.println("Loading Properties failed");
        }
        return props;
    }
}