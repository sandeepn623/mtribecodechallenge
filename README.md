# mtribecodechallenge
Contians source code for code challenge


#### mtribedevchallenge

### Prerequisites

- Android Studio
- Java JDk 1.8
- Android SDK minimum API level 15 for installing the apk but the project is built using sdk version 26

#### Libraries used in the project

-Room - Room is a persistence library, part of the Android Architecture Components. It makes it easier to work with SQLiteDatabase               objects in your app, decreasing the amount of boilerplate code and verifying SQL queries at compile time. 

<strike>-Jackson - JSON library for Java (or JVM platform in general), or, as the "best JSON parser for Java." Or simply as "JSON for Java."</strike>

GSON - Gson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object.

<strike>-OkHttp - An HTTP & HTTP/2 client for Android and Java applications. For more information see [the website][1] and [the wiki][2].</strike>

RetroFit 2 - Type-safe HTTP client for Android and Java by Square, Inc.

-Data Binding - Android offers support to write declarative layouts using data binding. This minimizes the necessary code in your                         application logic to connect to the user interface elements.

- Lifecycle-aware components

- ViewModels

- LiveData

### Workflow of the application

The app nam is CarFeedApp. Once installed it can be found in All App screen on the phone with **m-tribe logo.

When the user clicks on the launcher icon:

<strike>1. A splash screen is displayed and will be shown util the data is fetched from rest api http://data.m-tribes.com/locations.json
  and inserted into database.</strike><br/>
1. A splash screen is displayed and will be shown for 5 seconds.
2. This network sync is done only once during first time launch and subsequently the data is loaded from DB, thereby minimizing            unecessary calls to servers. The data is fetched from rest api http://data.m-tribes.com/locations.json
2. Once the data is persisted on the DB, the user is navigated to main screen which displays a list of all the car details.
3. The list items shows the details of the car such as<br/>
    a. Name <br/>
    b. Address <br/>
    c. Engine type <br/>
    d. Interior condition<br/>
    e. Exterior condition<br/>

<br/><strike>4. The main screen has refresh option. When clicked it makes a new rest call to fetch updated data.</strike><br/>
5. The also has a Floating Action Button, which is used to diplay the user a map screen with makers of all the cars at given                coordinates. The user is navigated to current location or default location based on whether the location service is enabled.<br/>
6. All the car information markers are red and current location marker is green.<br/>
7. The user can navigate to current location with the help location button on top right hand corner of the screen.<br/>
8. There is Zoom controls at the bottom right corener of the screen.<br/>
9. On cliking the maker the a info window with car name is shown and rest of the markers are hidden. Clicking the marker again reverses    the operation.<br/>

## Error Scenarios
<strike>1. When the app is launched and if the network connections are disabled. The app throws a alert promting the user to turn on the network and try agin. The app is terminated once the user confirms.</strike><br/>
2. When the location services are disabled on the phone the user is prompted to enable the location service from settings and he is        navigated to a defult location. The location button is is disabled and hidden from the user until the location services are not          enabled.

### Design

1. The application is based on mvvm pattern. In mvvm architecture, Views react to changes in the ViewModel without being explicitly        called. 
2. The http request are made using <strike>okHttp</strike> Retrofit.
3. The <strike>jackson</strike> Gson parser is used to parser json to Java POJO.
4. The ViewModel interacts with datarepository to get the data and updates the View through databiding.
5. The recylerviews are used instead of normal listviews. CardView are user to diplay each item on the list.



### Future Improvements
<strike>1. Fine tune the abtraction and the dependencies between http and database.</strike><br/>
2. Smooth scrolling to the marker when the user clicks a marker.
3. optimize the load of diplaying the markers using clustering or KML concept.
