package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import woohoo.gameobjects.Player;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.CollisionComponent.Movement;
import woohoo.gameobjects.components.MapObjectComponent.Direction;
import woohoo.screens.PlayingScreen;

public class InputHandler implements InputProcessor
{
	PlayingScreen screen;
	Player player;
	
    public InputHandler(Screen scr)
    {
		screen = (PlayingScreen)scr;
		player = screen.getEngine().getPlayer();
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
						player.setMovement(Movement.Vertical);
						player.move(Direction.Up);
						break;
					case Keys.DOWN:
						player.setMovement(Movement.Vertical);
						player.move(Direction.Down);
						break;
					case Keys.LEFT:
						player.setMovement(Movement.Horizontal);
						player.move(Direction.Left);
						break;
					case Keys.RIGHT:
						player.setMovement(Movement.Horizontal);
						player.move(Direction.Right);
						break;
					case Keys.SPACE:
						screen.getEngine().checkDialogue(player);
						screen.getEngine().checkItems(player);
						break;
                    case Keys.ESCAPE:
                        screen.getInventoryManager().showInventory();
                        break;
					case Keys.S:
						System.out.println(player.getPosition());
						break;
					case Keys.A:
						player.attack();
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
			
			case Inventory:
				switch (keycode)
				{
					case Keys.ESCAPE:
						screen.getInventoryManager().closeInventory();
				}
				break;
				
			case Cutscene:
				switch(keycode)
				{
					case Keys.NUM_0: // Debug get rid of later
						screen.getEngine().getPlayer().setAIMode(AIComponent.AIMode.Input);
						screen.setState(PlayingScreen.GameState.Playing);
						break;
				}
				break;
		}
		
		if (!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN) && 
			!Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT))
		{
			player.stop();
			player.setMovement(Movement.None);
		}
		
		if (Gdx.input.isKeyPressed(Keys.UP) && Gdx.input.isKeyPressed(Keys.DOWN) && 
			Gdx.input.isKeyPressed(Keys.RIGHT) && Gdx.input.isKeyPressed(Keys.LEFT))
		{
			player.setMovement(Movement.None);
		}
			
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
						if (xor(Keys.RIGHT, Keys.LEFT))
							player.setMovement(Movement.Horizontal);
						break;
					case Keys.DOWN:
						player.move(Direction.Up);
						if (xor(Keys.RIGHT, Keys.LEFT))
							player.setMovement(Movement.Horizontal);
						break;
					case Keys.LEFT:
						player.move(Direction.Right);
						if (xor(Keys.UP, Keys.DOWN))
							player.setMovement(Movement.Vertical);
						break;
					case Keys.RIGHT:
						player.move(Direction.Left);
						if (xor(Keys.UP, Keys.DOWN))
							player.setMovement(Movement.Vertical);
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
			player.setMovement(Movement.None);
		}
		
		if (Gdx.input.isKeyPressed(Keys.UP) && Gdx.input.isKeyPressed(Keys.DOWN) && 
			Gdx.input.isKeyPressed(Keys.RIGHT) && Gdx.input.isKeyPressed(Keys.LEFT))
		{
			player.setMovement(Movement.None);
		}
		
        return false;
    }
	
	private boolean xor(int key1, int key2)
	{
		return (Gdx.input.isKeyPressed(key1) || Gdx.input.isKeyPressed(key2)) && !(Gdx.input.isKeyPressed(key1) && Gdx.input.isKeyPressed(key2));
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
