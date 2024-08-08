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
package com.trustonic.ttpv3.server.samples.prepaid.imei;

import com.trustonic.ttpv3.client.client.PrepaidOperations;
import com.trustonic.ttpv3.client.client.impl.PrepaidOperationsImpl;
import com.trustonic.ttpv3.client.enums.CommonEnums;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidActivateServicesRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidDeActivateServicesRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidListDevicesRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidLockDevicesRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidLockMessageDevicesRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidNotifyDevicesRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidReleaseDevicesRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidReloadDevicesRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidServiceStatusRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidUnLockDevicesRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidUpdateDevicesRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidUploadDevicesRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.PrepaidUploadStatusRequest;
import com.trustonic.ttpv3.client.model.prepaid.request.item.PrepaidActivateServiceItem;
import com.trustonic.ttpv3.client.model.prepaid.request.item.PrepaidDeActivateServiceItem;
import com.trustonic.ttpv3.client.model.prepaid.request.item.PrepaidListDeviceRequestItem;
import com.trustonic.ttpv3.client.model.prepaid.request.item.PrepaidLockDeviceRequestItem;
import com.trustonic.ttpv3.client.model.prepaid.request.item.PrepaidLockMessageDeviceRequestItem;
import com.trustonic.ttpv3.client.model.prepaid.request.item.PrepaidNotifyDeviceRequestItem;
import com.trustonic.ttpv3.client.model.prepaid.request.item.PrepaidReleaseDeviceRequestItem;
import com.trustonic.ttpv3.client.model.prepaid.request.item.PrepaidReloadDeviceRequestItem;
import com.trustonic.ttpv3.client.model.prepaid.request.item.PrepaidUnLockDeviceRequestItem;
import com.trustonic.ttpv3.client.model.prepaid.request.item.PrepaidUpdateDeviceRequestItem;
import com.trustonic.ttpv3.client.model.prepaid.request.item.PrepaidUploadRequestItem;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidActivateServicesResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidDeActivateServicesResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidListDevicesResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidLockDevicesResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidLockMessageDevicesResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidNotifyDevicesResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidReleaseDevicesResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidReloadDevicesResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidServiceStatusResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidUnLockDevicesResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidUpdateDevicesResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidUploadDevicesResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.PrepaidUploadStatusResponse;
import com.trustonic.ttpv3.client.model.prepaid.response.item.PrepaidListDeviceResponseItem;
import com.trustonic.ttpv3.client.model.prepaid.response.item.PrepaidLockDeviceResponseItem;
import com.trustonic.ttpv3.client.model.prepaid.response.item.PrepaidLockMessageDeviceResponseItem;
import com.trustonic.ttpv3.client.model.prepaid.response.item.PrepaidNotifyDeviceResponseItem;
import com.trustonic.ttpv3.client.model.prepaid.response.item.PrepaidReleaseDeviceResponseItem;
import com.trustonic.ttpv3.client.model.prepaid.response.item.PrepaidReloadDeviceResponseItem;
import com.trustonic.ttpv3.client.model.prepaid.response.item.PrepaidServiceStatusResponseItem;
import com.trustonic.ttpv3.client.model.prepaid.response.item.PrepaidUnLockDeviceResponseItem;
import com.trustonic.ttpv3.client.model.prepaid.response.item.PrepaidUpdateDeviceResponseItem;
import com.trustonic.ttpv3.client.model.prepaid.response.item.PrepaidUploadStatusResponseItem;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class describes examples and usage of Trustonic API
 */
public class PrepaidExamples {
    private static final Properties props;
    private static final PrepaidOperations prepaidOperations;
    private static final Logger log = Logger.getLogger(PrepaidExamples.class.getSimpleName());
    private static final String PROP_TENANT_NAME = "tenant_name";
    private static final String PROP_API_KEY = "api_key";
    private static final String PROP_BASE_FOLDER_NAME = "base-folder";

    static {
        props = loadProperties();
        try {
            LogManager.getLogManager().readConfiguration(PrepaidExamples.class.getClassLoader().getResource("logging-client.properties").openStream());
        } catch (Throwable ex) {
            log.severe("Logging file error " + Arrays.toString(ex.getStackTrace()));
        }
        log.info("Configured Properties for project " + props);
        prepaidOperations = PrepaidOperationsImpl.getInstance(props.getProperty(PROP_TENANT_NAME));
    }

