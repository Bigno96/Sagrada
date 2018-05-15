## Protocol

## Connection Messages

* **login message** </br>
    Client -> Server </br>
    login <TAB>(user) <TAB>(Password)CR </br>

    explanation: Client sends his user and password for authentication

* **login answer** </br>
    Server -> Client </br>
    "logged" <TAB>(userID)CR </br>

    explanation: Server answer is "logged" and userID if it was successful

* **login authentication failed answer** </br>
    Server -> Client </br>
    "Your username or password are wrong"CR </br>

    explanation: Server answer is "Your username or password are wrong" if authentication failed

* **start Game message** </br>
    Client -> Server </br>
    startCR </br>

    explanation: Client send "start" when he wants to start a game

* **start Game answer** </br>
    Server -> Client </br>
    "Wait a moment, the game will start shortly"CR </br>

    explanation: Server answer is "Wait a moment, the game will start shortly" if it was successful

* **impossible to start** </br>
     Server -> Client </br>
     "Impossible to start a game"CR </br>

     explanation: Server answer is  "Impossible to start a game" if is impossible to start a new game

* **end Connection** </br>
    Server -> Client </br>
    "Connection closed!"CR </br>

    explanation: Server sends "Connection closed!" if the connection go out

* **Error Code** </br>
    "Description"CR </br>

    explanation: generic error messages

## Game Messages

* **ChooseWindowCard message** </br>
    Server -> Client </br>
    "Choose your windowCard between:"CR </br>
    "Num1" <TAB>(IDWindowCard)CR </br>
    "Num2" <TAB>(IDWindowCard)CR </br>
    "Num3" <TAB>(IDWindowCard)CR </br>
    "Num4" <TAB>(IDWindowCard)CR </br>

    explanation: server sends the four windowCards for the choice

* **ChooseWindowCard answer** </br> 
    Client -> Server </br>
    ChooseWindowCard <TAB>(NumWindowCard)CR </br>
    
    explanation: client chooses the number of his card

* **ranking message** </br>
    Server -> Client </br>
    "The First Player is" <TAB>(Username)CR </br>
    "With" <TAB>(PlayerPoints)CR </br>
    "The Second Player is" <TAB>(Username)CR </br>
    "With" <TAB>(PlayerPoints)CR </br>
    "The Third Player is" <TAB>(Username)CR //only if the number of players is >3 </br>
    "With" <TAB>(PlayerPoints)CR </br>
    "The First Player is" <TAB>(Username)CR //only if the number of players is 4 </br>
    "With" <TAB>(PlayerPoints)CR </br>
    
    explanation: at the end of the game, server sends the ranking of all players

## Turn Messages

* **MoveDiceInWindowCard** </br>
    Client -> Server </br>
    MoveDice <TAB>(NumDice)CR(Col) <TAB>(Row)CR </br>

    explanation: during his turn, client chooses a dice in the draft and puts it in him windowCard

* **UseToolCard** </br>
    Client -> Server </br>
    Tool <TAB>(NumOfToolCard)CR </br>

    explanation: during his turn, when client wants use a toolCard send "Tool (NumOfToolCard)"

* **Moves answer** </br>
    Server -> Client </br>
    "OK"CR </br>

    explanation: server answer is "Ok" when the move (UseToolCard or MoveDiceInWindowCard) is successful

* **Illegal move answer** </br>
    Server -> Client </br>
    "Your move is wrong"CR </br>

    explanation: server answer is "Ok" when the move (UseToolCard or MoveDiceInWindowCard) is an illegal move

* **No Move** </br>
    Client -> Server</br>
    NoMoveCR </br>

    explanation: during his turn, client sends "NoMove" if client wants to finish the turn without another moves

* **Roll Dice** </br>
    Client -> Server </br>
    RollDiceCR </br>

    explanation: during his turn, when the client needs to roll a dice, he sends "RollDice"

* **Roll Draft** </br>
    Client -> Server </br>
    RollDraftCR </br>

    explanation: during the first turn of the round, the first player sends "RollDraft" to roll dice of the Draft

* **turn message** </br>
    Server -> Client </br>
    "Is your turn"CR </br>

    explanation: server sends "Is your turn" at the client, when is his turn

* **end Turn message** </br>
    Server -> Client </br>
    "End turn"CR </br>

    explanation: server sends "End turn" at the client, when is his turn inished

* **end Turn answer** </br>
    Server -> Client </br>
    "Your turn is over"CR </br>

    explanation: server sends "Your turn is over" at the client, when the timer is over

## Round Messages </br>

* **new round message** </br>
    Server -> Client </br>
    "New round is starting"CR </br>

    explanation: server sends "New round is starting" at all clients, when the last round finished, and the new round
    is starting

* **end round message** </br>
    Server -> Client </br>
    "The round is over"CR </br>

    explanation: server sends "The round is over" at all clients, when the the following turn is ending

* **first player** </br>
    Server -> Client </br>
    "You are the first player"CR </br>

    explanation: when the last round finished, server sends "You are the first player" at the first client of the round

## End Game

* **end game message** </br>
    Server -> Client </br>
    "The game is over"CR </br>

    explanation: when the tenth round finished, server sends "The game is over" at all clients

* **play again?** </br>
    Server -> Client </br>
    "Do you want play again?"CR </br>

    explanation: after the "end game message", server sends "Do you want play again" to all clients

* **Yes, replay** </br>
    Client -> Server </br>
    YesReplayCR </br>

    explanation: client answer "Yes, replay", at the server, if he want play another game

* **No, replay** </br>
    Client -> Server </br>
    NoReplayCR </br>

    explanation: client answer "No, replay", at the client, if he doesn't want play again
