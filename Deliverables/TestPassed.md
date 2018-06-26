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
* **testCalcPointPrivate**: testing calculating private objective point
* **testCalcDifferentColumnColor**: testing calculating point from different colors in columns
* **testCalcDifferentRowColor**: testing calculating point from different colors in rows
* **testCalcVarietyColor**: testing calculating point from different sets of colors
* **testCalcDiagonalColor**: testing calculating point from diagonal adjacent colors
* **testCalcDifferentColumnShade**: testing calculating point from different shade in columns
* **testCalcDifferentRowShade**: testing calculating point from different shade in rows
* **testCalcVarietyShade**: testing calculating point from different sets of shades
* **testCalcGradationShade**: testing calculating point from couples of shades

### ObjectiveCard 
* **testId**: testing getId
* **testPoint**: testing getPoint
* **testDescription**: testing getting Description
* **testSetParameter**: testing setting parameter used by objective calculator
* **testCopy**: testing method for copying objective card

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

## windowCard

### Cell
* **testGetter**: testing getValue, getColor, getCol, getRow
* **testChangeDiceValue**: testing changing value of dice in the cell
* **testIsOccupied**: testing of a cell is occupied
* **testCheck**: testing control over value and color restrictions
* **testValueException**: testing reaction to impossible value of cell
* **testNotEmptyException**: testing that if cell is already occupied is impossible to insert a Dice
* **testFreeCell**: testing freeing a cell of its Dice
* **testIgnoreRestriction**: test if the cells, nearby the current, have ignoring restriction set

### MatrixCell
* **testGetter**: testing getMaxRow, getMaxCol, getMatrix, getCell
* **testLoadMatrixCell**: testing setting correctly matrix inside of window card
* **testContainsDice**: test if the dice passed is in the matrix
* **testIsBorder**: testing finding cells on border
* **testPositionException**: testing reaction when asking for nearby cells around an incorrect cell
* **testRetOrthogonal**: testing finding orthogonal neighbors of a cell
* **testRetDiagonal**: testing finding diagonal neighbors of a cell
* **testRetNeighbors**: testing finding all neighbors of a cell

### WindowCard
* **testGetter**: testing getId, getName, getNumFavPoint and getWindow
* **testCheckFirstDice**: testing if first Dice is correctly positioned
* **testCheckFirstDiceException**: testing cases as first dice placement of no dices positioned, more than one dice positioned, not in border and color and/or value restriction not respected
* **testCheckOneDice**: testing if only one dice is positioned
* **testCheckOneDiceException**: testing cases no dice are positioned and color and/or value restriction not respected
* **testCheckOrtPos**: test the correct condition of the orthogonally positioned cells
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

## parser

### ObjectiveParserTest
* **testPrivateCards**: test if private cards are built correctly
* **testPublic1**: test if parameter of public card 1 are built correctly
* **testPublic2**: test if parameter of public card 2 are built correctly
* **testPublic3**: test if parameter of public card 3 are built correctly
* **testPublic4**: test if parameter of public card 4 are built correctly
* **testPublic5**: test if parameter of public card 5 are built correctly
* **testPublic6**: test if parameter of public card 6 are built correctly
* **testPublic7**: test if parameter of public card 7 are built correctly
* **testPublic8**: test if parameter of public card 8 are built correctly
* **testPublic9**: test if parameter of public card 9 are built correctly
* **testPublic10**: test if parameter of public card 10 are built correctly


