# OverView
With help i was able to develope this process.

Written in ### (Flutter+Java) ####




Location Tracking 

I am using GPS Provider instead of Network Provider.

It is a service that runs in background in its own thread to execute some code even after the app is killed.

The main job of this code is to Get location and send it to server or store it in offline database SQLite.

There are 2 scenerios for this app to work

# App is Online


###########################################

When App is online and the user starts the service-:

The app will start the service in 1 minute after pressing the button and then it will run in background after every 1 minute continously.
Now, after Android 8+ to run anything in the background needs a notification channel.
Whenever the app gets the location it will produce a notification that we can customize according to ourselves.
After getting the data it will first check for-
internet connection 
if internet is on
{
send data online to database
and check if the the offline database is empty or not
if 
{
offline SQLite database is empty
{
Nothing
}
else
{
Take the data and send it online
}
###########################################

Now the 2nd part of program

# App is Offline

if the device is in offline state-
This is rather intersting.We are using SQLIte instead of SharedPreference but you can use either of them.Here what is happening is that when user is offline the GPS is getting the location and adding it into the offline database that is then accessed when internet is on to send all that congregated data online .

The data includes 
Location
Battery Percentage 
Is Plugged In or Plugged Out
Airplane Mode is On or OFF

# NOTES

I have also added permissions to main page


Make sure to keep the app out of Battery Optimization because of some OEM's way of handling background process is different.
MIUI ONE-UI Color-OS Oxyzen-OS(these tend to close the service running in backround as soon as the service is enabled approx 20sec) Stock-OS(May vary but i tried it in Pixel Experience and got the same result but on my MIA1 with stock Pixel OS it was woring for some strange reasones)


