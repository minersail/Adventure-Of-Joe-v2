package woohoo.framework;

import com.badlogic.gdx.Gdx;
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
		switch (screen.getState())
		{
			case Playing:
				switch (keycode) 
				{            
					case Keys.UP:
						player.move(Direction.Up);
						break;
					case Keys.DOWN:
						player.move(Direction.Down);
						break;
					case Keys.LEFT:
						player.move(Direction.Left);
						break;
					case Keys.RIGHT:
						player.move(Direction.Right);
						break;
					case Keys.SPACE:
						screen.getEngine().checkDialogue(player);
						break;
					case Keys.S:
						screen.switchScreens(1);
						break;
				}
				break;
			
			case Dialogue:
				switch (keycode)
				{
					case Keys.SPACE:
						screen.getDialogueManager().advanceDialogue();
				}
				break;
		}
		
		if (!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN) && 
			!Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT))
		{
			player.stop();
		}
		
		screen.getEngine().adjustCamera(player);
			
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
		switch (screen.getState())
		{
			case Playing:
				switch (keycode) {
					case Keys.UP:
						player.move(Direction.Down);
						break;
					case Keys.DOWN:
						player.move(Direction.Up);
						break;
					case Keys.LEFT:
						player.move(Direction.Right);
						break;
					case Keys.RIGHT:
						player.move(Direction.Left);
						break;
				}
				break;
				
			case Dialogue:
				switch(keycode)
				{
					
				}
				break;
		}
		
		if (!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN) && 
			!Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT))
		{
			player.stop();
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
