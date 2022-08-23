#include "tile.h"

Tile::Tile(int value, int left, int right, int up, int down)
{
    this->left = left;
	this->right = right;
	this->up = up;
	this->down = down;
    this->value = value;
    this->visitable = true;//if a player is not on the tile
}
