#include "move.h"

Move::Move(Card murderer, Card weapon, Card room, int location)
{
	this->murderer = murderer;
	this->weapon = weapon;
	this->room = room;
	this->location = location;
}

Move::Move()
{

}

void Move::setLocation(int location)
{
	this->location = location;
}

void Move::setRoom(Card room)
{
	this->room = room;
}

void Move::setMurderer(Card murderer)
{
	this->murderer = murderer;
}

void Move::setWeapon(Card weapon)
{
	this->weapon = weapon;
}

int Move::getLocation()
{
	return location;
}

Card Move::getMurderer()
{
	return murderer;
}

Card Move::getWeapon()
{
	return weapon;
}

Card Move::getRoom()
{
	return room;
}
