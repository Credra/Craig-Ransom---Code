#pragma once
#ifndef PLAYER_H
#define PLAYER_H
#include <vector>
#include <string>
#include "move.h"

using namespace std;

class Player
{
protected:
    string username;
    Card character;
    vector<Card> cardsInHand;
    int location;
    //GameGUI gui

    void play();
    void setCards(vector<Card> cards);
    int startTurn(int diceRoll);
    Move endTurn(Move finalMove);
    void accuse(Card murderer, Card location,Card weapon);
    void ask(Card murderer,Card location, Card weapon);
    void move(int position);
    Card chooseCardToShow(vector<Card> cardArray);
    void decodeMessage(string message);

public:
    Player();
    Player(string username, Card character);
    ~Player();
};

#endif // PLAYER_H
