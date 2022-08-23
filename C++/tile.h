#pragma once
class Tile
{
public:
	int left;
	int right;
	int up;
	int down;
    int value;
	bool visitable;
    Tile(int value, int left, int right, int up, int down);
};
