
# Arrays and Lists of Objects

In this tutorial, we'll see how to use the *Sinbad* library to access collections of objects. If you have followed along the [Fetching Objects](welcome02-obj.md) tutorial, there is really not much difference!

### Required Java Concepts

* Arrays
* ArrayLists

## Defining a Class

As in the [Fetching Objects](welcome02-obj.md) tutorial, let's define a class that the *Sinbad* library will use to create a collection of objects. Go ahead and define a class named **WeatherStation** that has fields

* *name* (String)
* *id* (String)
* *state* (String)
* lat (double)
* lng (double)

Also, provide the following methods for the class:

* `getId` (returns the `id`)
* `getName` (returns the `name`)
* `isLocatedInState` (returns a boolean, whether the weather station is located in the given state)

With your class definition set (here's my [WeatherStation](WeatherStation.java)), go on to the next section and/or the one after it, depending on whether you want to access an array or an `ArrayList` of data.

## Fetching WeatherStation Data: Array

We'll use the following URL to access a list of all weather stations utilized by the NOAA's National Weather Service: [weather.gov/xml/current_obs/index.xml](http://weather.gov/xml/current_obs/index.xml). Connect to and load the data as usual by defining a `DataSource` object and using the `connect` and `load` methods. Then, use the **`fetchArray`** method (instead of just `fetch`) to retrieve an array of data for all the weather stations. Here's what I have:

````
DataSource ds = DataSource.connect("http://weather.gov/xml/current_obs/index.xml").load();
WeatherStation[] allstns = ds.fetchArray("WeatherStation", "station/station_name", 
                                         "station/station_id", "station/state",
                                         "station/latitude", "station/longitude");
System.out.println("Total stations: " + allstns.length);
````

You should get a line printed out like:

````
Total stations: 2652
````

That means that `allstns` is a array of about 2,600 objects of the `WeatherStation` class you defined! 

Now, you can do things with this array. For example, let's filter and print out only those that are located in a particular state:

````
String state = "GA";
````

* Write a loop that iterates through all over the weather station objects and prints out the id and name of those located in the given state.
* Provide some way for the user to input the state abbreviation. 
  * If you're using Java, you can use a `Scanner`:
  
        import java.util.Scanner;
        ...
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a state abbreviation: ");
        String state = sc.next();
        
    Here's my complete [Welcome03.java](Welcome03.java) program.

  * If you're using Processing, you can use a dialog box:
  
        import static javax.swing.JOptionPane.*;
        ...
        
        String state = showInputDialog("Type in a state name...");
        if (state != null) {  // only when the user presses the 'ok' button
           ... // display the results
        }


## Fetching WeatherStation Data: ArrayList

We'll use the following URL to access a list of all weather stations utilized by the NOAA's National Weather Service: [weather.gov/xml/current_obs/index.xml](http://weather.gov/xml/current_obs/index.xml). Connect to and load the data as usual by defining a `DataSource` object and using the `connect` and `load` methods. Then, use the **`fetchList`** method (instead of `fetch`) to retrieve an `ArrayList` of data for all the weather stations. Here's what I have:

````
DataSource ds = DataSource.connect("http://weather.gov/xml/current_obs/index.xml").load();
ArrayList<WeatherStation> allstns = ds.fetchList("WeatherStation", "station/station_name", 
                                                 "station/station_id", "station/state",
                                                 "station/latitude", "station/longitude");
System.out.println("Total stations: " + allstns.size());
````

You should get a line printed out like:

````
Total stations: 2652
````

That means that `allstns` is a list of about 2,600 objects of the `WeatherStation` class you defined! 

Now, you can do things with this list. For example, let's filter and print out only those that are located in a particular state:

````
String state = "GA";
````

* Write a loop that iterates through all over the weather station objects and prints out the id and name of those located in the given state.
* Provide some way for the user to input the state abbreviation. 
  * If you're using Java, you can use a `Scanner`:
  
        import java.util.Scanner;
        ...
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a state abbreviation: ");
        String state = sc.next();
        
    Here's my complete [Welcome03_List.java](Welcome03_List.java) program.

  * If you're using Processing, you can use a dialog box:
  
        import static javax.swing.JOptionPane.*;
        ...
        
        String state = showInputDialog("Type in a state name...");
        if (state != null) {  // only when the user presses the 'ok' button
           ... // display the results
        }




## Exercises

Here are some extensions to the program above you can try working on. 

* Using your `Observation` class from the [Fetching Objects](welcome02-obj.md) tutorial, add a method `currentWeather` to the `WeatherStation` class that uses the `id` of the weather station to connect to a `DataSource` at the URL `"http://weather.gov/xml/current_obs/" + id + ".xml"`. If the weather source has data for the fields `"temp_f", "weather", "wind_degrees"`, then fetch and return an `Observation` object with that data. 

* Develop a `WeatherReporter` class whose constructor takes (1) an array or list of weather stations and (2) a state abbreviation. 
  * The weather reporter object should filter the array/list of stations and save only those in the given state.
  * Define a `report` method in the `WeatherReporter` class that prints out the id of each weather station and its observation information. It should also report on the average temperature as well as the coldest (or warmest) station.
  
At the bottom of this page is a link to a solution that also fetches and uses date/time information.

-------

Sample solution (Java): [Welcome03Full.java](Welcome03Full.java)
