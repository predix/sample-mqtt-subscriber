# Sample MQTT subscriber web app
This is spring boot app that acts as MQTT subscriber to any MQTT broker. It expects the user to enter the MQTT broker connection info (`hostname` and `port`) and subscribes to `accelerometer` topic on that MQTT broker. It then displays the data published on `accelerometer` topic on a chart. It expects the data to be numeric.

This app was used to demo TCP router functionality in Cloud Foundry Summit 2016.

## Known issues
The websocket implementation makes this app to be a single user app due to the way websocket session is used. Since this was used for demo no efforts were done to make it multi-user. If you need to make it a multi-user app, please submit a PR and we will be happy to pull it in.
