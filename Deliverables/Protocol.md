# Protocol

## Connection Messages

### Initialization

* **login username** </br>
    Client -> Server </br>
    login <TAB>\<user> <TAB><password> </br>
    
    explanation: Client sends his username for authentication
    
* **login authentication successful reply** </br>
    Server -> Client </br>
    logged <TAB>\<userID> </br>

    explanation: Server answer is "logged" and userID if it was successful

* **login authentication failed reply** </br>
    Server -> Client </br>
    Your username or password are wrong </br>

    explanation: Server reply is "Your username or password are wrong" if authentication failed

* **start Game waiting reply** </br>
    Server -> Client </br>
    Wait a moment, the game will start shortly </br>

    explanation: Server answer is "Wait a moment, the game will start shortly" if it was successful

* **impossible to start** </br>
     Server -> Client </br>
     Impossible to start a game </br>

     explanation: Server answer is  "Impossible to start a game" if is impossible to start a new game
     
* **log out message** </br>
    Client -> Server</br>
    log out </br>
    
    explanation: Client sends "logOut" if he want close the connection
     
## Game Messages

### Window Card

* **ChooseWindowCard message** </br>
    Server -> Client </br>
    Choose your windowCard between: </br>
    Num1 <TAB>\<IDWindowCard> </br>
    Num2 <TAB>\<IDWindowCard> </br>
    Num3 <TAB>\<IDWindowCard> </br>
    Num4 <TAB>\<IDWindowCard> </br>

    explanation: server sends the four windowCards for the choice

* **ChooseWindowCard reply** </br> 
    Client -> Server </br>
    ChooseWindowCard <TAB>\<NumWindowCard> </br>
    
    explanation: client chooses the number of his card
    
### Tool Card

* **ToolCard message** </br>
    Server -> Client </br>
    These are the toolCards: </br>
    Num1 <TAB>\<IDToolCard> </br>
    Num2 <TAB>\<IDToolCard> </br>
    Num3 <TAB>\<IDToolCard> </br>

    explanation: server sends the three toolCards
    
### Public Objective Card

* **Public Objective message** </br>
    Server -> Client </br>
    These are the publicObjectiveCards: </br>
    Num1 <TAB>\<IDPubObj> </br>
    Num2 <TAB>\<IDPubObj> </br>
    Num3 <TAB>\<IDPubObj> </br>

    explanation: server sends the three publicObjectiveCards
    
### Private Objective Card

* **Private Objective message** </br>
    Server -> Client </br>
    These are the privateObjectiveCards: </br>
    \<IDPrivObj> </br>
    
    explanation: server sends the three privateObjectiveCards
    
### Ranking

* **ranking message** </br>
    Server -> Client </br>
    The First Player is <TAB>\<Username> </br>
    With <TAB>\<PlayerPoints> </br>
    The Second Player is" <TAB>\<Username> </br>
    With <TAB>\<PlayerPoints> </br>
    The Third Player is" <TAB>\<Username> //only if the number of players is >3 </br>
    With <TAB>\<PlayerPoints> </br>
    The First Player is" <TAB>\<Username> //only if the number of players is 4 </br>
    With <TAB>\<PlayerPoints> </br>
    
    explanation: at the end of the game, server sends the ranking of all players
   
## Turn Messages

* **turn message** </br>
    Server -> Client </br>
    "Is your turn" </br>

    explanation: server sends "Is your turn" at the client, when is his turn

* **Roll Draft** </br>
    Client -> Server </br>
    RollDraft </br>

    explanation: during the first turn of the round, the first player sends "RollDraft" to roll dice of the Draft

* **MoveDiceInWindowCard** </br>
    Client -> Server </br>
    MoveDice <TAB>\<NumDice> </br>
    \<Col> <TAB>\<Row> </br>

    explanation: during his turn, client chooses a dice in the draft and puts it in him windowCard
    
* **UseToolCard** </br>
    Client -> Server </br>
    Tool <TAB>\<NumOfToolCard> </br>

    explanation: during his turn, when client wants use a toolCard send "Tool (NumOfToolCard)"
    
* **Roll Dice** </br>
    Client -> Server </br>
    RollDice </br>

    explanation: during his turn, when the client needs to roll a dice, he sends "RollDice"

* **Moves reply** </br>
    Server -> Client </br>
    "OK" </br>

    explanation: server reply is "Ok" when the move (UseToolCard or MoveDiceInWindowCard) is successful

* **Illegal move message** </br>
    Server -> Client </br>
    "Your move is wrong" </br>

    explanation: server answer is "Ok" when the move (UseToolCard or MoveDiceInWindowCard) is an illegal move

* **Pass message** </br>
    Client -> Server</br>
    Pass</br>

    explanation: during his turn, client sends "Pass" if client wants to finish the turn without another moves
    
* **pass reply** </br>
    Server -> Client </br>
    "End turn" </br>

    explanation: server sends "End turn" at the client replying "Pass" message from client

* **end Turn timer off message** </br>
    Server -> Client </br>
    "The timer is over, your turn is gone" </br>

    explanation: server sends "The timer is over, your turn is gone" at the client, when the timer is over

## Round Messages </br>

* **new round message** </br>
    Server -> Client </br>
    "New round is starting" </br>

    explanation: server sends "New round is starting" at all clients, when the last round finished, and the new round
    is starting
 
* **first player** </br>
    Server -> Client </br>
    "You are the first player" </br>

    explanation: when the last round finished, server sends "You are the first player" at the first client of the round

* **end round message** </br>
    Server -> Client </br>
    "The round is over" </br>

    explanation: server sends "The round is over" at all clients, when the the following turn is ending
    
## End Game

* **end game message** </br>
    Server -> Client </br>
    "The game is over" </br>

    explanation: when the tenth round finished, server sends "The game is over" at all clients

* **play again?** </br>
    Server -> Client </br>
    "Do you want play again?" </br>

    explanation: after the "end game message", server sends "Do you want play again" to all clients
    
* **close connection request** </br>
    Client -> Server </br>
    close_connection </br>
    
    explanation: Client send "close_connection" when he wants to close the connection

* **Yes, replay** </br>
    Client -> Server </br>
    YesReplay </br>

    explanation: client answer "Yes, replay", at the server, if he want play another game

* **No, replay** </br>
    Client -> Server </br>
    NoReplay </br>

    explanation: client answer "No, replay", at the client, if he doesn't want play again
