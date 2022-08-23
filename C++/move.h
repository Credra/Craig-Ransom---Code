#pragma once
#ifndef MOVE_H
#define MOVE_H

#include "card.cpp"

#include <iostream>
using namespace std;


class Move
{
private:
	int location;
	Card murderer;
	Card weapon;
	Card room;

public:
    Move();
	Move(Card murderer, Card weapon, Card room, int location);
    void setLocation(int location);
	void setMurderer(Card murderer);
	void setWeapon(Card weapon);
	void setRoom(Card room);
	int getLocation();
	Card getMurderer();
	Card getWeapon();
	Card getRoom();
};

#endif // MOVE_H
/*
location: int
-murderer:card = none
-weapon:card = none
-room:card = none

+Move(murderer:Card, weapon:Card, room:Card)
+setLocation(location:Card)
+setMurderer(murderer:Card)
+setWeapon(weapon:Card)
+getLocation():int
+getMurderer():card
+getWeapon():card
+getRoom():card
*/
