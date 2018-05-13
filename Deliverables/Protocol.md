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

* **start Game message**
    start CR

* **start Game answer**
    "Wait a moment, the game will start shortly"CR

* **end Connection**
    "Connection closed!"CR

* **Error Code**
    "Description"CR

## Game Messages

* **ChooseWindowCard**
    ChooseWindowCard <TAB>(NumWindowCard)CR

* **ranking message**
    "The First Player is" <TAB>(Username)CR
    "With" <TAB>(PlayerPoints)CR
    "The Second Player is" <TAB>(Username)CR
    "With" <TAB>(PlayerPoints)CR
    "The Third Player is" <TAB>(Username)CR         //only if the number of players is >3
    "With" <TAB>(PlayerPoints)CR
    "The First Player is" <TAB>(Username)CR         //only if the number of players is 4
    "With" <TAB>(PlayerPoints)CR

## Turn Messages

* **MoveDiceInWindowCard**
    MoveDice <TAB>(NumDice)CR(Col) <TAB>Row)CR

* **UseToolCard**
    Tool <TAB>(NumOfToolCard)CR

* **Moves answer**
    "OK" CR

* **Illegal move answer**
    "Your move is wrong"CR

* **No Move**
    NoMoveCR

* **Roll Dice**
    RollDiceCR

* **Roll Draft**
    RollDraftCR

* **turn message**
    "Is your turn"CR

* **end Turn message**
    "End turn"CR

* **end Turn answer**
    "Your turn is over"CR

## Round Messages

* **new round message**
    "New round is starting"CR

* **end round message**
    "The round is over"CR

* **first player**
    "You are the first player"CR

## End Game

* **end game message**
    "The game is over"CR

* **play again?**
    "Do you want play again?"CR

* **Yes, replay**
    YesReplayCR

* **No, replay**
    NoReplayCR