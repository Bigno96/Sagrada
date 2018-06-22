# TestPassed

## diceBag

### Dice 
* **testGetter**: testing getId, getColor
* **testIDNotFoundException**: incorrect Id passed when creating Dice
* **testRollDice**: testing value of dice must be 1 from 6
* **testChangeValue**: testing change of the value of a dice
* **testValueException**: testing the changing to an incorrect value
* **testCopyDice**: testing if copy of the dice is correct

### DiceBag 
* **testDiceBagEmpty**: testing if cannot find a dice in an empty bag
* **testFindDice**: testing if can find a dice in the bag
* **testRandDice**: testing the extraction of a random Dice, without removing it from Bag
* **testDiceRemaining**: testing the number of remaining dices in the bag after a rollDice call
* **testDiceAdding**: testing add dice
* **testDiceRemoving**: testing remove dice

### Draft 
* **testFillDraft**: testing fillDraft of random dice from DiceBag
* **testEmptyException**: testing reaction of fillDraft when DiceBag is empty
* **testRollDraft**: testing rolling dices of draft
* **testModifyingDraft**: testing adding and removing
* **testSetNumberDice**: testing setter of numberDice of draft

## game

### Board
* **testGetter**: testing all the "get-methods" in Board
* **testSetPublicObj**: testing if all the public objective are instantiated
* **testSetToolCard**: testing if all the tool card are instantiated

### Game
* **testGetter**: testing all the "get-methods" in Game
* **testStartGame**: testing the correct start of the game and correct finding of the player
* **testAddPlayer**: testing the correct addition of a player
* **testSamePlayerException**: test if there is already the same player in the game
* **testPlayerNotFoundException**: test if there isn't a player in the game
* **testCurrentPlayer**: test the player who is playing the turn
* **testRmPlayer**: test the correct remove of a player

### Player
* **testWindowCard**: test the correct pick of the window card by a player
* **testGetID**: test getID
* **testGetBoard**: test getBoard
* **testTurn**: test the correct setting and resetting of turn indicator in player
* **testPlayDice**: test the correct setting and resetting of placing dice indicator in player
* **testUseTool**: test the correct setting and resetting of using tool indicator in player
* **testPrivateObj**: test the correct setting of the private objective
* **testFavorPoint**: test the correct number of favor point
* **testRateScore**: test the calculation of the score of the player

### Round
* **testNextPlayer**: test the correct player who has to play
* **testNextRound**: test the correct round sequence
* **testGetPlayer**: test getPlayer
* **testGetPlayerException**: test tha call of the exception if the id of the player does not exist

## objectiveCard

### ObjectiveCalculator
* **testCalcPointPriv**: testing calculating private objective point
* **testCalcDifferentColumnColor**: testing calculating point from different colors columns
* **testCalcDifferentRowColor**: testing calculating point from different colors rows
* **testCalcVarietyColor**: testing calculating point from different sets of colors
* **testCalcDiagonalColor**: testing calculating point from diagonal adjacent colors
* **testCalcDifferentColumnShade**: testing calculating point from different shade columns
* **testCalcDifferentRowShade**: testing calculating point from different shade rows
* **testCalcVarietyShade**: testing calculating point from different sets of shades
* **testCalcGradationShade**: testing calculating point from couples of shades

### ObjectiveCard 
* **testId**: testing getId
* **testPoint**: testing getPoint
* **testDescr**: testing getting Description
* **testCalcPointPrivate**: testing calculating point of private objective over a window card
* **testCalcPointPublic**: testing calculating point of public objective over a window card

### ObjectiveFactory
* **testGetPrivCard**: testing getting a private Objective with a specific id
* **testGetPublCard**: testing getting a public Objective with a specific id
* **testExceptionPriv**: testing reaction when getting a private Objective with an incorrect id
* **testExceptionPubl**: testing reaction when getting a public Objective with an incorrect id

### ObjectiveStrategy
* **testPrivateObjectiveCard**: testing the call to Objective Strategy to calc point from private objective
* **testPublicObjectiveCard**: testing the call to Objective Strategy to calc point from public objective
* **testException**: testing bad id or bad objective

## roundtrack

### ListDiceRound
* **testAddDice**: testing adding Dice to a round of RoundTrack
* **testRemoveDice**: testing removing Dice from a round of RoundTrack
* **testEmptyException**: testing reaction when removing from an empty ListDiceRound
* **testIDNotFoundException**: testing reaction when asking for a Dice not in ListDiceRound
* **testGetDice**: testing if a Dice can be found
* **testContains**: testing the presence of a Dice

