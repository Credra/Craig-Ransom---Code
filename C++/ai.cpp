#include "ai.h"

//constructor
AI::AI(string username, Card character) :Player{ username, character}
{
    outputFile.open(username + "_reasons.txt");
	goToWin = false;

	//Assigning values of the tiles according to the board 
    //-1 is not a viable route to take from that tile
	//rooms
    tiles.push_back(Tile(0, 36, 37, 20, 68));
    tiles.push_back(Tile(1, -1, -1, 40, 50));
    tiles.push_back(Tile(2, 7, -1, 82, -1));
    tiles.push_back(Tile(3, -1, -1, 78, -1));
    tiles.push_back(Tile(4, 5, 9, 74, -1));
    tiles.push_back(Tile(5, -1, 4, 51, -1));
    tiles.push_back(Tile(6, -1, -1, -1, 41));
    tiles.push_back(Tile(7, 2, 8, -1, 10));
    tiles.push_back(Tile(8, 7, -1, -1, 19));
    tiles.push_back(Tile(9, 4, -1, -1, 13));
	//row 1
    tiles.push_back(Tile(10, -1, 11, 7, 15));
    tiles.push_back(Tile(11, 10, -1, -1, 16));

    tiles.push_back(Tile(12, -1, 13, -1, 22));
    tiles.push_back(Tile(13, 12, 14, 9, 23));
    tiles.push_back(Tile(14, 13, -1, -1, 24));
	//row 2
    tiles.push_back(Tile(15, -1, 16, 10, 25));
    tiles.push_back(Tile(16, 15, 17, 11, 26));
    tiles.push_back(Tile(17, 16, 18, -1, 27));
    tiles.push_back(Tile(18, 17, 19, -1, 28));
    tiles.push_back(Tile(19, 18, 20, 8, -1));
    tiles.push_back(Tile(20, 19, 21, -1, 0));
    tiles.push_back(Tile(21, 20, 22, -1, -1));
    tiles.push_back(Tile(22, 21, 23, 12, 29));
    tiles.push_back(Tile(23, 22, 24, 13, 30));
    tiles.push_back(Tile(24, 23, -1, 14, 31));
	//row 3
    tiles.push_back(Tile(25, -1, 26, 15, 33));
    tiles.push_back(Tile(26, 25, 27, 16, 34));
    tiles.push_back(Tile(27, 26, 28, 17, 35));
    tiles.push_back(Tile(28, 27, -1, 18, 36));

    tiles.push_back(Tile(29, -1, 30, 22, 47));
    tiles.push_back(Tile(30, 29, 31, 23, 38));
    tiles.push_back(Tile(31, 30, 32, 24, 39));
    tiles.push_back(Tile(32, 31, -1, -1, 40));
	//row 4
    tiles.push_back(Tile(33, -1, 34, 25, 43));
    tiles.push_back(Tile(34, 33, 35, 26, 44));
    tiles.push_back(Tile(35, 34, 36, 27, 45));
    tiles.push_back(Tile(36, 35, -1, 28, 46));

    tiles.push_back(Tile(37, 0, 38, 29, 47));
    tiles.push_back(Tile(38, 37, 39, 30, 48));
    tiles.push_back(Tile(39, 38, 40, 31, 49));
    tiles.push_back(Tile(40, 39, 1, 32, 50));
	//row 5
    tiles.push_back(Tile(41, -1, 42, 6, 51));
    tiles.push_back(Tile(42, 41, -1, -1, 52));

    tiles.push_back(Tile(43, -1, 44, 33, -1));
    tiles.push_back(Tile(44, 43, 45, 34, -1));
    tiles.push_back(Tile(45, 44, 46, 35, 55));
    tiles.push_back(Tile(46, 45, -1, 36, 56));

    tiles.push_back(Tile(47, -1, 48, 37, 57));
    tiles.push_back(Tile(48, 47, 49, 38, 58));
    tiles.push_back(Tile(49, 48, 50, 39, 59));
    tiles.push_back(Tile(50, 49, -1, 40, 60));
	//row 6
    tiles.push_back(Tile(51, 5, 52, 41, 61));
    tiles.push_back(Tile(52, 51, 53, 42, 62));
    tiles.push_back(Tile(53, 52, 54, -1, 63));
    tiles.push_back(Tile(54, 53, 55, -1, 64));
    tiles.push_back(Tile(55, 54, 56, 45, 65));
    tiles.push_back(Tile(56, 55, -1, 46, 66));

    tiles.push_back(Tile(57, -1, 58, 47, 70));
    tiles.push_back(Tile(58, 57, 59, 48, 71));
    tiles.push_back(Tile(59, 58, 60, 49, 72));
    tiles.push_back(Tile(60, 59, -1, 50, -1));
	//row 7
    tiles.push_back(Tile(61, -1, 62, 51, 73));
    tiles.push_back(Tile(62, 61, 63, 52, 74));
    tiles.push_back(Tile(63, 62, 64, 53, -1));
    tiles.push_back(Tile(64, 63, 65, 54, -1));
    tiles.push_back(Tile(65, 64, 66, 55, 75));
    tiles.push_back(Tile(66, 65, 67, 56, 76));
    tiles.push_back(Tile(67, 66, 68, -1, 77));
    tiles.push_back(Tile(68, 67, 69, 0, 78));
    tiles.push_back(Tile(69, 68, 70, -1, 79));
    tiles.push_back(Tile(70, 69, 71, 57, 80));
    tiles.push_back(Tile(71, 70, 72, 58, 81));
    tiles.push_back(Tile(72, 71, -1, 59, 82));
	//row 8
    tiles.push_back(Tile(73, -1, 74, 61, -1));
    tiles.push_back(Tile(74, 73, -1, 62, 4));

    tiles.push_back(Tile(75, -1, 76, 65, -1));
    tiles.push_back(Tile(76, 75, 77, 66, -1));
    tiles.push_back(Tile(77, 76, 78, 67, -1));
    tiles.push_back(Tile(78, 77, 79, 68, 3));
    tiles.push_back(Tile(79, 78, 80, 69, -1));
    tiles.push_back(Tile(80, 79, 81, 70, -1));
    tiles.push_back(Tile(81, 80, 82, 71, -1));
    tiles.push_back(Tile(82, 81, -1, 72, 2));


    vector<int> temp;
    temp.push_back(40);
    temp.push_back(50);
	oneAway.push_back(temp);
    temp.clear();
	temp.push_back(82);
	oneAway.push_back(temp);
    temp.clear();
	temp.push_back(78);
	oneAway.push_back(temp);
    temp.clear();
	temp.push_back(74);
	oneAway.push_back(temp);
    temp.clear();
	temp.push_back(51);
	oneAway.push_back(temp);
    temp.clear();
	temp.push_back(41);
	oneAway.push_back(temp);
    temp.clear();
	temp.push_back(10);
	oneAway.push_back(temp);
    temp.clear();
	temp.push_back(19);
	oneAway.push_back(temp);
    temp.clear();
	temp.push_back(13);
	oneAway.push_back(temp);
    temp.clear();


	temp.push_back(60);
	temp.push_back(49);
	temp.push_back(39);
	temp.push_back(32);
	twoAway.push_back(temp);
    temp.clear();

	temp.push_back(81);
	temp.push_back(72);
	twoAway.push_back(temp);
    temp.clear();

	temp.push_back(77);
	temp.push_back(68);
	temp.push_back(79);
	twoAway.push_back(temp);
    temp.clear();

	temp.push_back(73);
	temp.push_back(62);
	twoAway.push_back(temp);
    temp.clear();

	temp.push_back(52);
	temp.push_back(61);
	twoAway.push_back(temp);
    temp.clear();

	temp.push_back(42);
	twoAway.push_back(temp);
    temp.clear();

	temp.push_back(15);
	temp.push_back(11);
	twoAway.push_back(temp);
    temp.clear();

	temp.push_back(18);
	temp.push_back(20);
	twoAway.push_back(temp);
    temp.clear();

	temp.push_back(12);
	temp.push_back(23);
	temp.push_back(14);
	twoAway.push_back(temp);
    temp.clear();
}

