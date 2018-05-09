# TestPassed

## Cell 
* **testChangeDiceValue** : changing value of dice in the cell
* **testIsOccupied** : testing if cell is already occupied is impossible to insert a Dice
* **testGetPos** : testing the position inside WindowCard of cell
* **testIsBorder** : testing return true if position of cell is on the border
* **testCheck** : testing control over value and color restrictions
* **testValueException** : testing reaction to impossible value of cell
* **testPositionException** : testing incorrect position of cell
* **testNotEmptyException** : testing reaction to an addition of dice in an occupied cell

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

## ListDiceRound 
* **testAddDice**: testing adding Dice to a round of RoundTrack
* **testRemoveDice**: testing removing Dice from a round of RoundTrack
* **testEmptyException**: testing reaction when removing from an empty ListDiceRound
* **testIDNotFoundException**: testing reaction when asking for a Dice not in ListDiceRound
* **testGetDice**: testing if a Dice can be found
* **testContains**: testing the presence of a Dice

## ObjectiveCard 
* **testId**: testing getId
* **testPoint**: testing getPoint
* **testDescr**: testing getting Description
* **testFP**: testing getting and setting favor point

## ObjectiveFactory
* **testGetPrivCard**: testing getting a private Objective with a specific id
* **testGetPublCard**: testing getting a public Objective with a specific id
* **testExceptionPriv**: testing reaction when getting a private Objective with an incorrect id
* **testExceptionPubl**: testing reaction when getting a public Objective with an incorrect id

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

## RoundTrack 
* **testFindDice**: testing the correct search of a dice in round Track
* **testIDNotFoundException**: testing reaction when trying to find a dice not in round Track
* **testAddDice**: testing about adding dices and reaction to illegal adding attempts
* **testRmDice**: testing about removing dices and reaction to illegal removing attempts

## WindowCard 
* **testGetter**: testing getId, getName, getNumFavPoint and getCell
* **testIdNotFoundException**: testing reaction to an incorrect Id

## WindowFactory 
* **testGetWindow**: testing correctly obtaining a pool of 4 WindowCard
* **testException**: testing reaction to incorrect research
