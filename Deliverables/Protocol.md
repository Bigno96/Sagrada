## Protocol

## Connection Messages

* **connection message**
    Client -> Server
    Socket <TAB>(IP) <TAB>(Port)CR

    explanation: Client starts connection

* **connection answer**
    Server -> Socket
    "Socket connection OK!"CR

    explanation: Server sends "Socket connection OK!" if it was successful

* **login message**
    Client -> Server
    login <TAB>(user) <TAB>(Password)CR

    explanation: Client sends his user and password for authentication

* **login answer**
    Server -> Client
    "logged" <TAB>(userID)CR

    explanation: Server answer is "logged" and userID if it was successful

* **login authentication failed answer**
    Server -> Client
    "Your username or password are wrong"CR

    explanation: Server answer is "Your username or password are wrong" if authentication failed

* **start Game message**
    Client -> Server
    start CR

    explanation: Client send "start" when he wants to start a game

* **start Game answer**
    Server -> Client
    "Wait a moment, the game will start shortly"CR

    explanation: Server answer is "Wait a moment, the game will start shortly" if it was successful

* **impossible to start**
     Server -> Client
     "Impossible to start a game"CR

     explanation: Server answer is  "Impossible to start a game" if is impossible to start a new game

* **end Connection**
    Server -> Client
    "Connection closed!"CR

    explanation: Server sends "Connection closed!" if the connection go out

* **Error Code**
    "Description"CR

    explanation: generic error messages

## Game Messages

* **ChooseWindowCard message**
    Server -> Client
    "Choose your windowCard between:"CR
    "Num1" <TAB>(IDWindowCard)CR
    "Num2" <TAB>(IDWindowCard)CR
    "Num3" <TAB>(IDWindowCard)CR
    "Num4" <TAB>(IDWindowCard)CR

    explanation: server sends the four windowCards for the choice

* **ChooseWindowCard answer**
    Client -> Server
    ChooseWindowCard <TAB>(NumWindowCard)CR

    explanation: client chooses the number of his card

* **ranking message**
    Server -> Client
    "The First Player is" <TAB>(Username)CR
    "With" <TAB>(PlayerPoints)CR
    "The Second Player is" <TAB>(Username)CR
    "With" <TAB>(PlayerPoints)CR
    "The Third Player is" <TAB>(Username)CR         //only if the number of players is >3
    "With" <TAB>(PlayerPoints)CR
    "The First Player is" <TAB>(Username)CR         //only if the number of players is 4
    "With" <TAB>(PlayerPoints)CR

    explanation: at the end of the game, server sends the ranking of all players

## Turn Messages

* **MoveDiceInWindowCard**
    Client -> Server
    MoveDice <TAB>(NumDice)CR(Col) <TAB>Row)CR

    explanation: during his turn, client chooses a dice in the draft and puts it in him windowCard

* **UseToolCard**
    Client -> Server
    Tool <TAB>(NumOfToolCard)CR

    explanation: during his turn, when client wants use a toolCard send "Tool (NumOfToolCard)"

* **Moves answer**
    Server -> Client
    "OK" CR

    explanation: server answer is "Ok" when the move (UseToolCard or MoveDiceInWindowCard) is successful

* **Illegal move answer**
    Server -> Client
    "Your move is wrong"CR

    explanation: server answer is "Ok" when the move (UseToolCard or MoveDiceInWindowCard) is an illegal move

* **No Move**
    Client -> Server
    NoMoveCR

    explanation: during his turn, client sends "NoMove" if client wants to finish the turn without another moves

* **Roll Dice**
    Client -> Server
    RollDiceCR

    explanation: during his turn, when the client needs to roll a dice, he sends "RollDice"

* **Roll Draft**
    Client -> Server
    RollDraftCR

    explanation: during the first turn of the round, the first player sends "RollDraft" to roll dice of the Draft

* **turn message**
    Server -> Client
    "Is your turn"CR

    explanation: server sends "Is your turn" at the client, when is his turn

* **end Turn message**
    Server -> Client
    "End turn"CR

    explanation: server sends "End turn" at the client, when is his turn inished

* **end Turn answer**
    Server -> Client
    "Your turn is over"CR

    explanation: server sends "Your turn is over" at the client, when the timer is over

## Round Messages

* **new round message**
    Server -> Client
    "New round is starting"CR

    explanation: server sends "New round is starting" at all clients, when the last round finished, and the new round
    is starting

* **end round message**
    Server -> Client
    "The round is over"

    explanation: server sends "The round is over" at all clients, when the the following turn is ending

* **first player**
    Server -> Client
    "You are the first player"CR

    explanation: when the last round finished, server sends "You are the first player" at the first client of the round

## End Game

* **end game message**
    Server -> Client
    "The game is over"CR

    explanation: when the tenth round finished, server sends "The game is over" at all clients

* **play again?**
    Server -> Client
    "Do you want play again?"CR

    explanation: after the "end game message", server sends "Do you want play again" to all clients

* **Yes, replay**
    Client -> Server
    YesReplayCR

    explanation: client answer "Yes, replay", at the server, if he want play another game

* **No, replay**
    Client -> Server
    NoReplayCR

    explanation: client answer "No, replay", at the client, if he doesn't want play again