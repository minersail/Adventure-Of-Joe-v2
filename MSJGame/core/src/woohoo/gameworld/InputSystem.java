package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.InputComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.MovementComponent.Direction;
import woohoo.screens.PlayingScreen;

public class InputSystem extends IteratingSystem implements InputProcessor
{
	public InputSystem()
	{
		super(Family.all(InputComponent.class).get());
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		
	}

	@Override
	public boolean keyDown(int keycode)
	{
		switch (screen.getState())
		{
			case Playing:
				switch (keycode) 
				{            
					case Input.Keys.UP:
						player.setMovement(MovementComponent.Movement.Vertical);
						player.move(MapObjectComponent.Direction.Up);
						break;
					case Input.Keys.DOWN:
						player.setMovement(MovementComponent.Movement.Vertical);
						player.move(MapObjectComponent.Direction.Down);
						break;
					case Input.Keys.LEFT:
						player.setMovement(MovementComponent.Movement.Horizontal);
						player.move(MapObjectComponent.Direction.Left);
						break;
					case Input.Keys.RIGHT:
						player.setMovement(MovementComponent.Movement.Horizontal);
						player.move(MapObjectComponent.Direction.Right);
						break;
					case Input.Keys.SPACE:
						screen.getEngine().checkDialogue(player);
						screen.getEngine().checkItems(player);
						break;
                    case Input.Keys.ESCAPE:
                        screen.getInventoryManager().showInventory();
                        break;
                    case Input.Keys.F1:
                        screen.getQuestManager().showQuests();
                        break;
					case Input.Keys.S:
						System.out.println(player.getPosition());
						break;
					case Input.Keys.A:
						player.attack();
						break;
				}
				break;
			
			case Dialogue:
				switch (keycode)
				{
					case Input.Keys.SPACE:
						screen.getDialogueManager().advanceDialogue();
				}
				break;
			
			case Inventory:
				switch (keycode)
				{
					case Input.Keys.ESCAPE:
						screen.getInventoryManager().closeInventory();
				}
				break;
			
			case Quests:
				switch (keycode)
				{
					case Input.Keys.F1:
						screen.getQuestManager().closeQuests();
				}
				break;
				
			case Cutscene:
				switch(keycode)
				{
					case Input.Keys.NUM_0: // Debug get rid of later
						screen.getEngine().getPlayer().setAIMode(AIComponent.AIMode.Input);
						screen.setState(PlayingScreen.GameState.Playing);
						break;
				}
				break;
		}
		
		if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN) && 
			!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			player.stop();
			player.setMovement(MovementComponent.Movement.None);
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.DOWN) && 
			Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			player.setMovement(MovementComponent.Movement.None);
		}
		
		return false;
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
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
	
	public interface InputCommand
	{
		public void apply(Entity entity);
	}
	
	public class MoveUpInput implements InputCommand
	{
		@Override
		public void apply(Entity entity)
		{
			if (!Mappers.movements.has(entity)) return;
			
			Mappers.movements.get(entity).direction = Direction.Up;
		}
	}
	
	public class MoveDownInput implements InputCommand
	{
		@Override
		public void apply(Entity entity)
		{
			if (!Mappers.movements.has(entity)) return;
			
			Mappers.movements.get(entity).direction = Direction.Down;
		}
	}
	
	public class MoveLeftInput implements InputCommand
	{
		@Override
		public void apply(Entity entity)
		{
			if (!Mappers.movements.has(entity)) return;
			
			Mappers.movements.get(entity).direction = Direction.Left;
		}
	}
	
	public class MoveRightInput implements InputCommand
	{
		@Override
		public void apply(Entity entity)
		{
			if (!Mappers.movements.has(entity)) return;
			
			Mappers.movements.get(entity).direction = Direction.Right;
		}
	}
}