//deconstructor
AI::~AI()
{
	outputFile.close();
}


void AI::play()
{

}

//Character is a unique identifier for each player
void AI::addToPotentiallyHas(Card character,Card murderer, Card weapon, Card room)
{
	poteniallyHas.push_back({character, murderer, weapon, room});
}

//when a player doesn't show a card to a question
void AI::addToDoesNotHave(Card character, Card murderer, Card weapon, Card room)
{
    doesNotHave.push_back({ character, murderer, weapon, room });

}

//when cards are revealed, adds to the vector
void AI::addToKnown(Card card)
{
    for (int i = 0; i < knownCards.size(); i++)
	{
		if (card == knownCards[i])
			return;
	}
	knownCards.push_back(card);
}

//called after each turn to combined knownCards, poteniallyHas, doesNotHave

void AI::updateProbabilities(string results)
{
	//reduces poteniallyHas according to knownCards
    for (int i = 0; i < knownCards.size(); i++)
	{
        for (int j = 0; j < poteniallyHas.size(); j++)
		{

            switch (poteniallyHas[j].size())
			{
				case 1:
					addToKnown(poteniallyHas[j][0]);
                    poteniallyHas.erase(poteniallyHas.begin()+j);
					break;
				case 2:
					if (poteniallyHas[j][0] == knownCards[i])
                    {
                        addToKnown(poteniallyHas[j][1]);
                        poteniallyHas.erase(poteniallyHas.begin()+j);
                    }
					else if (poteniallyHas[j][1] == knownCards[i])
                    {
                        addToKnown(poteniallyHas[j][0]);
                        poteniallyHas.erase(poteniallyHas.begin()+j);
                    }
					break;
				case 3:
					if (poteniallyHas[j][0] == knownCards[i])
                        poteniallyHas[j].erase(poteniallyHas[j].begin()+0);
					else if (poteniallyHas[j][1] == knownCards[i])
                        poteniallyHas[j].erase(poteniallyHas[j].begin()+1);
					else if (poteniallyHas[j][2] == knownCards[i])
                        poteniallyHas[j].erase(poteniallyHas[j].begin()+2);
					break;
                default:
					break;
			}
		}
	}
	cout << "updateProbabilities" << endl;
}