    public static void main(String[] args) {
        if (null == prepaidOperations || (null == props || 0 == props.size())) {
            log.severe("Mandatory data not exist. Existing program");
            return;
        }

        // This method contains sample code to query device's information
        queryDevicesExample();

        // This method contains sample code for upload prepaid device's
        uploadDevicesExample();

        // This method contains sample code for lock message device's
        lockMessageDevicesExample();

        // This method contains sample code for lock device's
        lockDevicesExample();

        // This method contains sample code for notify device's
        notifyDevicesExample();

        // This method contains sample code for release device's
        releaseDevicesExample();

        // This method contains sample code for reload device's
        reloadDevicesExample();

        // This method contains sample code for unlock device's
        unlockDevicesExample();

        // This method contains sample code for update IMEI2 for device's
        updateDevicesExample();

        // This method contains sample code for activating prepaid service for device's
        activateServiceDevicesExample();

        // This method contains sample code for de-activating prepaid service for device's
        deActivateServiceDevicesExample();
    }

    private static void deActivateServiceDevicesExample() {
        String csvFile = props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "DeActivateServiceDevices.csv";
        List<PrepaidDeActivateServiceItem> deActivateServiceDevicesList = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidDeActivateServiceItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidDeActivateServiceItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID"));
                }
                if (null != record.get("ADDITIONAL_REMARKS") && 0 < record.get("ADDITIONAL_REMARKS").length()) {
                    imeiInfo.setAdditionalRemarks(record.get("ADDITIONAL_REMARKS"));
                }
                if (null != record.get("DEACTIVATE_REASON") && 0 < record.get("DEACTIVATE_REASON").length()) {
                    imeiInfo.setDeactivateReason(record.get("DEACTIVATE_REASON"));
                }
                deActivateServiceDevicesList.add(imeiInfo);
            }
        } catch (Exception ex) {
            log.severe("Exception occurred while forming deactivate service for device. Exception " + ex);
        }
        if (0 < deActivateServiceDevicesList.
                size()) {
            // Step-1 Getting List of IMEI details to be deactivated service and forming the request.
            PrepaidDeActivateServicesRequest prepaidActivateServiceDevicesRequest = PrepaidDeActivateServicesRequest.builder().deviceList(deActivateServiceDevicesList).build();
            List<String> noRespDevicesList = prepaidActivateServiceDevicesRequest.getDeviceList().stream().map(PrepaidDeActivateServiceItem::getDeviceUid).collect(Collectors.toList());

            // Step-2 Creating interfaces and do the deactivate service  devices operation
            ResponseEntity<PrepaidDeActivateServicesResponse> deActivateServiceDevicesRespList = prepaidOperations.deActivateService(props.getProperty(PROP_API_KEY), prepaidActivateServiceDevicesRequest);

            //Step-3 Response Object processing. This describes responseCode, body parameters and response object
            log.info("Prepaid Deactivating Service Devices Response Code " + deActivateServiceDevicesRespList.getStatusCode());
            log.info("Prepaid Deactivating Service  Response Headers " + deActivateServiceDevicesRespList.getHeaders());
            if (null != deActivateServiceDevicesRespList.getBody() && HttpStatus.OK.equals(deActivateServiceDevicesRespList.getStatusCode())) {
                PrepaidDeActivateServicesResponse resp = deActivateServiceDevicesRespList.getBody();
                log.info("Prepaid Deactivating Service  Devices Response Body " + resp);
                if (!resp.getServiceOrderStatus().equalsIgnoreCase("FAILED")) {
                    getServiceUploadStatus(resp.getServiceOrderID(), noRespDevicesList);
                }
            }
            noRespDevicesList
                    .forEach(device -> log.info("No Processing for Prepaid Deactivate Service for Device " + device));
        } else {
            log.info("No devices for Prepaid Deactivate Service");
        }
    }


    private static void activateServiceDevicesExample() {
        String csvFile = props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "ActivateServiceDevices.csv";
        List<PrepaidActivateServiceItem> activateServiceDevicesList = new ArrayList<>();
        List<String> existingFieldNames = new ArrayList<>(List.of("DEVICE_ID", "SERVICE_TENURE", "IMEI2", "EXPIRY", "RELOAD_DAYS"));

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidActivateServiceItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidActivateServiceItem.builder().build();
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
                    if (null != entryVal.getValue() && 0 < entryVal.getValue().length() && (!existingFieldNames.contains(entryVal.getKey().toUpperCase(Locale.ROOT)))) {
                        additionalProps.put(entryVal.getKey(), entryVal.getValue());
                    }
                }
                if (0 < additionalProps.size()) {
                    imeiInfo.setAdditionalInfo(additionalProps);
                }
                log.info("Prepaid Activate Service Device request " + imeiInfo.toString());
                activateServiceDevicesList.add(imeiInfo);
            }
        } catch (Exception ex) {
            log.severe("Exception occurred while forming activate service for device. Exception " + ex);
        }
        if (0 < activateServiceDevicesList.
                size()) {
            // Step-1 Getting List of IMEI details to be activated service and forming the request.
            PrepaidActivateServicesRequest prepaidActivateServiceDevicesRequest = PrepaidActivateServicesRequest.builder().deviceList(activateServiceDevicesList).build();
            List<String> noRespDevicesList = prepaidActivateServiceDevicesRequest.getDeviceList().stream().map(PrepaidActivateServiceItem::getDeviceUid).collect(Collectors.toList());

            // Step-2 Creating interfaces and do the activate service  devices operation
            ResponseEntity<PrepaidActivateServicesResponse> activateServiceDevicesRespList = prepaidOperations.activateService(props.getProperty(PROP_API_KEY), prepaidActivateServiceDevicesRequest);

            //Step-3 Response Object processing. This describes responseCode, body parameters and response object
            log.info("Prepaid Upload Devices Response Code " + activateServiceDevicesRespList.getStatusCode());
            log.info("Prepaid Upload Devices Response Headers " + activateServiceDevicesRespList.getHeaders());
            if (null != activateServiceDevicesRespList.getBody() && HttpStatus.OK.equals(activateServiceDevicesRespList.getStatusCode())) {
                PrepaidActivateServicesResponse resp = activateServiceDevicesRespList.getBody();
                log.info("Prepaid Activate Service Devices Response Body " + resp);
                if (!resp.getServiceOrderStatus().equalsIgnoreCase("FAILED")) {
                    getServiceUploadStatus(resp.getServiceOrderID(), noRespDevicesList);
                }
            }
            noRespDevicesList
                    .forEach(device -> log.info("No Processing for Prepaid Activate Service for Device " + device));
        } else {
            log.info("No devices for Prepaid Activate Service for Device");
        }
    }

    private static void getServiceUploadStatus(String uploadId, List<String> devices) {
        boolean isProcessingInProgress = true;
        PrepaidServiceStatusRequest uploadServiceStatusReq;
        ResponseEntity<PrepaidServiceStatusResponse> uploadServiceStatusResp;
        while (isProcessingInProgress) {
            uploadServiceStatusReq = PrepaidServiceStatusRequest.builder().serviceOrderId(uploadId).build();
            uploadServiceStatusResp = prepaidOperations.getServiceStatus(props.getProperty(PROP_API_KEY), uploadServiceStatusReq);
            log.info("Prepaid Upload Service Status Response Code " + uploadServiceStatusResp.getStatusCode());
            log.info("Prepaid Upload Service Status Response Response Headers " + uploadServiceStatusResp.getHeaders());
            if (null != uploadServiceStatusResp.getBody()) {
                PrepaidServiceStatusResponse uploadItemResp = uploadServiceStatusResp.getBody();
                log.info("Prepaid Upload Service Status Response Response Data " + uploadItemResp);
                if (null != uploadItemResp.getOrderStatus() && (!uploadItemResp.getOrderStatus().equalsIgnoreCase("INPROGRESS"))) {
                    log.info("Service Status Result is " + uploadItemResp.getOrderStatus());
                    isProcessingInProgress = false;
                    log.info("Prepaid Upload Service Status Device List is " + uploadItemResp.getDeviceList());
                    for (PrepaidServiceStatusResponseItem item : uploadItemResp.getDeviceList()) {
                        log.info("Prepaid Upload Service Status Result for Device " + item.getDeviceUid() + " Info " + item);
                        devices.remove(item.getDeviceUid());
                    }
                }
            }
        }
    }

    private static void updateDevicesExample() {
        String csvFile = props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "UpdateDevices.csv";
        List<PrepaidUpdateDeviceRequestItem> updateDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidUpdateDeviceRequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidUpdateDeviceRequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("IMEI2") && 0 < record.get("IMEI2").length()) {
                    imeiInfo.setIMEI2(record.get("IMEI2"));
                }
                if (null != record.get("DEVICE_TYPE") && 0 < record.get("DEVICE_TYPE").length()) {
                    imeiInfo.setDeviceType(CommonEnums.UpdateDeviceDeviceType.valueOf(record.get("DEVICE_TYPE")));
                }
                updateDeviceItems.add(imeiInfo);
                log.info("Prepaid Update Device request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe("Exception occurred while forming update device request. Exception " + ex);
        }
        if (0 < updateDeviceItems.size()) {
            // Step-1 Getting Update of IMEI details for forming the request.
            PrepaidUpdateDevicesRequest prepaidUpdateDevicesRequest = PrepaidUpdateDevicesRequest.builder().deviceList(updateDeviceItems).build();
            List<String> noRespDevicesList = prepaidUpdateDevicesRequest.getDeviceList().stream().map(PrepaidUpdateDeviceRequestItem::getDeviceUid).collect(Collectors.toList());

            // Step-2 Perform update device operation for devices
            ResponseEntity<PrepaidUpdateDevicesResponse> updatePrepaidDevices = prepaidOperations.updateDevices(props.getProperty(PROP_API_KEY), prepaidUpdateDevicesRequest);

            //Step-3 Response Object processing. This describes responseCode, body parameters and response object
            log.info("Prepaid Update device Response Code " + updatePrepaidDevices.getStatusCode());
            log.info("Prepaid Update Message Response Headers " + updatePrepaidDevices.getHeaders());
            if (null != updatePrepaidDevices.getBody()) {
                PrepaidUpdateDevicesResponse resp = updatePrepaidDevices.getBody();
                log.info("Prepaid Update device Response Body " + resp);
                for (PrepaidUpdateDeviceResponseItem item : resp.getDeviceResponseList()) {
                    log.info("Prepaid Update Device for " + item.getDeviceUid() + " Response Is " + item);
                    noRespDevicesList.remove(item.getDeviceUid());
                }
            }
            noRespDevicesList
                    .forEach(device -> log.info("No Processing for Prepaid Update Device " + device));
        } else {
            log.info("No devices for Prepaid Update Device");
        }
    }

    private static void unlockDevicesExample() {
        String csvFile = props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "UnlockDevices.csv";
        List<PrepaidUnLockDeviceRequestItem> unlockDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidUnLockDeviceRequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidUnLockDeviceRequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("UNLOAD_DAYS") && 0 < record.get("UNLOAD_DAYS").length()) {
                    imeiInfo.setValidityDays(Integer.valueOf(record.get("UNLOAD_DAYS")));
                }
                unlockDeviceItems.add(imeiInfo);
                log.info("Prepaid Unlock Device request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe("Exception occurred while forming unlock device request. Exception " + ex);
        }
        if (0 < unlockDeviceItems.size()) {
            // Step-1 Getting Unlock of IMEI details for forming the request.
            PrepaidUnLockDevicesRequest prepaidUnlockDevicesRequest = PrepaidUnLockDevicesRequest.builder().unLockList(unlockDeviceItems).build();

            List<String> noRespDevicesList = prepaidUnlockDevicesRequest.getUnLockList().stream().map(PrepaidUnLockDeviceRequestItem::getDeviceUid).collect(Collectors.toList());

            // Step-2 Perform unlock device operation for devices
            ResponseEntity<PrepaidUnLockDevicesResponse> unLockPrepaidDevices = prepaidOperations.unlockPrepaidDevices(props.getProperty(PROP_API_KEY), prepaidUnlockDevicesRequest);

            //Step-3 Response Object processing. This describes responseCode, body parameters and response object
            log.info("Prepaid Unlock device Response Code " + unLockPrepaidDevices.getStatusCode());
            log.info("Prepaid Unlock Message Response Headers " + unLockPrepaidDevices.getHeaders());
            if (null != unLockPrepaidDevices.getBody()) {
                PrepaidUnLockDevicesResponse resp = unLockPrepaidDevices.getBody();
                log.info("Prepaid Unlock device Response Body " + resp);
                for (PrepaidUnLockDeviceResponseItem item : resp.getUnlockResponseList()) {
                    log.info("Prepaid Unlock Device for " + item.getDeviceUid() + " Response Is " + item);
                    noRespDevicesList.remove(item.getDeviceUid());
                }
            }
            noRespDevicesList
                    .forEach(device -> log.info("No Processing for Prepaid Unlock Device " + device));
        } else {
            log.info("No devices for Prepaid Unlock Device");
        }
    }

    private static void reloadDevicesExample() {
        String csvFile = props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "ReloadDevices.csv";
        List<PrepaidReloadDeviceRequestItem> reloadDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidReloadDeviceRequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidReloadDeviceRequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("RELOAD_DAYS") && 0 < record.get("RELOAD_DAYS").length()) {
                    imeiInfo.setReloadDays(Integer.valueOf(record.get("RELOAD_DAYS")));
                }
                reloadDeviceItems.add(imeiInfo);
                log.info("Prepaid Reload Device request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe("Exception occurred while forming reload device request. Exception " + ex);
        }
        if (0 < reloadDeviceItems.size()) {
            // Step-1 Getting Reload of IMEI details for forming the request.
            PrepaidReloadDevicesRequest prepaidReloadDevicesRequest = PrepaidReloadDevicesRequest.builder().reloadList(reloadDeviceItems).build();

            List<String> noRespDevicesList = prepaidReloadDevicesRequest.getReloadList().stream().map(PrepaidReloadDeviceRequestItem::getDeviceUid).collect(Collectors.toList());


            // Step-2 Perform reload device operation for devices
            ResponseEntity<PrepaidReloadDevicesResponse> reloadPrepaidDevices = prepaidOperations.reloadPrepaidDevices(props.getProperty(PROP_API_KEY), prepaidReloadDevicesRequest);

            //Step-3 Response Object processing. This describes responseCode, body parameters and response object
            log.info("Prepaid Reload device Response Code " + reloadPrepaidDevices.getStatusCode());
            log.info("Prepaid Reload Message Response Headers " + reloadPrepaidDevices.getHeaders());
            if (null != reloadPrepaidDevices.getBody()) {
                PrepaidReloadDevicesResponse resp = reloadPrepaidDevices.getBody();
                log.info("Prepaid Reload device Response Body " + resp);
                for (PrepaidReloadDeviceResponseItem item : resp.getReloadResponseList()) {
                    log.info("Prepaid Reload Device for " + item.getDeviceUid() + " Response Is " + item);
                    noRespDevicesList.remove(item.getDeviceUid());
                }
            }
            noRespDevicesList
                    .forEach(device -> log.info("No Processing for Prepaid Reload Device " + device));
        } else {
            log.info("No devices for Prepaid Reload Device");
        }
    }

    private static void releaseDevicesExample() {
        String csvFile = props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "ReleaseDevices.csv";
        List<PrepaidReleaseDeviceRequestItem> releaseDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidReleaseDeviceRequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidReleaseDeviceRequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("REASON") && 0 < record.get("REASON").length()) {
                    imeiInfo.setReason(record.get("REASON"));
                }
                releaseDeviceItems.add(imeiInfo);
                log.info("Prepaid Release Device request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe("Exception occurred while forming release device request. Exception " + ex);
        }
        if (0 < releaseDeviceItems.size()) {
            // Step-1 Getting Release of IMEI details for forming the request.
            PrepaidReleaseDevicesRequest prepaidReleaseDevicesRequest = PrepaidReleaseDevicesRequest.builder().deviceReleaseList(releaseDeviceItems).build();
            List<String> noRespDevicesList = prepaidReleaseDevicesRequest.getDeviceReleaseList().stream().map(PrepaidReleaseDeviceRequestItem::getDeviceUid).collect(Collectors.toList());

            // Step-2 Perform release device operation for devices
            ResponseEntity<PrepaidReleaseDevicesResponse> releasePrepaidDevices = prepaidOperations.releasePrepaidDevices(props.getProperty(PROP_API_KEY), prepaidReleaseDevicesRequest);

            //Step-3 Response Object processing. This describes responseCode, body parameters and response object
            log.info("Prepaid Release device Response Code " + releasePrepaidDevices.getStatusCode());
            log.info("Prepaid Release Message Response Headers " + releasePrepaidDevices.getHeaders());
            if (null != releasePrepaidDevices.getBody()) {
                PrepaidReleaseDevicesResponse resp = releasePrepaidDevices.getBody();
                log.info("Prepaid Release device Response Body " + resp);
                for (PrepaidReleaseDeviceResponseItem item : resp.getReleaseResponseList()) {
                    log.info("Prepaid Release Device for " + item.getDeviceUid() + " Response Is " + item);
                    noRespDevicesList.remove(item.getDeviceUid());
                }
            }
            noRespDevicesList
                    .forEach(device -> log.info("No Processing for Prepaid Release Device for " + device));
        } else {
            log.info("No devices for Prepaid Release Device");
        }
    }

    private static void notifyDevicesExample() {
        String csvFile = props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "NotifyDevices.csv";
        List<PrepaidNotifyDeviceRequestItem> notifyDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidNotifyDeviceRequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidNotifyDeviceRequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("NOTIFICATION_TITLE") && 0 < record.get("NOTIFICATION_TITLE").length()) {
                    imeiInfo.setNotificationTitle(record.get("NOTIFICATION_TITLE"));
                }
                if (null != record.get("NOTIFICATION_CONTENT") && 0 < record.get("NOTIFICATION_CONTENT").length()) {
                    imeiInfo.setNotificationContent(record.get("NOTIFICATION_CONTENT"));
                }
                if (null != record.get("NOTIFICATION_TYPE") && 0 < record.get("NOTIFICATION_TYPE").length()) {
                    imeiInfo.setNotificationType(PrepaidNotifyDeviceRequestItem.NotificationType.valueOf(record.get("NOTIFICATION_TYPE")));
                }
                notifyDeviceItems.add(imeiInfo);
                log.info("Prepaid Notify Device request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe("Exception occurred while forming notify device request. Exception " + ex);
        }
        if (0 < notifyDeviceItems.size()) {
            // Step-1 Getting List of IMEI details to send notify device request and forming the request.
            PrepaidNotifyDevicesRequest prepaidNotifyDevicesRequest = PrepaidNotifyDevicesRequest.builder().messageList(notifyDeviceItems).build();

            List<String> noRespDevicesList = prepaidNotifyDevicesRequest.getMessageList().stream().map(PrepaidNotifyDeviceRequestItem::getDeviceUid).collect(Collectors.toList());

            // Step-2 Perform notify device operation for devices
            ResponseEntity<PrepaidNotifyDevicesResponse> notifyPrepaidDevices = prepaidOperations.notifyPrepaidDevices(props.getProperty(PROP_API_KEY), prepaidNotifyDevicesRequest);

            //Step-3 Response Object processing. This describes responseCode, body parameters and response object
            log.info("Prepaid Notify device Response Code " + notifyPrepaidDevices.getStatusCode());
            log.info("Prepaid Notify Message Response Headers " + notifyPrepaidDevices.getHeaders());
            if (null != notifyPrepaidDevices.getBody()) {
                PrepaidNotifyDevicesResponse resp = notifyPrepaidDevices.getBody();
                log.info("Prepaid Notify device Response Body " + resp);
                for (PrepaidNotifyDeviceResponseItem item : resp.getMessageResponseList()) {
                    log.info("Prepaid Notify Device for " + item.getDeviceUid() + " Response Is " + item);
                    noRespDevicesList.remove(item.getDeviceUid());
                }
            }
            noRespDevicesList
                    .forEach(device -> log.info("No Processing for Query Device for Device " + device));
        } else {
            log.info("No devices for Prepaid Notify Device");
        }
    }

    private static void lockDevicesExample() {
        String csvFile = props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "LockDevices.csv";
        List<PrepaidLockDeviceRequestItem> lockDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidLockDeviceRequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidLockDeviceRequestItem.builder().build();
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo.setDeviceUid(record.get("DEVICE_ID").trim());
                }
                if (null != record.get("LOCK_MESSAGE_TITLE") && 0 < record.get("LOCK_MESSAGE_TITLE").length()) {
                    imeiInfo.setLockMsgTitle(record.get("LOCK_MESSAGE_TITLE"));
                }
                if (null != record.get("LOCK_MESSAGE_CONTENT") && 0 < record.get("LOCK_MESSAGE_CONTENT").length()) {
                    imeiInfo.setLockMsgContent(record.get("LOCK_MESSAGE_CONTENT"));
                }
                lockDeviceItems.add(imeiInfo);
                log.info("Prepaid Lock Device request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe("Exception occurred while forming lock message request. Exception " + ex);
        }
        if (0 < lockDeviceItems.size()) {
            // Step-1 Getting List of IMEI details to send lock request and forming the request.
            PrepaidLockDevicesRequest prepaidLockDevicesRequest = PrepaidLockDevicesRequest.builder().lockList(lockDeviceItems).build();

            List<String> noRespDevicesList = prepaidLockDevicesRequest.getLockList().stream().map(PrepaidLockDeviceRequestItem::getDeviceUid).collect(Collectors.toList());

            // Step-2 Perform lock device operation for devices
            ResponseEntity<PrepaidLockDevicesResponse> lockPrepaidDevices = prepaidOperations.lockPrepaidDevices(props.getProperty(PROP_API_KEY), prepaidLockDevicesRequest);

            //Step-3 Response Object processing. This describes responseCode, body parameters and response object
            log.info("Prepaid Lock device Response Code " + lockPrepaidDevices.getStatusCode());
            log.info("Prepaid Lock Message Response Headers " + lockPrepaidDevices.getHeaders());
            if (null != lockPrepaidDevices.getBody()) {
                PrepaidLockDevicesResponse resp = lockPrepaidDevices.getBody();
                log.info("Prepaid Lock device Response Body " + resp);
                for (PrepaidLockDeviceResponseItem item : resp.getLockResponseList()) {
                    log.info("Prepaid Lock Device for " + item.getDeviceUid() + " Response Is " + item);
                    noRespDevicesList.remove(item.getDeviceUid());
                }
            }
            noRespDevicesList
                    .forEach(device -> log.info("No Processing for Prepaid Lock for Device " + device));
        } else {
            log.info("No devices for Prepaid Lock Devices");
        }
    }

    private static void lockMessageDevicesExample() {
        String csvFile = props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "LockMessageDevices.csv";
        List<PrepaidLockMessageDeviceRequestItem> lockMessageDeviceItems = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidLockMessageDeviceRequestItem imeiInfo;
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidLockMessageDeviceRequestItem.builder().build();
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
                log.info("Prepaid Lock message request " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe("Exception occurred while forming Prepaid lock message request. Exception " + ex);
        }
        if (0 < lockMessageDeviceItems.size()) {
            // Step-1 Getting List of IMEI details to send lock message and forming the request.
            PrepaidLockMessageDevicesRequest prepaidLockMessageDevicesRequest = PrepaidLockMessageDevicesRequest.builder().lockMessageList(lockMessageDeviceItems).build();
            List<String> noRespDevicesList = prepaidLockMessageDevicesRequest.getLockMessageList().stream().map(PrepaidLockMessageDeviceRequestItem::getDeviceUid).collect(Collectors.toList());
            // Step-2 Perform lock message operation for devices
            ResponseEntity<PrepaidLockMessageDevicesResponse> lockMessagePrepaidDevices = prepaidOperations.lockMessagePrepaidDevices(props.getProperty(PROP_API_KEY), prepaidLockMessageDevicesRequest);

            //Step-3 Response Object processing. This describes responseCode, body parameters and response object
            log.info("Prepaid Lock Message Response Code " + lockMessagePrepaidDevices.getStatusCode());
            log.info("Prepaid Lock Message Response Headers " + lockMessagePrepaidDevices.getHeaders());
            if (null != lockMessagePrepaidDevices.getBody()) {
                PrepaidLockMessageDevicesResponse resp = lockMessagePrepaidDevices.getBody();
                log.info("Prepaid Lock Message Response Body " + resp);
                for (PrepaidLockMessageDeviceResponseItem item : resp.getMessageResponseList()) {
                    log.info("Prepaid Lock Message Device " + item.getDeviceUid() + " Response Is " + item);
                    noRespDevicesList.remove(item.getDeviceUid());
                }
            }
            noRespDevicesList
                    .forEach(device -> log.info("No Processing for Prepaid Lock Message Device for Device " + device));
        } else {
            log.info("No devices for Prepaid Lock Message Device");
        }
    }

    /**
     * This method contains details for upload prepaid device's
     */
    private static void uploadDevicesExample() {
        String csvFile = props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "UploadDevices.csv";
        List<PrepaidUploadRequestItem> uploadDevicesList = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidUploadRequestItem imeiInfo;
            List<String> fieldNames = new ArrayList<>(List.of("DEVICE_ID", "ID_TYPE", "DEVICE_TYPE", "IMEI2", "EXPIRY", "RELOAD_DAYS"));
            for (CSVRecord record : parser) {
                imeiInfo = PrepaidUploadRequestItem.builder().build();
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
                Map<String, String> addProps = new HashMap<>();
                for (Map.Entry<String, String> entry : record.toMap().entrySet()) {
                    if (null != entry.getValue() && 0 < entry.getValue().length() && (!fieldNames.contains(entry.getKey().toUpperCase(Locale.ROOT)))) {
                        addProps.put(entry.getKey(), entry.getValue());
                    }
                }
                if (0 < addProps.size()) {
                    imeiInfo.setAdditionalInfo(addProps);
                }
                uploadDevicesList.add(imeiInfo);
                log.info("Upload Prepaid Device Details for " + imeiInfo.toString());
            }
        } catch (Throwable ex) {
            log.severe("Exception occurred while forming device query request. Exception " + ex);
        }
        if (0 < uploadDevicesList.
                size()) {
            // Step-1 Getting List of IMEI details to be uploaded and forming the request.
            PrepaidUploadDevicesRequest prepaidUploadDevicesRequest = PrepaidUploadDevicesRequest.builder().deviceList(uploadDevicesList).build();

            // Step-2 Creating interfaces and do the upload prepaid devices operation
            ResponseEntity<PrepaidUploadDevicesResponse> uploadDevicesRespList = prepaidOperations.uploadPrepaidDevices(props.getProperty(PROP_API_KEY), prepaidUploadDevicesRequest);
            List<String> noRespDevicesList = prepaidUploadDevicesRequest.getDeviceList().stream().map(PrepaidUploadRequestItem::getDeviceUid).collect(Collectors.toList());
            //Step-3 Response Object processing. This describes responseCode, body parameters and response object
            log.info("Prepaid Upload Devices Response Code " + uploadDevicesRespList.getStatusCode());
            log.info("Prepaid Upload Devices Response Headers " + uploadDevicesRespList.getHeaders());
            if (null != uploadDevicesRespList.getBody()) {
                PrepaidUploadDevicesResponse resp = uploadDevicesRespList.getBody();
                log.info("Prepaid Upload Devices Response Body " + resp);
                getUploadDevicesStatus(resp.getUploadID(), noRespDevicesList);
            }
            noRespDevicesList
                    .forEach(device -> log.info("No Processing for Upload Prepaid Device for Device " + device));
        }

    }

    private static void getUploadDevicesStatus(Integer uploadId, List<String> allDeviceIds) {
        boolean isProcessingInProgress = true;
        PrepaidUploadStatusRequest uploadStatusReq;
        ResponseEntity<PrepaidUploadStatusResponse> uploadStatusResp;
        while (isProcessingInProgress) {
            uploadStatusReq = PrepaidUploadStatusRequest.builder().uploadId(uploadId).build();
            uploadStatusResp = prepaidOperations.getPrepaidUploadStatus(props.getProperty(PROP_API_KEY), uploadStatusReq);
            log.info("Prepaid Upload Devices Status Response Code " + uploadStatusResp.getStatusCode());
            log.info("Prepaid Upload Devices Status Response Response Headers " + uploadStatusResp.getHeaders());
            if (null != uploadStatusResp.getBody()) {
                PrepaidUploadStatusResponse uploadItemResp = uploadStatusResp.getBody();
                log.info("Prepaid Upload Devices Status Response Response Data " + uploadItemResp);
                if (!uploadItemResp.getUploadStatus().equalsIgnoreCase("INPROGRESS")) {
                    log.info("IMEI Upload Result is " + uploadItemResp.getUploadStatus());
                    isProcessingInProgress = false;
                    log.info("Prepaid Upload Devices Status Device List is " + uploadItemResp.getDeviceList());
                    for (PrepaidUploadStatusResponseItem item : uploadItemResp.getDeviceList()) {
                        log.info("Prepaid Upload Devices Status Result for Device " + item.getDeviceUid() + " Info " + item);
                        allDeviceIds.remove(item.getDeviceUid());
                    }
                }
            }
        }
    }

    /**
     * This method query information for mentioned devices
     */
    private static void queryDevicesExample() {
        String csvFile = props.getProperty(PROP_BASE_FOLDER_NAME) + File.separator + "QueryDevices.csv";
        List<PrepaidListDeviceRequestItem> queryDeviceList = new ArrayList<>();

        try (FileReader reader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            PrepaidListDeviceRequestItem imeiInfo;
            for (CSVRecord record : parser) {
                if (null != record.get("DEVICE_ID") && 0 < record.get("DEVICE_ID").trim().length()) {
                    imeiInfo = PrepaidListDeviceRequestItem.builder()
                            .deviceUid(record.get("DEVICE_ID").trim())
                            .build();
                    queryDeviceList.add(imeiInfo);
                    log.info("Query Device Details for " + imeiInfo.toString());
                }
            }
        } catch (Throwable ex) {
            log.severe("Exception occurred while forming device query request. Exception " + ex);
        }
        if (0 < queryDeviceList.size()) {
            // Step-1 Getting List of IMEI details to be queried and forming the request.
            PrepaidListDevicesRequest prepaidListDevicesRequest = PrepaidListDevicesRequest.builder().deviceList(queryDeviceList).build();

            // Step-2 Creating interfaces and do the query/list devices operation
            ResponseEntity<PrepaidListDevicesResponse> devicesList = prepaidOperations.listDevicesInfo(props.getProperty(PROP_API_KEY), prepaidListDevicesRequest);
            List<String> noRespDevicesList = prepaidListDevicesRequest.getDeviceList().stream().map(PrepaidListDeviceRequestItem::getDeviceUid).collect(Collectors.toList());

            //Step-3 Response Object processing. This describes responseCode, body parameters and response object
            log.info("Query Device Response Code " + devicesList.getStatusCode());
            log.info("Query Device Response Headers " + devicesList.getHeaders());
            if (null != devicesList.getBody()) {
                PrepaidListDevicesResponse resp = devicesList.getBody();
                log.info("Query Device Response Body " + resp);
                for (PrepaidListDeviceResponseItem item : resp.getDeviceResponseList()) {
                    log.info("Query Device " + item.getDeviceUniqueID() + " Information Is " + item);
                    noRespDevicesList.remove(item.getDeviceUniqueID());
                }
            }
            noRespDevicesList
                    .forEach(device -> log.info("No Processing for Query Device for Device " + device));
        } else {
            log.info("No devices for Query Device");
        }
    }

    private static Properties loadProperties() {
        String fileName = "project.properties";
        Properties props = new Properties();

        try (InputStream inputStream = PrepaidExamples.class.getClassLoader().getResourceAsStream(fileName)) {
            props.load(inputStream);
        } catch (Exception ex) {
            System.out.println("Loading Properties failed");
        }
        return props;
    }
}
