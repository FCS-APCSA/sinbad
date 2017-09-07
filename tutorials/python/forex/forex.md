# Exercises: Currency Exchange

Welcome! This set of exercises uses *Sinbad* to explore current and historical currency exchange rates. The exercises are grouped according to the programming concepts that are required to complete each one. With each part, we'll make a note of the Python concepts that you need to have covered to be able to follow along.

* We assume you have already gone through the [Welcome Tutorial](../welcome/welcome01.md) first before working through this activity.

### Sinbad

Recall that to access any data source using *Sinbad*, there are three basic steps you carry out:

  1. _Connect_ to the data source by URL or filename
  2. _Load_ all the data
  3. _Fetch_ elements of interest from the data

### Data Source

Use the data provided by the European Central Bank at this URL: http://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html. This provides conversion rates from Euros to various other currencies. Again, remember that the base, or reference, currency is the Euro.

Scroll about two-thirds of the way down the page and you should see a section of "Downloads" with a link for a CSV file of "Current reference rates". Right-click on that link and copy the link address. (If you want, you can also (left) click on it to download the file to your computer and open it in Excel.) The link should be something like: `http://www.ecb.europa.eu/stats/eurofxref/eurofxref.zip?...`


## Part 1 - Euro-to-Dollar Exchange Rate

### Required Python Concepts

* Basic data types - string, float
* Variables
* Using (i.e. calling) functions/methods
* `import`ing a library
* Using `print` to display text in the console

### Task

Write a Python script that prints out the current EUR-USD (Euro to US Dollar) conversion rate and the date associated with it. Your program should print output that looks like:

      As of 06 September 2017 , 1 Euro = 1.1931 Dollars
      
