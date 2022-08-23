#pragma once
#ifndef AI_H
#define AI_H

#include "player.h"
#include "tile.h"
#include <fstream>

class AI : public Player
{
private:
	vector<Card> knownCards;
	vector<vector<Card>> poteniallyHas;
	vector<vector<Card>> doesNotHave;
    vector<vector<Card>> shownCards;
	ofstream outputFile;
    vector<int> playerLocations;
	vector<Tile> tiles;
    vector<vector<int>> oneAway;
    vector<vector<int>> twoAway;
    bool searchMoveTree(bool up, bool left, int pos, int roll, vector<int>& options, vector<bool> found);
    void updateTiles();
    bool goToWin;
public:
    AI(string username, Card character);
	~AI();
	void play();
    void addToPotentiallyHas(Card character,Card murderer, Card weapon, Card room);
    void addToDoesNotHave(Card character, Card murderer, Card weapon, Card room);
	void addToKnown(Card card);
	void updateProbabilities(std::string results);
    vector<Tile> getTiles();
    void writeToFile(string message);
    vector<Card> getKnownCards();
    vector<vector<Card>> getPoteniallyHas();
    //Move startTurn(int diceRoll);
    void setLocation(int location);
    int changeLocation(int roll);
    Card showCard(vector<Card> cards, Card player);
    void setGoToWin(bool win);
    void setPlayerLocations(vector<int> loc);
};
#endif // AI_H
