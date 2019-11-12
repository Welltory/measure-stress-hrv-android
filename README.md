# Welltory Integration Android Demo

<p>
  <img src="/screens/Frame.jpg?raw=true" alt="Welltory flow">
</p>

This demo app is intended for people who want to integrate with Welltory app to collect stress, energy and other HRV data about their users. The Demo app demonstrates how your app can work with Welltory. This integration is free and it’s created to help you add value for your users who already use Welltory.  [Read more here](#install)

To become an integration partner - [apply here](https://welltory.typeform.com/to/epJ3PR)


Welltory is an app that measures people’s HRV with just a smartphone camera to calculate their stress and energy levels. You can redirect users to our app and direct them back to your app with measurement results. Welltory integration will allow you to collect data of the following parameters:

* Stress (Welltory's proprietary algorithm trained on millions of measurements)
* Energy (Welltory's proprietary algorithm trained on millions of measurements)
* Productivity index (Welltory's proprietary algorithm trained on millions of measurements)
* RMSSD index
* SDNN index
* Total power

This demo is to demonstrate:
* how the user is navigated from your app to the App Store,
* how the measurement is taken,
* a request asking the user to share the results of the measurement with your app,
* the return to your application,
* a sample of results presentation,
and also the source code of the integration.

Continue reading for installation.

### Table of Contents
1. [How to install](#install)
   1. [Requirements](#requirements)
   2. [Installation guide](#guide)
2. [Example usage](#usage)
3. [Integration](#integration)
4. [Measurement request](#request)
   1. [Measurement request link and parameters](#link)
5. [Stress results overview](#result)
   1. [Configure your app to add it the list of approved domains](#configure_domain)
   2. [Configure your website to host the 'apple-app-site-association' file](#configure_aasa)
6. [Stress results processing](#result_processing)
7. [Demo Applications](#demo)
8. [Questions](#questions)
9. [License and author info](#license)

# How to install <a name="install"></a>

### Requirements: <a name="requirements"></a>

* Android Studio 3.5 or later

No additional tools required.

### Installation guide: <a name="guide"></a>

* Clone the repository master brunch using ``` git clone https://github.com/Welltory/measure-stress-hrv-android.git ```
* Open project directory with the Android Studio 3.5+
* Run the project

# Example usage <a name="usage"></a>

1. In the DDS Example application press "Measure now" button to start measurement
<p>
  <img src="/screens/screen_1.jpg?raw=true" width="200" alt="Measure now">
</p>

2. Welltory application will launch and automatically starts a measurement
<p>
    <img src="/screens/screen_2.jpg?raw=true" width="200" alt="Measurement process">
</p>

3. After measurement complete, results sharing window will appear
<p>
  <img src="/screens/screen_3.jpg?raw=true" width="200" alt="Measurement result">
</p>

4. After user presses "ok, let's do it" button measurement results and user controll pull back to the DDS application
<p>
  <img src="/screens/screen_4.jpg?raw=true" width="200" alt="Presenting result">
</p>


# Integration <a name="integration"></a>

Welltory doesn't provide any integration SDKs, all applications interaction are performed with intents.

**You should start your integration filling out an [Integration Request Form](https://welltory.typeform.com/to/epJ3PR).**

Please send us a live chat message [on our website](https://welltory.com/) if you have any questions.


# Measurement request <a name="request"></a>

To start a measurement you should send URI intent

Important: The intent link changes depending on whether Welltory is installed or not.


### Measurement request link <a name="link"></a>

For the very first measurement from your application, launch the following link: [https://play.google.com/store/apps/details?id=com.welltory.client.android&referrer=<utf_8_encoded_params>](#)

Where the **referrer** contains:

* source - Your application name. Will be displayed in Welltory interfaces.
* callback - The application package and activity name divided with "/" symbol, to pass result data. ```com.welltory.dds.android/com.welltory.dds.android.MainActivity```, for example
* other parameters - [optionally] list of parameters to pass with measurement results. If you want to pass your own parameters to the measurement result, you should add them as query parameters to the URI

`Important: we DON’T save parameters in a database`\
`Important: Activity should have android:exported=”true” configuration`


It will take the user to the measurement screen in case Welltory is installed, or redirect them to Google Play page to install it.

Example:
[https://play.google.com/store/apps/details?id=com.welltory.client.android&referrer=source%3DYourApp%26callback%3Dcom.welltory.dds.android%2Fcom.welltory.dds.android.MainActivity%26param1%3Dtest_param1](#)

--------

Every following measurement request should be done using direct Welltory link: [welltory://branch/Measurement/Start/<utf_8_encoded_params>](#)


Where the **utf_8_encoded_params** contains:

* source - Your application name. Will be displayed in Welltory interfaces.
* callback - The application package and activity name divided with "/" symbol, to pass result data. ```com.welltory.dds.android/com.welltory.dds.android.MainActivity```, for example
* other parameters - [optionally] list of parameters to pass with measurement results. If you want to pass your own parameters to the measurement result, you should add them as query parameters to the URI

`Important: we DON’T save parameters in a database`\
`Important: Activity should have android:exported=”true” configuration`

Example: [welltory://branch/Measurement/Start/source%3DYourApp%26callback%3Dcom.welltory.dds.android%2Fcom.welltory.dds.android.MainActivity%26param1%3Dtest_param1](#)

`Important: Execute Intent with  FLAG_ACTIVITY_NEW_TASK and FLAG_ACTIVITY_CLEAR_TOP flags, to avoid a duplication of partner’s application instances.`

Examples:

```java

String callBackActivity = String.format(Locale.getDefault(), "%s/%s",
       activity.getPackageName(), activity.getClass().getName());
String params = String.format(Locale.getDefault(),
       "source=%s&callback=%s&param1=test_param1", "YourApp", callBackActivity);
Intent intent = null;
try {
   String encodedParams = URLEncoder.encode(params, "UTF-8");
   intent = new Intent(Intent.ACTION_VIEW, Uri.parse("welltory://branch/Measurement/Start/" + encodedParams));
   if (intent.resolveActivity(activity.getPackageManager()) == null) {
       intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.welltory.client.android&referrer=" + encodedParams));
   }
} catch (UnsupportedEncodingException e) {
   e.printStackTrace();
}
if (intent != null) {
   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
   activity.startActivity(intent);
}


```


# Stress results overview <a name="result"></a>

After the user data has been processed, Welltory application will use your **callback** activity to send measurement results to your application.
Welltory will put the following parameters into intent extras:


| name | type | description |
| ------ | ------ | ------ |
| stress | Float&nbsp;(0.0&nbsp;-&nbsp;1.0) | user's stress % |
| energy | Float (0.0 - 1.0) | user's energy % |
| productivity | Float (0.0 - 1.0) | user's productivity % |
| rmssd | Float | user's rmssd index |
| sdnn | Float | user's sdnn index |
| power | Float | user's power index |
| token | String | Measurement share token. It allows to open a measurement webpage https://app.welltory.com/share-measurement?token=<token> |
| productivity_c | String | Productivity parameter interpretation color |
| energy_c | String | Energy parameter interpretation color |
| stress_c | String | Stress parameter interpretation color |
| other parameters | String | Your custom parameters with keys and values you passed with URI

Colours:
* green - Good
* yellow - Normal
* red - Bad
* unknown - Unknown

# Stress results processing <a name="result_processing"></a>

After `startActivity` is called, partner’s application should expect for results in `onNewIntent` or `onCreate` function (in case Android kills application's activity). \
Measurement results could be fetched from the Intent by calling `Intent.getFloatExta`

Example:
```java
@Override
protected void onNewIntent(Intent intent) {
	super.onNewIntent(intent);
	resultView.setText(parseIntent(intent));
}

private String parseIntent(Intent data) {
	if (data != null && data.hasExtra("stress")) {
    	return String.format(Locale.getDefault(), "productivity=%s\nrmssd=%s\nenergy=%s\npower=%s\nstress=%s\nsdnn=%s",
            	data.getFloatExtra("productivity", -1),
            	data.getFloatExtra("rmssd", -1),
            	data.getFloatExtra("energy", -1),
            	data.getFloatExtra("power", -1),
            	data.getFloatExtra("stress", -1),
            	data.getFloatExtra("sdnn", -1));
	} else {
    	return null;
	}
}
```

# Demo Applications <a name="demo"></a>
This repository contains a working Demo DDS application.


# Questions? <a name="questions"></a>
If you have questions about the partnership, send us a live chat message [on our website](https://welltory.com/)

# License and author info <a name="license"></a>

```
Welltory Integration Android Example

The MIT License (MIT)

Copyright (c) 2019 Welltory Integration Android

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