//function that starts the AI's turn
/*Move AI::startTurn(int diceRoll)
{
    return temp;
}*/

//updates player locations on the tiles
void AI::updateTiles()
{
	for (int i = 0; i < tiles.size(); i++)
		tiles[i].visitable = true;

	for (int i = 0; i < playerLocations.size(); i++)
		tiles[playerLocations[i]].visitable = false;
}


//returns the location to be moved to
int AI::changeLocation(int roll)
{
    writeToFile("New turn: rolled a " + to_string(roll));
	updateTiles();

    //determines the rooms that are known
    vector<int> options;
    vector<bool> found;
    for (int i = 0; i < 9; i++)
        found.push_back(false);
    for (int i = 0; i < knownCards.size(); i++)
    {
        if (knownCards[i] == Courtyard)
            found[0] = true;
        else if (knownCards[i] == Garage)
            found[1] = true;
        else if (knownCards[i] == GameRoom)
            found[2] = true;
        else if (knownCards[i] == Bedroom)
            found[3] = true;
        else if (knownCards[i] == Bathroom)
            found[4] = true;
        else if (knownCards[i] == Study)
            found[5] = true;
        else if (knownCards[i] == Kitchen)
            found[6] = true;
        else if (knownCards[i] == DiningRoom)
            found[7] = true;
        else if (knownCards[i] == LivingRoom)
            found[8] = true;
    }

    if (goToWin)//if needing to move to end room
	{
        writeToFile("Performing search on up, left tree");
        bool done = done = searchMoveTree(true, true, location, roll, options, found);
        if (!done)
        {
            writeToFile("Performing search on up, right tree");
            done = searchMoveTree(true, false, location, roll, options, found);
            if (!done)
            {
                writeToFile("Performing search on down, right tree");
                done = searchMoveTree(false, false, location, roll, options, found);
                if (!done)
                {
                    done = searchMoveTree(false, true, location, roll, options, found);
                    writeToFile("Performing search on down, left tree");
                }
            }
        }

        if (location < 10)//if in a room
		{
			bool done = false;
			switch (location)
			{
			case 1://Courtyard
				if (tiles[40].visitable)
				{
					done = searchMoveTree(true, true, 40, roll - 1, options, found);
				}
				else if (tiles[50].visitable)
				{
					done = searchMoveTree(true, true, 50, roll - 1, options, found);
				}
				break;
			case 2://Garage
				if (tiles[82].visitable)
				{
					done = searchMoveTree(true, true, 82, roll - 1, options, found);
				}
				break;
			case 3://GameRoom
				if (tiles[78].visitable)
				{
					done = searchMoveTree(true, true, 78, roll - 1, options, found);
				}
				break;
			case 4://Bedroom
				if (tiles[74].visitable)
				{
					done = searchMoveTree(true, false, 74, roll - 1, options, found);
				}
				break;
			case 5://Bathroom
				if (tiles[51].visitable)
				{
					done = searchMoveTree(true, true, 51, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, true, 51, roll - 1, options, found);
					}
				}
				break;
			case 6://Study
				if (tiles[41].visitable)
				{
					done = searchMoveTree(false, true, 41, roll - 1, options, found);
				}
				break;
			case 7://Kitchen
				if (tiles[10].visitable)
				{
					done = searchMoveTree(false, false, 10, roll - 1, options, found);
				}
				break;
			case 8://DiningRoom
				if (tiles[19].visitable)
				{
					done = searchMoveTree(false, true, 19, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 19, roll - 1, options, found);
					}
				}
				break;
			case 9://LivingRoom
				if (tiles[13].visitable)
				{
					done = searchMoveTree(false, true, 13, roll - 1, options, found);
				}
				break;
			}

			if (done)
			{
                writeToFile("Moving to end room");
				return 0;
			}

            if(options.size()==0)
            {
                writeToFile("No valid path, staying in current location");
                return location;
            }

            writeToFile("No path to winning room, moving to random option");
            return options[rand()%options.size()];
		}
        else
        {
            vector<int> temp;
            temp.push_back(20);
            temp.push_back(37);
            temp.push_back(36);
            temp.push_back(68);
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < options.size(); k++)
                {
                    if (options[k] == temp[j])
                    {
                        writeToFile("Tile found, moving to" + to_string(options[k]));
                        return options[k];
                    }
                }
            }
            temp.clear();
            temp.push_back(19);
            temp.push_back(21);
            temp.push_back(29);
            temp.push_back(38);
            temp.push_back(47);
            temp.push_back(69);
            temp.push_back(78);
            temp.push_back(67);
            temp.push_back(46);
            temp.push_back(35);
            temp.push_back(28);
            writeToFile("No tile found. Looking for tile 2 away from end room");
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < options.size(); k++)
                {
                    if (options[k] == temp[j])
                    {
                        writeToFile("Tile found, moving to" + to_string(options[k]));
                        return options[k];
                    }
                }
            }
        }
        if(options.size()==0)
        {
            writeToFile("No valid path, staying in current location");
            return location;
        }

        writeToFile("No path to winning room, moving to random option");
        return options[rand()%options.size()];
	}


	//if in a room and that room or a connecting room is not known
	switch (location)
	{
	case 1://Courtyard
		if (!found[0])
		{
			writeToFile("Stay in Courtyard, room hasn't been found");
			return location;
		}
		break;
	case 2://Garage
		if (!found[1])
		{
			writeToFile("Stay in Garage, room hasn't been found");
			return location;
		}
		if (!found[6])
		{
			writeToFile("Move to Kitchen, that room hasn't been found");
			return 7;
		}
		break;
	case 3://GameRoom
		if (!found[2])
		{
			writeToFile("Stay in GameRoom, room hasn't been found");
			return location;
		}

		break;
	case 4://Bedroom
		if (!found[3])
		{
			writeToFile("Stay in Bedroom, room hasn't been found");
			return location;
		}
		if (!found[4])
		{
			writeToFile("Move to Bathroom, that room hasn't been found");
			return 5;
		}
        if (!found[8])
        {
            writeToFile("Move to LivingRoom, that room hasn't been found");
            return 9;
        }
		break;
	case 5://Bathroom
		if (!found[4])
		{
			writeToFile("Stay in Bathroom, room hasn't been found");
			return location;
		}
		if (!found[3])
		{
			writeToFile("Move to Bedroom, that room hasn't been found");
			return 4;
		}
		break;
	case 6://Study
		if (!found[5])
		{
			writeToFile("Stay in Study, room hasn't been found");
			return location;
		}

		break;
	case 7://Kitchen
		if (!found[6])
		{
			writeToFile("Stay in Kitchen, room hasn't been found");
			return location;
		}
		if (!found[1])
		{
			writeToFile("Move to Garage, that room hasn't been found");
			return 2;
		}
		if (!found[7])
		{
			writeToFile("Move to Dinning Room, that room hasn't been found");
			return 8;
		}
		break;
	case 8://DiningRoom
		if (!found[7])
		{
			writeToFile("In DiningRoom, room hasn't been found");
			return location;
		}
		if (!found[6])
		{
			writeToFile("Move to Kitchen, that room hasn't been found");
			return 7;
		}
		break;
	case 9://LivingRoom
		if (!found[8])
		{
			writeToFile("In LivingRoom, room hasn't been found");
			return location;
		}
        if (!found[3])
        {
            writeToFile("Move to Bedroom, that room hasn't been found");
            return 4;
        }
		break;
	}

	//construct a tree using the tiles, all places can be reached by limiting each cycle through the tree to two moves... construct 4 binary trees then select a room or the closest tile to a room

	//if in a room that is known and all connecting rooms are known
	if (location < 10)
	{
		bool done = false;
		switch (location)
		{
		case 0://Cluedo Room
			if (tiles[20].visitable)
			{
				done = searchMoveTree(true, true, 20, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 20, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 20, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 20, roll - 1, options, found);
					}
				}
			}
			else if (tiles[37].visitable)
			{
				done = searchMoveTree(true, true, 37, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 37, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 37, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 37, roll - 1, options, found);
					}
				}
			}
			else if (tiles[68].visitable)
			{
				done = searchMoveTree(true, true, 68, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 68, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 68, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 68, roll - 1, options, found);
					}
				}
			}
			else if (tiles[36].visitable)
			{
				done = searchMoveTree(true, true, 36, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 36, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 36, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 36, roll - 1, options, found);
					}
				}
			}
			break;
		case 1://Courtyard
			if (tiles[40].visitable)
			{
				done = searchMoveTree(true, true, 40, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 40, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 40, roll - 1, options, found);
						if (!done)
                            done = searchMoveTree(false, true, 40, roll - 1, options, found);
					}
				}
			}
			else if (tiles[50].visitable)
			{
				done = searchMoveTree(true, true, 50, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 50, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 50, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 50, roll - 1, options, found);
					}
				}
			}
			break;
		case 2://Garage
			if (tiles[82].visitable)
			{
				done = searchMoveTree(true, true, 82, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 82, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 82, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 82, roll - 1, options, found);
					}
				}
			}
			break;
		case 3://GameRoom
			if (tiles[78].visitable)
			{
				done = searchMoveTree(true, true, 78, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 78, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 78, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 78, roll - 1, options, found);
					}
				}
			}
			break;
		case 4://Bedroom
			if (tiles[74].visitable)
			{
				done = searchMoveTree(true, true, 74, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 74, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 74, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 74, roll - 1, options, found);
					}
				}
			}
			break;
		case 5://Bathroom
			if (tiles[51].visitable)
			{
				done = searchMoveTree(true, true, 51, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 51, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 51, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 51, roll - 1, options, found);
					}
				}
			}
			break;
		case 6://Study
			if (tiles[41].visitable)
			{
				done = searchMoveTree(true, true, 41, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 41, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 41, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 41, roll - 1, options, found);
					}
				}
			}
			break;
		case 7://Kitchen
			if (tiles[10].visitable)
			{
				done = searchMoveTree(true, true, 10, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 10, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 10, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 10, roll - 1, options, found);
					}
				}
			}
			break;
		case 8://DiningRoom
			if (tiles[19].visitable)
			{
				done = searchMoveTree(true, true, 19, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 19, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 19, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 19, roll - 1, options, found);
					}
				}
			}
			break;
		case 9://LivingRoom
			if (tiles[13].visitable)
			{
				done = searchMoveTree(true, true, 13, roll - 1, options, found);
				if (!done)
				{
					done = searchMoveTree(true, false, 13, roll - 1, options, found);
					if (!done)
					{
						done = searchMoveTree(false, false, 13, roll - 1, options, found);
						if (!done)
							done = searchMoveTree(false, true, 13, roll - 1, options, found);
					}
				}
			}
			break;
		}

		if (done)
		{
            writeToFile("Moving to " + to_string(options[options.size() - 1]));
            return options[options.size() - 1];
		}
		else
		{
			int i = 0;
            while (i < options.size())
			{
				if (options[i] < 10 && options[i]>0)
				{
                    writeToFile("Moving to " + to_string(options[i]));
					return options[i];
				}
				i++;
			}

		}
        writeToFile("No unknown room to move to, stay in current room");
		return location;
	}


	writeToFile("Performing search on up, left tree");
	bool done = done = searchMoveTree(true, true, location, roll, options, found);
	if (!done)
	{
		writeToFile("Performing search on up, right tree");
		done = searchMoveTree(true, false, location, roll, options, found);
		if (!done)
		{
			writeToFile("Performing search on down, right tree");
			done = searchMoveTree(false, false, location, roll, options, found);
			if (!done)
			{
				done = searchMoveTree(false, true, location, roll, options, found);
				writeToFile("Performing search on down, left tree");
			}
		}
	}

	if (done)
	{
        writeToFile("Unknown room found, moving to"+ to_string(options[options.size() - 1]));
        return options[options.size() - 1];
	}
	else
	{
		writeToFile("No unknown room found");
		int i = 0;
        while (i < options.size())
		{
			if (options[i] < 10 && options[i]>0)
			{
				writeToFile("Moving to known room, to "+ options[i]);
				return options[i];
			}
			i++;
		}
		writeToFile("No known room found");
	}
	//choose tile if not ending up in room
	writeToFile("Looking for tile 1 away from unknown room");
	for (int i = 0; i < 10; i++)
	{
		if (!found[i])
		{
            writeToFile("Room "+to_string(i)+" unknown");
            for (int j = 0; j < oneAway[i].size(); j++)
			{
                for (int k = 0; k < options.size(); k++)
				{
                    for (int l = 0; l <oneAway[j].size();l++)
                    {
                        if (options[k] == oneAway[j][l])
                        {
                            writeToFile("Tile found, moving to " + to_string(options[k]));
                            return options[k];
                        }
                    }
				}
			}
		}
	}
    writeToFile("No tile found. Looking for tile 2 away from unknown room");
    for (int i = 0; i < 10; i++)
    {
        if (!found[i])
        {
            for (int j = 0; j < twoAway[i].size(); j++)
            {
                for (int k = 0; k < options.size(); k++)
                {
                    for (int l = 0; l <twoAway[j].size();l++)
                    {
                        if (options[k] == twoAway[j][l])
                        {
                            writeToFile("Tile found, moving to" + to_string(options[k]));
                            return options[k];
                        }
                    }
                }
            }
        }
    }
	writeToFile("No Tile found");
    writeToFile("Making arbitary move to "+to_string(options[0]));
	return options[0];
}