(The value printed by your program should match what you get if you search Google for the [EUR-to-USD exchange rate](https://www.google.com/search?q=euro+to+usd).)

### Hints

* The `connect` function always requires one argument: the URL of the data service. Additionally, if the format of the data is not completely obvious from the URL (like in this case, where the link is actually for a 'zip' archive file), we can provide the `connect` function an additional `format` parameter to help clarify that, like this:

      ds = DataSource.connect("http://www.ecb.europa.eu/stats/eurofxref/eurofxref.zip?...", format="csv")
      
    where the `...`s in the URL should be filled in with the actual CSV file link that you copied above.

* The labels of interest that you need to fetch from the data are `"Date"` and `"USD"`. Use the `fetch` method to fetch the date as a string and `fetch_float` to fetch the USD conversion rate as a floating point number. 
  
         date = ds.fetch("Date")
         euro_usd = ds.fetch_float("USD")
          
* Remember, you can use the `print_description` method to figure out what other pieces of data are available. You should get a listing like:

````
-----
Data Source: http://www.ecb.europa.eu/stats/eurofxref/eurofxref.zip?...

The following data is available:
dictionary with {
     : *
    AUD : *
    BGN : *
    BRL : *
    ...
    Date : *
    DKK : *
    JPY : *
    USD : *
    ...
   }
-----
````

### Aside: Financial Data, Numbers, and Programming 

Be aware that while we are using `float` values to represent financial data (exchange rates) for the purpose of these exercises, in general, floating point numbers **do not** maintain enough precision to prevent accumulation of rounding errors. For instance, `round(2.675, 2)` in Python produces `2.67` while `round(2.875, 2)` produces `2.88`. The Python tutorial discusses [this more here](https://docs.python.org/3.6/tutorial/floatingpoint.html).

## Part 2 - Conversion Rates

### Required Python Concepts

* Arithmetic operations
* Console input

### Task

Write a Python script that displays the following conversion rates:

* Euros to dollars (fetch the `USD` data)
* Euros to British pounds (`GBP`)
* Dollars to euros 
* Dollars to pounds

### Task

Extend your program to enable the user to enter a numeric amount and source and target currency codes. The program should perform and display the result of the conversion of the given amount in the source currency to the target currency. For example:

````
Enter amount: 4000
Enter source currency code: USD
Enter target currency code: EUR

4000 USD equals 3500 EUR as of 06 September 2017
````


## Part 3 - Currencies: Stronger and weaker

While the strength or weakness of one currency against another depends on several factors, a simple metric that can be used is the exchange rate. For example, if 1 EUR can be exchanged for less than 1 unit of another currency, we'll say that it is weaker; on the other hand, if 1 EUR is exchanged for more than one unit of another currency, we'll say that it is stronger. 

### Required Python Concepts

* Conditional statements (`if`)

### Task

* Define a variable storing a target currency:

       target = "USD"
       
* Write a Python script that fetches the exchange rate for the target currency and prints out whether the Euro is stronger than it or not. You should be able to change the value of the `target` variable to something else, e.g. 

       target = "JPY"

  and the rest of the program should produce the appropriate output.
  
  
## Part 4 - Functions

### Required Python Concepts

* Defining functions
* using `round()`

### Task

* Based on insight from how you computed the dollars to pounds exchange rate in Part 2 above, write a function,

      def currency_rate(source, target):

  that produces (`return`s) the conversion rate from one currency to another, rounded to the nearest hundredth. For example, if the EUR-to-FOO rate is 2.0 and the EUR-to-BAR rate is .4, then `currency_rate("FOO", "BAR")` should produce `0.2`. You will need to look up the Euro-based conversion rates for `FOO` and `BAR` in the data source.
  
   Make sure that your function works correctly if either `source` or `target` is `"EUR"`, and also if `source` and `target` are the same. In the latter case, it should always produce `1.0` as the rate.
   
   
## Part 5 - Historical Data

The [site above](http://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html) also provides "Time series" historical exchange rates based on the Euro. Scroll all the way to the bottom of the page and copy the CSV (.zip) link for the time series data. It should be something like `http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.zip?...`.

If you download and look at this data in Excel, you'll see that it is a series of rows with currency rates for almost every day of the last 15+ years. 

### Tasks (A)

* Connect to and load the historical exchange rate data. Print a description of the available data. You should see something like:

````
The following data is available:
list of:
  dictionary with {
    AUD : *
    ...
````

  Notice that this is providing you a **list** of data records. In a later task, you will access the data as a list in Python. For now, we'll use a version of the `fetch` methods that allow you to select items at a particular position in the list. If you use `ds.fetch_first_float("USD")` on the list of historical data, it will give you the first USD value in the list. There is another method, `fetch_ith_float` which lets you specific any particular offset in the list for which you want to select a value. So the following two statements are equivalent:
  
      euro_usd_now = ds.fetch_first_float("USD")
      euro_usd_now = ds.fetch_ith_float(0, "USD")

  Try printing out the value returned each time and make sure they are the same.
  
* Now try accessing the 100th USD entry in the list of data:

      euro_usd_100 = ds.fetch_ith_float(100, "USD")
      
  This entry will be the exchange rate for approximately 100 days ago (it will probably be for a bit farther back, because the data skips some days). You can look up the corresponding dates for the entries using:
  
      ds.fetch_first("Date")
      ds.fetch_ith(100, "Date")

* Write a program that computes how much money in USD you would have today if 100 time periods ago you started with $2000, converted it into Euros at that point, and then today converted the Euros back into dollars. Did the dollar become stronger or weaker since then?

* Develop a function:

      def hist_currency_rate(source, target, periods_ago):
      
  that produces the exchange rate from `source` to `target` the given number of `periods_ago` in the historical data.
  
* Use your function to determine how much money in USD you would have today if 200 time periods ago you started with $5000, converted it to Yen (JPY) at that point, and then converted the Yen back into dollars today. (Passing `0` for `periods_ago` should produce the current exchange rate.)


### Required Python Concepts

* Indexed loop

### Task (B)

* Write a program that determines the date at which the dollar was trading strongest against another currency of your choice (EUR, JPY, etc.) You can get the total number of time periods using `ds.data_length()`.

  _Hint:_ Once you have determined the position in the list of the strongest value of the dollar, you can use that position value to look up the corresponding date in the data.
  
## Part 6 - Lists and Structured Data

### Required Python Concepts

* Dictionaries
* Lists

### Fetching Data

Using `ds.fetch()` will produce all the available data in the structure that is described by `ds.print_description()`. For example, with the first currency exchange data source that we used, `ds.fetch()` will return a dictionary value:

````
{'AUD': ' 1.4961',
 'BGN': ' 1.9558',
 'BRL': ' 3.7145',
 'CAD': ' 1.4787',
 ...
 'Date': '06 September 2017',
 ...
 'USD': ' 1.1931',
 'ZAR': ' 15.3965'}
````

With the historical exchange data, `ds.fetch()` would provide a *list* of such dictionary values.

### Ideas


* Write a function `is_currency_code(s)` that determines if the given string looks like a currency code. For example, `"USD"` is a currency code; `"Date"` or `"_col_42"` are not.
  
  Use `ds.field_list()` to obtain a list of the labeled elements in the data, i.e. `['AUD', 'BGN', 'BRL', 'CAD', 'Date', ..., 'USD', ...]`. For each element of the list that is a currency code, write a program that displays the percent change (+/-) of the Euro exchange rate against that currency. You need to access the current exchange rate and the immediately previous one (e.g. `ds.fetch_ith_float(0, "USD")` and `ds.fetch_ith_float(1, "USD")`).


* Develop a function `change_reference(data, old_base, new_base)` that takes a dictionary value containing currency exchange rates relative to `old_base` (such as produced for the Euro by `ds.fetch()`) and produces a new dictionary that contains currency exchange rates relative to `new_base` instead. Note that the `'Date'` value should be retained, and the entry for `new_base` in the given `data` will be replaced with a key/value pair for `old_base` instead. 

  For example, `change_reference( { 'USD' : 1.2, 'JPY' : 150, 'Date' : 'today' }, 'EUR', 'USD')` should produce `{'EUR': 0.8333, 'JPY': 125.0, 'Date' : 'today' }`.

* Determine the strongest and weakest currencies against the Euro. You can use `ds.field_list()` to obtain a list of the labeled elements in the data, i.e. `['AUD', 'BGN', 'BRL', 'CAD', 'Date', ..., 'USD', ...]`. Remove any strings from the list that are not 3 uppercase letters long. Then you have a list of the currencies for which values are available and you can use `fetch_float` accordingly.

* Incorporate another data set like [this one](https://raw.githubusercontent.com/datasets/currency-codes/master/data/codes-all.csv) to look up and display currency names instead of just their alphabetic codes.

* For a given currency, determine the high and low exchange rate against the Euro in the period covered by the historical data. How does the current exchange rate compare against the high and low?
