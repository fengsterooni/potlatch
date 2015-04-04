# Potlatch Service

## Overview

This is the Potlatch Service project for the Capstone project of Coursera "Mobile Cloud Computing with Android" specializations.

The project is based on the Video project for "Programming Clound Services for Android Handheld Systems" course. This project "reuses" the Oauth authentication part of the course project.

## Warning

UNDER NO CIRCUMSTANCES SHOULD YOU USE THE INCLUDED KEYSTORE IN A PRODUCTION APP!!!
UNDER NO CIRCUMSTANCES SHOULD YOU USE THIS APP "AS IS" IN PRODUCTION!!!

## Running the Application

Please read the instructions carefully.

To run the application:

1. Right-click on the Application class in the assignment project->Run As->Java Application (the 
   application may try to start and fail with an error message - this is OK). If the application
   successfully starts, stop the application before proceeding to the next step.
2. (Menu Bar) Run->Run Configurations
3. Under Java Applications, select your run configuration for this app's Application class that
   was just created in step 1 (if you select the run configuration, it should list the assignment
   as the project name)
4. Open the Arguments tab
5. In VM Arguments, provide the following information to use the
   default keystore provided with the sample code:

   -Dkeystore.file=src/main/resources/private/keystore -Dkeystore.pass=changeit

6. Note, this keystore is highly insecure! If you want more security, you 
   should obtain a real SSL certificate:

   http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
   
7. This keystore is not secured and should be in a more secure directory -- preferably
   completely outside of the app for non-test applications -- and with strict permissions
   on which user accounts can access it


