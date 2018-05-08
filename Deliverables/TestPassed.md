## TestPassed

- # Cell : testIsOccupied : if cell is occupied is impossible to insert a Dice else return null
           testChangeColor : change cell color
           testChangeValue : change cell value
           testGetPos : return position of the cell
           testIsBorder : return true if position of cell is on the boarder
           testValueException : impossible value of cell
           testPositionException : incorrect position of cell
           testNotEmptyException : when adding dice in not empty cell

- # DiceBag :   testFindDice : test if the dice is in the bag
                testRandDice : testing the extraction of a random Dice, without removing it from Bag
                testDiceRemaining : test the number of remaining dice in the bag after a rollDice call
                testDiceAdding : testing add dice
                testDiceRemoving : testing remove dice

- # Dice : testGetter : test getId getColor of dice
           testIDNotFoundException : incorrect Id
           testRollDice : test value of dice must between 0 - 7
           testChangeValue :  test the correct change of the value of the dice
           testValueException : test the insertion of the value of the dice
           testCopyDice : test if the copy of the dice is correct

- # Draft : testFillDraft : testing fillDraft with size of diceBag before and after and with size of draft
            testRollDraft : testing rolling dices of draft
            testModifyingDraft : testing adding and removing
            testSetnDice : test if SetnDice is correct

- # ListRound : testListRound : test the correct fill of ListRound with dice of draft in this turn

- # ObjetiveCard :  testId : test getId
                    testPoint : test getPoint
                    testDescr : test getDescr
                    testFP : test getFP

- # ObjectiveFactoryCard :  testGetObjCard : test getObjCard both privateCard publicCard
                            testNegativePriv : test incorrect attributes of privateCard
                            testNegativePubl : test incorrect attributes of publicCard

- # Player : testIsFirstTurn : return true if is the first turn of player in this round
             testEndFirstTurn : set firstTurn to false
             testResetFirstTurn : reset firstTurn to true
             testGetId : return id of player
             testWindCard : test of windowCard on player

- # Round : testNextTurn : the correct order of the players in the turn
            testNextRound : test the new order of player after endRound

- # RoundTrack : testFindDice : the correct search of the dice in the roundTrack

- # WindowCard : testGetter : test getId, getName, getNumFavPoint and getCell
                 testIdNotFoundException : test incorrect Id

- # WindowFactory : testPositiveGetWindow : test correct factory WindowCard
                    testNegativeGetWindow : test incorrect factory WindowCard