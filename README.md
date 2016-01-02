# currency_converter_yahooapi
Android Currency Converter using Yahoo Finance API (YQL) and implements Text change Listener.

The Yahoo Finance API : https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%3D%22USDINR%2C%20EURINR%2CCADINR%2CJPYINR%2CGBPINR%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys

The above REST Query is generated from YQL and returns data in JSON format.
The curreny rates are updated each time the app is opened and connected to internet. The background task of updating the currency rates is done using AsyncTask. It also implements the new HttpURLConnection Android API over the deprecated ones (Android 6.0 release removes support for the Apache HTTP client).

The YQL used : SELECT * FROM yahoo.finance.xchange WHERE pair IN ("USDINR", "JPYINR")

Visit YQL Console : https://developer.yahoo.com/yql/console/
