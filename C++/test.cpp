#include "test.h"
#include "ai.h"

Test::Test()
{

}

void Test::testCard()
{
    cout<<"Test to see if enum matches comm doc"<<endl;
    Card temp = Courtyard;
    cout <<"0 == "<< temp <<endl;
    temp = LivingRoom;
    cout <<"9 == "<< temp+1 <<endl;
    temp = White;
    cout <<"20 == "<< temp <<endl;
}

void Test::testMove()
{
    cout<<"Testing basic functionallity of class"<<endl;
    Card murderer=Green;
    Card weapon = Rope;
    Card room = Bathroom;
    int location = 0;
    Move move(murderer, weapon, room, location);
    cout << Green << " == " << move.getMurderer() << endl;
    move.setMurderer(Peacock);
    cout << Peacock<<" == " << move.getMurderer() << endl;

    cout << Bathroom << "  == " << move.getRoom() << endl;
    move.setRoom(LivingRoom);
    cout << LivingRoom << "  == " << move.getRoom() << endl;

    cout << Rope << "  == " << move.getWeapon() << endl;
    move.setWeapon(Dagger);
    cout << Dagger << "  == " << move.getWeapon() << endl;

    cout << "0 == " << move.getLocation() << endl;
    move.setLocation(10);
    cout << "10 == " << move.getLocation() << endl;
}

void Test::testTile()
{
    cout<<"Testing Valid"<<endl;
    AI ai("Craig", Green);
    vector<Tile> t = ai.getTiles();
    int pos=0;
    pos=t[pos].down;
    pos=t[pos].down;
    pos=t[pos].down;
    cout << pos << " == " << 3 <<endl;
    pos=61;
    cout<< "expected: 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72"<<endl<<"actual  : ";
    for(int i =0; i<11;i++)
    {
        pos=t[pos].right;
        cout<<pos<<", ";
    }
    cout<<endl;
     cout<< "expected: 59, 49, 39, 31, 24, 14"<<endl<<"actual  : ";
    for(int i =0; i<6;i++)
    {
        pos=t[pos].up;
        cout<<pos<<", ";
    }
    cout<<endl;
    pos=t[pos].left;
    pos=t[pos].up;
    cout << pos << " == " << 9 <<endl;
    cout<<endl<<"Testing Invalid"<<endl;

    cout<<"-1=="<<t[10].left<<endl;
    cout<<"-1=="<<t[43].left<<endl;
    cout<<"-1=="<<t[43].down<<endl;
    cout<<"-1=="<<t[80].down<<endl;
    cout<<"-1=="<<t[28].right<<endl;
    cout<<"-1=="<<t[60].right<<endl;
    cout<<"-1=="<<t[54].up<<endl;
    cout<<"-1=="<<t[14].up<<endl;
}

void Test::aiWrite()
{
    AI ai("Craig", Green);
    ai.writeToFile("Testing 123");
}

void Test :: aiMoveRoomToRoom()
{
    AI ai("Craig", Green);
    ai.setLocation(2);
    cout << "2 == " << ai.changeLocation(8) <<endl;
    ai.addToKnown(Garage);
    cout << "7 == " << ai.changeLocation(8) <<endl;
    ai.addToKnown(Kitchen);
    cout << "9 == " << ai.changeLocation(8) <<endl;
    ai.addToKnown(LivingRoom);
    ai.setLocation(7);
    cout << "8 == " << ai.changeLocation(8) <<endl;
}

void Test :: aiMoveTileToRoom()
{
    cout << "Testing with no player obstructions and no known:"<<endl;
    AI ai("Craig", Green);
    ai.setLocation(75);
    cout << "3 == " << ai.changeLocation(5) <<endl;
    cout << "Testing with no player obstructions and known:"<<endl;
    ai.setLocation(18);
    ai.addToKnown(DiningRoom);
    cout << "8 == " << ai.changeLocation(5) <<endl;

    cout << "Testing with player obstructions and unknown:"<<endl;
    ai.setLocation(42);
    vector<int> loc;
    loc.push_back(41);
    ai.setPlayerLocations(loc);

    cout << "5 == " << ai.changeLocation(3) <<endl;

    ai.addToKnown(Bedroom);
    cout << "Testing with player obstructions and known:"<<endl;
    cout << "4 == " << ai.changeLocation(5) <<endl;
}

void Test :: aiMoveTileToTile()
{
    AI ai("Craig", Green);

    cout << "Testing with no player obstructions, 1 tile away:"<<endl;
    ai.setLocation(26);
    cout << "10 == " << ai.changeLocation(3) <<endl;


    cout << "Testing with no player obstructions, 2 tiles away:"<<endl;
    ai.setLocation(33);
    cout << "15 == " << ai.changeLocation(2) <<endl;

    vector<int> loc;
    loc.push_back(74);
    loc.push_back(62);
    ai.setPlayerLocations(loc);

    cout << "Testing with player obstructions, 1 tile away:"<<endl;
    ai.setLocation(73);
    cout << "51 == " << ai.changeLocation(2) <<endl;

    cout << "Testing with player obstructions, 2 tiles away:"<<endl;
    ai.setLocation(63);
    cout << "52 == " << ai.changeLocation(2) <<endl;

    cout << "Testing with 3 or more tiles away, arbitary move(left most then up most):"<<endl;
    ai.setLocation(34);
    cout << "15 == " << ai.changeLocation(3) <<endl;
}

void Test :: aiMoveRoomToEnd()
{
    AI ai("Craig", Green);
    ai.setGoToWin(true);
    cout<<"Testing room to end"<<endl;
    ai.setLocation(2);
    cout << "0 == " << ai.changeLocation(7) <<endl;
    ai.setLocation(8);
    cout << "0 == " << ai.changeLocation(3) <<endl;

    cout<<"Testing room to tile towards end"<<endl;
    ai.setLocation(4);
    cout << "67 == " << ai.changeLocation(7) <<endl;
    ai.setLocation(7);
    cout << "18 == " << ai.changeLocation(5) <<endl;

}

void Test :: aiMoveTileToEnd()
{
    AI ai("Craig", Green);
    ai.setGoToWin(true);
    cout<<"Testing tile to end"<<endl;
    ai.setLocation(82);
    cout << "0 == " << ai.changeLocation(7) <<endl;
    ai.setLocation(24);
    cout << "0 == " << ai.changeLocation(5) <<endl;

    cout<<"Testing tile to tile towards end:"<<endl;
    ai.setLocation(74);
    cout<<"One Tile away:"<<endl;
    cout << "68 == " << ai.changeLocation(7) <<endl;
    ai.setLocation(81);
    cout<<"Two tiles away:"<<endl;
    cout << "78 == " << ai.changeLocation(3) <<endl;
}

void Test::aiShowCards()
{
    AI ai("Craig", Green);
    vector<Card> hand;
    hand.push_back(Green);
    hand.push_back(Rope);

    cout<<"Never shown those cards before:"<<endl;

    cout<<Green<<"=="<<ai.showCard(hand, Green)<<endl;

    hand.clear();
    hand.push_back(Rope);
    hand.push_back(Green);
    cout<<"Shown a card to that player:"<<endl;

    cout<<Green<<"=="<<ai.showCard(hand, Green)<<endl;

    cout<<"Shown a card to another player:"<<endl;

    cout<<Green<<"=="<<ai.showCard(hand, Scarlet)<<endl;
}
