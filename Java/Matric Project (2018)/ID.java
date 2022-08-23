package ransom;

/**
 *Used to identify and differentiate objects and the screen that is currently being displayed
 * @author craig
 */
public enum ID
{

    /**
     *Used to identify the Player GameObject
     */
    Player(),

    /**
     *Used to identify and differentiate between GameObjects 
     */
    WallN(),

    /**
     *Used to identify and differentiate between GameObjects 
     */
    WallS(),

    /**
     *Used to identify and differentiate between GameObjects 
     */
    WallE(),

    /**
     *Used to identify and differentiate between GameObjects 
     */
    WallW(),

    /**
     *Used to identify the Player's current location
     */
    PokeCenter(),

    /**
     *Used to identify the Player's current location
     */
    Cave(),

    /**
     *Used to identify the Player's current location
     */
    Connect(),

    /**
     *Used to identify the Player's current location
     */
    Shop(),

    /**
     *Used to identify the Player's current location
     */
    MainArea(),

    /**
     *Used to identify the Grass GameObject and differentiate between different types of grasses
     */
    GrassN(),

    /**
     *Used to identify the Grass GameObject and differentiate between different types of grasses
     */
    GrassD(),

    /**
     *Used to identify the Grass GameObject and differentiate between different types of grasses
     */
    GrassI(),

    /**
     *Used to identify the Grass GameObject and differentiate between different types of grasses
     */
    GrassA(),

    /**
     *Used to identify the Grass GameObject and differentiate between different types of grasses
     */
    GrassC(),

    /**
     *Used to identify GameObjects that move the player into the Main Area
     */
    Door(),

    /**
     *Used to identify the current Screen that is being displayed
     */
    Game(),

    /**
     *Used to identify the current Screen that is being displayed
     */
    Stats(),

    /**
     *Used to identify the current Screen that is being displayed
     */
    Battle(),

    /**
     *Used to identify the current Screen that is being displayed
     */
    Help(),

    /**
     *Used to identify the current Screen that is being displayed
     */
    Computer(),

    /**
     *Used to identify the current Screen that is being displayed
     */
    Buy(),

    /**
     *Used to identify the current Screen that is being displayed
     */
    Sell(),

    /**
     *Used to identify and differentiate between GameObjects 
     */
    Heal()
}