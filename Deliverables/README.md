# Sagrada

Progetto ingegneria del software 2017/2018

## Gruppo 41
* **10494854 Alampi** Giovanni
* **10520963 Bignoli** Andrea
* **10537156 Chiappin Airoldi** Federico

## Deployment

Starting of server can be made by either executing server-start.sh or launching server.jar
Starting of server can be made by either executing client-start.sh or launching client.jar

### To set up server
Parameter for setting up server can be modified using NetworkInfo.json
* socket port -> number of port used by socket communication
* rmi server port -> number of port used by rmi communication
* get local ip -> ip address where the server is listening

### To set up game parameter
* game is starting time -> length of pre game timer, starts after 2 players are connected (millisecond)
* time until random card -> length of time period that user have to choose their window card, before randomising one (millisecond)
* action timer -> lenght of time period user have for their turn before automatically passing (millisecond)

### Advanced functionality
There is the possibility to add personalized window card on server side. Cards can be added only at couple, with same
id parameter but different name parameter. In order to add a window card:
* WindowCArd.json need to be updated with the card, using same vector format of the file
* 2 images of the card, in .png format, need to be added to resources/img/windowCard, each named with name of window card it represents
* number window card parameter of GameSettings.json needs to be updated, + 1 for each couple of new card added