### RoundTrack 
* **testFindDice**: testing the correct search of a dice in round Track
* **testFindColor**: test th correct search of a color
* **testGetRound**: test getRound
* **testIDNotFoundException**: testing reaction when trying to find a dice not in round Track
* **testAddDice**: testing about adding dices and reaction to illegal adding attempts
* **testRmDice**: testing about removing dices and reaction to illegal removing attempts

## toolcard

### ToolCard
* **testAttribute**: testing all "get-methods" in ToolCard
* **testGetActor**: test getActor
* **testTool1**: test tool card 1
* **testTool2_3**: test tool card 2 and 3
* **testTool4**: test tool card 4
* **testTool5**: test tool card 5
* **testTool6_7**: test tool card 6 and 7
* **testTool8**: test tool card 8
* **testTool9**: test tool card 9
* **testTool10**: test tool card 10
* **testTool11**: test tool card 11
* **testTool12**: test tool card 12

## ToolFactory
* **testMakeToolCard**: test the making of all the tool cards

## ToolStrategy
* **testCheckTool12**: test if the condition of the tool card 12 are correct
* **testCheckDiceIn**: test if the dice is in window card (first), round track (second), draft (third)
* **testChangeValue**: test the change of value only if the new value is old value +1 or -1
* **testMoveOneDice**: test the move of a dice from a cell in window Card to a Cell (dest) passed as parameter
* **testMoveExTwoDice**: test moving exactly two dice inside window Card
* **testMoveUpToTwoDice**: test moving 0, 1 or 2 dices inside window Card
* **testMoveFromDraftToRound**: test swapping a dice from round Track with one from Draft
* **testMoveFromDraftToBag**: test the move of  one dice from draft to the bag
* **testMoveFromDraftToCard**: test the place of a Dice from draft into window Card
* **testFindSetNearby**: test if two dices are on window Card, so ignore or set nearby restriction of both of their Cells. Else, ignore or set nearby restriction only for dest

## windowcard

### Cell
* **testGetter**: testing getValue, getColor, getCol, getRow
* **testChangeDiceValue**: changing value of dice in the cell
* **testIsOccupied**: testing if cell is already occupied is impossible to insert a Dice
* **testCheck**: testing control over value and color restrictions
* **testValueException**: testing reaction to impossible value of cell
* **testNotEmptyException**: testing reaction to an addition of dice in an occupied cell
* **testFreeCell**: testing freeing a cell of its Dice
* **testIgnoreRestriction**: test if the cells, nearby the current, have ignoring restriction setted 

### MatrixCell
* **testGetter**: testing getRows, getCols, getMatrix, getCell
* **testLoadMatrixCell**: testing setting correctly matrix inside of window card
* **testContainsDice**: test if the dice passed is in 
* **testIsBorder**: testing finding cells on border
* **testPositionException**: testing reaction when asking for nearby cells around an incorrect cell
* **testRetOrtogonal**: testing finding ortogonal neighbors of a cell
* **testRetDiagonal**: testing finding diagonal neighbors of a cell
* **testRetNeighbors**: testing finding all neighbors of a cell

### WindowCard
* **testGetter**: testing getId, getName, getNumFavPoint and getWindow
* **testCheckFirstDice**: testing if first Dice is correctly positioned
* **testCheckFirstDiceException**: testing cases as first dice placement of no dices positioned, more than one dice positioned, not in border and color and/or value restriction not respected
* **testCheckOneDice**: testing if only one dice is positioned
* **testCheckOneDiceException**: testing cases no dice are positioned and color and/or value restriction not respected
* **testCheckOrtPos**: test the correct condition of the ortogonally positioned cells
* **testCheckNeighbors**: test the correct condition of the cells nearby 
* **testCheckPlaceCond**: testing if all dices are correctly positioned
* **testCheckPlaceCondException**: testing all bad cases of placement
* **testNumEmptyCells**: test the number of empty cells

### WindowFactory 
* **testGetWindow**: testing correctly obtaining a pool of 4 WindowCard
* **testException**: testing reaction to incorrect research

## colors

### Colors
* **testRandomColors**: test if the result of RandomColors is different from null
* **testParseColors**: test the correct correspondence between the enum value and the name (string)
