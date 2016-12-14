package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import woohoo.gameworld.GameWorld;

public class InputHandler implements InputProcessor
{
    public InputHandler()
    {
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return true;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        switch (keycode) {
//            case Keys.LEFT:
//                GameWorld.getPlayer().movePlayer(3);
//                return true;
//            case Keys.RIGHT:
//                GameWorld.getPlayer().movePlayer(1);
//                return true;
//            case Keys.UP:
//                GameWorld.getPlayer().movePlayer(0);
//                return true;
//            case Keys.DOWN:
//                GameWorld.getPlayer().movePlayer(2);
//                return true;
        }
        return false;
    }
    
    public static boolean isKeyPressed(int keycode)
    {
        return Gdx.input.isKeyPressed(keycode);
    }

    @Override
    public boolean keyUp(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}
