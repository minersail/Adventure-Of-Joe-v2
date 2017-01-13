package woohoo.framework;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import woohoo.gameobjects.Player;
import woohoo.gameobjects.components.MapObjectComponent.Direction;
import woohoo.screens.PlayingScreen;

public class InputHandler implements InputProcessor
{
	PlayingScreen screen;
	Player player;
	
    public InputHandler(Screen scr, Player p)
    {
		screen = (PlayingScreen)scr;
		player = p;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return true;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        switch (keycode) 
        {            
			case Keys.UP:
				player.move(Direction.Up, true);
				break;
			case Keys.DOWN:
				player.move(Direction.Down, true);
				break;
			case Keys.LEFT:
				player.move(Direction.Left, true);
				break;
			case Keys.RIGHT:
				player.move(Direction.Right, true);
				break;
            case Keys.SPACE:
                screen.getEngine().checkDialogue(player.getPosition());
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
		switch (keycode) {
			case Keys.UP:
				player.move(Direction.Down, false);
				break;
			case Keys.DOWN:
				player.move(Direction.Up, false);
				break;
			case Keys.LEFT:
				player.move(Direction.Right, false);
				break;
			case Keys.RIGHT:
				player.move(Direction.Left, false);
				break;
        }
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
