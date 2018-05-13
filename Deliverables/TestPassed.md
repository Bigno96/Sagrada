# TestPassed

## DiceBag 
* **testDiceBagEmpty**: testing if cannot find a dice in an empty bag
* **testFindDice**: testing if can find a dice in the bag
* **testRandDice**: testing the extraction of a random Dice, without removing it from Bag
* **testDiceRemaining**: testing the number of remaining dices in the bag after a rollDice call
* **testDiceAdding**: testing add dice
** testDiceRemoving**: testing remove dice

## Dice 
* **testGetter**: testing getId, getColor
* **testIDNotFoundException**: incorrect Id passed when creating Dice
* **testRollDice**: testing value of dice must be 1 from 6
* **testChangeValue**: testing change of the value of a dice
* **testValueException**: testing the changing to an incorrect value
* **testCopyDice**: testing if copy of the dice is correct

## Draft 
* **testFillDraft**: testing fillDraft of random dice from DiceBag
* **testEmptyException**: testing reaction of fillDraft when DiceBag is empty
* **testRollDraft**: testing rolling dices of draft
* **testModifyingDraft**: testing adding and removing
* **testSetnDice**: testing setter of nDice of draft

## ObjectiveCard 
* **testId**: testing getId
* **testPoint**: testing getPoint
* **testDescr**: testing getting Description
* **testCalcPointPrivate**: testing calculating point of private objective over a window card
* **testCalcPointPublic**: testing calculating point of public objective over a window card

## ObjectiveFactory
* **testGetPrivCard**: testing getting a private Objective with a specific id
* **testGetPublCard**: testing getting a public Objective with a specific id
* **testExceptionPriv**: testing reaction when getting a private Objective with an incorrect id
* **testExceptionPubl**: testing reaction when getting a public Objective with an incorrect id

## ObjectiveStrategy
* **testPrivateObjectiveCard**: testing the call to Objective Strategy to calc point from private objective
* **testPublicObjectiveCard**: testing the call to Objective Strategy to calc point from public objective
* **testException**: testing bad id or bad objective

## PointCalculator
* **testCalcPointPriv**: testing calculating private objective point
* **testCalcDifferentColumnColor**: testing calculating point from different colors columns
* **testCalcDifferentRowColor**: testing calculating point from different colors rows
* **testCalcVarietyColor**: testing calculating point from different sets of colors
* **testCalcDiagonalColor**: testing calculating point from diagonal adjacent colors
* **testCalcDifferentColumnShade**: testing calculating point from different shade columns
* **testCalcDifferentRowShade**: testing calculating point from different shade rows
* **testCalcVarietyShade**: testing calculating point from different sets of shades
* **testCalcGradationShade**: testing calculating point from couples of shades

## ListDiceRound
* **testAddDice**: testing adding Dice to a round of RoundTrack
* **testRemoveDice**: testing removing Dice from a round of RoundTrack
* **testEmptyException**: testing reaction when removing from an empty ListDiceRound
* **testIDNotFoundException**: testing reaction when asking for a Dice not in ListDiceRound
* **testGetDice**: testing if a Dice can be found
* **testContains**: testing the presence of a Dice

## RoundTrack 
* **testFindDice**: testing the correct search of a dice in round Track
* **testIDNotFoundException**: testing reaction when trying to find a dice not in round Track
* **testAddDice**: testing about adding dices and reaction to illegal adding attempts
* **testRmDice**: testing about removing dices and reaction to illegal removing attempts

## Cell
* **testGetter**: testing getValue, getColor, getCol, getRow
* **testChangeDiceValue**: changing value of dice in the cell
* **testIsOccupied**: testing if cell is already occupied is impossible to insert a Dice
* **testCheck**: testing control over value and color restrictions
* **testValueException**: testing reaction to impossible value of cell
* **testPositionException**: testing incorrect position of cell
* **testNotEmptyException**: testing reaction to an addition of dice in an occupied cell
* **testFreeCell**: testing freeing a cell of its Dice

## MatrixCell
* **testGetter**: testing getRows, getCols, getMatrix, getCell
* **testLoadMatrixCell**: testing setting correctly matrix inside of window card
* **testIsBorder**: testing finding cells on border
* **testPositionException**: testing reaction when asking for nearby cells around an incorrect cell
* **testRetOrtogonal**: testing finding ortogonal neighbors of a cell
* **testRetDiagonal**: testing finding diagonal neighbors of a cell
* **testRetNeighbors**: testing finding all neighbors of a cell

## WindowCard
* **testGetter**: testing getId, getName, getNumFavPoint and getWindow
* **testCheckFirstDice**: testing if first Dice is correctly positioned
* **testCheckFirstDiceException**: testing cases as first dice placement of no dices positioned, more than one dice positioned,
                                   not in border and color and/or value restriction not respected
* **testCheckOneDice**: testing if only one dice is positioned
* **testCheckOneDiceException**: testing cases no dice are positioned and color and/or value restriction not respected
* **testCheckPlaceCond**: testing if all dices are correctly positioned
* **testCheckPlaceCondException**: testing all bad cases of placement


## WindowFactory 
* **testGetWindow**: testing correctly obtaining a pool of 4 WindowCard
* **testException**: testing reaction to incorrect research

## Player
* **testWindowCard**: testing setter and getter of WindowCard of the player
* **testGetId**: testing getter id of player
* **testGetBoard**: testing getter of the game board where player plays
* **testTurn**: testing if is player turn, passing turn and reading up for another turn
* **testPrivObj**: testing setter and getter of Private Objective of the player
* **testFavorPoint**: testing setter and getter of favor point of the player

## Round
* **testAddPlayer**: testing adding player
* **testSamePlayerException**: testing reaction when adding the same player more than once
* **testPlayerNotFoundException**: testing reaction when trying to find a player that doesn't exists
* **testRmPlayer**: testing removing player
* **testNextPlayer**: testing the correct order of the players in a round
* **testNextRound**: testing the new order of player in a new round
