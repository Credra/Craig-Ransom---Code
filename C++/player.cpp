#include "player.h"
#include <iostream>


/*string username;
Card character;
vector<Card> cardsInHand;
int location;
Client client;
//GameGUI gui*/

Player::Player()
{

}

Player::Player(string username, Card character)
{

    this->username = username;
    this->character = character;
}

Player::~Player()
{

}

void Player::play()
{
    //starts the overall turn process
    cout << "Playing" << endl;
}

void Player::setCards(vector<Card> cards)
{
    //assigns the cards given to the player by the server to our local hand
    for (int i; i<cards.size(); i++)
    {
        cardsInHand.push_back(cards[i]);
    }
    cout << "Set Cards" << endl;
}

int Player::startTurn(int diceRoll)
{
    //passes the dice roll through to the GUI
    cout << "Determined Move" << endl;
    return diceRoll;
}

Move Player::endTurn(Move finalMove)
{
    return finalMove;
}

void Player::accuse(Card murderer, Card location, Card weapon)
{
    cout << "Accusing" << endl;
}

void Player::ask(Card murderer,Card location, Card weapon)
{
    //recieve the Cards from the GUI selection and pass them to the network class
    cout << "Asking" << endl;
}

void Player::move(int position)
{
    cout << "Moving to " << position << endl;
}

Card Player::chooseCardToShow(vector<Card> cardArray)
{
    vector<Card> cardsToChoose;

    cout << "Choosing card to show if available" << endl;
    for (int i = 0; i < cardArray.size(); i++)
    {
        for (int j = 0; j < cardsInHand.size(); j++)
        {
            if (cardsInHand[i] == cardArray[i])
            {
                cardsToChoose.push_back(cardsInHand[i]);
            }
        }
    }
    return cardsToChoose[0];
}

void Player::decodeMessage(string message)
{
    cout << "Decoding message" << endl;
}
