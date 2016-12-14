package woohoo.gameworld;

/*
All objects are data that will be drawn by the tiles

Their updates will be called in this class
*/
public class GameWorld
{
    private static GameState currentState = GameState.PLAYING;
    public static float runtime;

    public enum GameState
    {
        PLAYING
    }
    
    public static void update(float delta)
    {
        runtime += delta;
    }
}
