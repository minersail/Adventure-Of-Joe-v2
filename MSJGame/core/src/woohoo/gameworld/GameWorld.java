package woohoo.gameworld;

import com.badlogic.gdx.Input;
import woohoo.framework.InputHandler;

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
        if (InputHandler.isKeyPressed(Input.Keys.LEFT))
            GameRenderer.scrollCamera(-1, 0);
        else if (InputHandler.isKeyPressed(Input.Keys.RIGHT))
            GameRenderer.scrollCamera(1, 0);
        else if (InputHandler.isKeyPressed(Input.Keys.UP))
            GameRenderer.scrollCamera(0, -1);
        else if (InputHandler.isKeyPressed(Input.Keys.DOWN))
            GameRenderer.scrollCamera(0, 1);
        
        
        runtime += delta;
    }
}
