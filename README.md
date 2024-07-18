## Trustonic North Bound API Client Test

This source code contains examples for using North Bound API to perform operations on Trustonic Telecoms Platform aka TPP.


## What is "Trustonic North Bound API Client Test"?

Trustonic offers Trustonic Telecoms Platform(TTP) product which provides wide range of features.
One of the way to access this is from NorthBound API. To facilitate seamless integration of the NorthBound API for specific operations using 
ready-to-use code, eliminating the need for custom setup and development for TTP v3 NorthBound API integration.
This Project contains samples on how to directly perform operation's of API's(features) directly without integration.
These examples contain ready to user code which can take and extend based on need. 
These examples also gives flexibility to combine and use them based on specific need.

These samples written based on Type of service.  
Currently, this project contains examples how to perform Prepaid Operations on Trustonic Telecom Platform NotrhBound API. 
In future this covers more services and more operations. 

## How to Setup and Use
This project examples code are written using Core Java to avoid usage of external plugins. 
Pre-requisites are mentioned in [Dependencies_and_OpenSource_Licenses.txt](Dependencies_and_OpenSource_Licenses.txt)

After getting mentioned Pre-requisites keep alps-ttp3-northbound-api-client_XXXX-X.X.jar file libs folder.
Compile using below gradle command options

####clean build

Ex: if gradlew is gradle file run below command to build project.

####gradlew clean build
After compilation success start refer and use the examples to integrate into Trustonic Telecom Platform North Bound API's.

More details about Trustonic North Bound API are mentioned below for reference. These examples are wrapper code the below residing API's.
[North Bound API Details](https://productbrochure.playground-trustonic.com/categories/product/north-bound-api)

####Note: Above link is accessable with only with valid Trustonic credentials. 

## Version 
1.0
    -- This is base version which covers following basic operations for Prepaid Service for below operations.<br /> 
        1. queryDevicesExample <br />
        2. uploadDevicesExample <br />
        3.  notifyDevicesExample <br />
        4.  lockMessageDevicesExample <br />
        5.  lockDevicesExample <br />
        6.  unlockDevicesExample <br />
        7.  updateDevicesExample <br />
        8.  reloadDevicesExample <br />
        9.  releaseDevicesExample <br />
        10.  activateServiceDevicesExample <br />
        11.  deActivateServiceDevicesExample <br />


## Security

See [CONTRIBUTING](CONTRIBUTING.md#security-issue-notifications) for more information.

## License

This library is licensed under the Trustonic License. See the  [LICENSE](LICENSE) file.
