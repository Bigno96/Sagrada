## Protocol

## Connection Messages

* **connection message**
    Socket <TAB>(IP) <TAB>(Port)CR

* **connection answer**
    "Socket connection OK!"CR

* **login message**
    login <TAB>(user) <TAB>(Password)CR

* **login answer**
    "logged" <TAB>(userID)CR

* **startGame message**
    start CR

* **startGame answer**
    "Wait a moment, the game will start shortly"CR

* **turn message**
    "Is your turn"CR

* **endTurn message**
    "End turn"CR

* **endTurn answer**
    "Your turn is over"CR

* **endConnection**
    "Connection closed!"CR

* **Error Code**
    "Description"

## Game Messages

* **ChooseWindowCard**
    ChooseWindowCard <TAB>(NumWindowCard)CR

* **MoveDiceInWindowCard**
    MoveDice <TAB>(NumDice)CR(Col) <TAB>Row)CR

* **UseToolCard**
    Tool <TAB>(NumOfToolCard)CR
* **Moves answer**
    OK CR
