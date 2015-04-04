Potlatch
=============

This is my implementation of the Coursera "[Mobile Cloud Computing with Android](https://www.coursera.org/specialization/mobilecloudcomputing2/36?utm_medium=listingPage)" capstone project (2014).

Requirements
-----------------
###Basic:
1. App supports multiple users via individual user accounts2. App contains at least one user facing function available only to authenticated users3. App comprises at least 1 instance of each of at least 2 of the following 4 fundamental Android components: 	+ Activity	+ BroadcastReceiver	+ Service	+ ContentProvider4. App interacts with at least one remotely-hosted Java Spring-based service5. App interacts over the network via HTTP6. App allows users to navigate between 3 or more user interface screens at runtime7. App uses at least one advanced capability or API from the following list (covered in the MoCCA Specialization): multimedia capture, multimedia playback, touch gestures, sensors, animation.8.  App supports at least one operation that is performed off the UI Thread in one or more background Threads of Thread pool.
###Functional:1. App defines a Gift as a unit of data containing an image, a title, and optional accompanying text.2. A User can create a Gift by taking a picture (or optionally by selecting an image already stored on the device), entering a title, and optionally typing in accompanying text. 3. a. Once the Gift is complete the User can post the Gift to a Gift Chain (which is one or more related Gifts).    b. Gift data is stored to and retrieved from a web-based service accessible in the cloud.    c. The post operation used to store Gift data requires an authenticated user account4. Users can view Gifts that have been posted. 5. a. Users can do text searches for Gifts performed only on the Gift's title.    b. Gifts matching the search criterion are returned for user viewing.6. a. Users can indicate that they were touched by a Gift, at most once per Gift (i.e., double touching is not allowed)    b. Users can flag Gifts as being obscene or inappropriate. Users can set a preference that prevents the display of Gifts flagged as obscene or inappropriate.7. a. Touched counts are displayed with each Gift   b. Touched counts can be periodically updated in accordance with a user-specified preference (e.g., Touched counts are updated every 1, 5 or 60 minutes) or updated via push notifications for continuous updates.8. App can display information about the top “Gift givers,” i.e., those whose Gifts have touched the most people.

Screencast
-----------------

###Initial submission: (10 minutes)
[Video Link](http://vimeo.com/113167420)

###Recently improved version: (3 minutes)

![screenshot](https://github.com/fengsterooni/potlatch/blob/master/potlatch.gif)

Improvements since submission
-----------------
1. Used IntentService
2. Improved UI
3. Deployed to the cloud


Further improvement ideas
-----------------
1. Implement Content Provider
2. Further polish UI
3. Use Database at Server side (Spring JPA)
4. Add more features - user profile, pictures from photo gallery, geo location support, etc

Develop Environment
-----------------

Client -- Android Studio 1.1.0

Server -- Spring Tool Suite (STS) 3.6



License
--------

    Copyright 2015 Feng Guo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    