//choose which card to show
Card AI::showCard(vector<Card> cards, Card player)
{
	vector<Card> temp;
	temp.push_back(player);

    for (int i = 0; i < shownCards.size(); i++)
	{
		if (shownCards[i][0] == player)
		{
            for (int j = 0; j < cards.size(); j++)
			{
                if (shownCards[i][1] == cards[j])
				{
                    writeToFile("Shown card already, showing again");
                    temp.push_back(cards[j]);
					return cards[j];
				}
			}
		}
	}

    for (int i = 0; i < shownCards.size(); i++)
	{
        for (int j = 0; j < cards.size(); j++)
		{
			if (shownCards[i][1] == cards[j])
			{
                writeToFile("Shown card to another player already, showing again");
				temp.push_back(cards[j]);
				return cards[j];
			}
		}
	}
    writeToFile("None of the options have been shown before, showing first");
	temp.push_back(cards[0]);
	shownCards.push_back(temp);
	return cards[0];
}



//creates a binary tree that  is search for rooms or final tiles
bool AI::searchMoveTree(bool up, bool left, int pos, int roll, vector<int> &options, vector<bool> found)
{
	//if going to end
	if (goToWin)
	{
		if (pos < 0)
			return false;
		if (!tiles[pos].visitable)
			return false;
		if (pos == 0)
		{
			options.push_back(pos);
			return true;
		}

		if (roll == 0)
		{
			options.push_back(pos);
			return false;
		}

		if (roll > 0)
		{
			if (up)
			{
				if (left)
				{
					if (searchMoveTree(up, left, tiles[pos].left, roll - 1, options, found))
						return true;
				}
				else
				{
					if (searchMoveTree(up, left, tiles[pos].right, roll - 1, options, found))
						return true;
				}
				if (searchMoveTree(up, left, tiles[pos].up, roll - 1, options, found))
					return true;
			}
			else
			{
				if (left)
				{
					if (searchMoveTree(up, left, tiles[pos].left, roll - 1, options, found))
						return true;
				}
				else
				{
					if (searchMoveTree(up, left, tiles[pos].right, roll - 1, options, found))
						return true;
				}
				if (searchMoveTree(up, left, tiles[pos].down, roll - 1, options, found))
					return true;
			}
		}
		return false;
	}

	//if move is invalid
	if (pos < 1)
		return false;
	if (!tiles[pos].visitable)
		return false;
	if (pos < 10)
	{
		options.push_back(pos);
        return !found[pos-1];
	}

	if (roll == 0)
	{
		options.push_back(pos);
		return false;
	}

	if (roll > 0)
	{
		if (up)
		{
			if (left)
			{
                if (searchMoveTree(up, left, tiles[pos].left, roll - 1, options, found))//search left
					return true;
			}
			else
			{
                if (searchMoveTree(up, left, tiles[pos].right, roll - 1, options, found))//search right
					return true;
			}
            if (searchMoveTree(up, left, tiles[pos].up, roll - 1, options, found))//search up
				return true;
		}
		else
		{
			if (left)
			{
                if (searchMoveTree(up, left, tiles[pos].left, roll - 1, options, found))//search left
					return true;
			}
			else
			{
                if (searchMoveTree(up, left, tiles[pos].right, roll - 1, options, found))//search right
					return true;
			}
            if (searchMoveTree(up, left, tiles[pos].down, roll - 1, options, found))//search down
				return true;
		}
	}
	return false;
}

//writes passed message to the file 
void AI::writeToFile(string message)
{
	outputFile << message << "\n";
}

vector<Tile> AI::getTiles()
{
    return tiles;
}

vector<Card> AI::getKnownCards()
{
    return knownCards;
}

vector<vector<Card>> AI::getPoteniallyHas()
{
   return poteniallyHas;
}

void AI::setLocation(int location)
{
    this->location=location;
}

void AI::setGoToWin(bool win)
{
    goToWin=win;
}

void AI:: setPlayerLocations(vector<int> loc)
{
    playerLocations.clear();
    for(int i = 0; i<loc.size();i++)
    {
        playerLocations.push_back(loc[i]);
    }
}
