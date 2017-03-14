package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import java.util.Iterator;
import woohoo.framework.input.*;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.InputComponent;
import woohoo.screens.PlayingScreen;

public class InputSystem extends IteratingSystem implements InputProcessor
{
	PlayingScreen screen;
	
	public InputSystem(PlayingScreen scr)
	{
		super(Family.all(InputComponent.class).get());
		
		screen = scr;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		for (Iterator<InputCommand> it = Mappers.inputs.get(entity).commands.iterator(); it.hasNext();)
		{
			InputCommand command = it.next();
			command.execute(entity);
			it.remove(); // Commands are only executed once
		}
		
		for (InputState state : Mappers.inputs.get(entity).states)
		{
			state.execute(entity);
		}
	}
	
	private void addCommand(InputCommand command)
	{
		for (Entity entity : getEntities())
		{
			Mappers.inputs.get(entity).commands.add(command);
		}
	}
	
	private void addState(InputState state)
	{
		for (Entity entity : getEntities())
		{
			Mappers.inputs.get(entity).states.add(state);
		}
	}
	
	private void removeState(InputState state)
	{
		for (Entity entity : getEntities())
		{
			for (Iterator<InputState> it = Mappers.inputs.get(entity).states.iterator(); it.hasNext();)
			{
				InputState inputState = it.next();
				
				if (inputState.getClass() == state.getClass()) // Compare equality by class (All MoveUpStates should be inherently equal)
				{
					it.remove();
					return;
				}
			}
		}
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
						addState(new MoveUpState());
						break;
					case Input.Keys.DOWN:
						addState(new MoveDownState());
						break;
					case Input.Keys.LEFT:
						addState(new MoveLeftState());
						break;
					case Input.Keys.RIGHT:
						addState(new MoveRightState());
						break;
					case Input.Keys.SPACE:
						addCommand(new PickupItemCommand());
						addCommand(new NPCTalkCommand());
						break;
                    case Input.Keys.ESCAPE:
                        screen.getInventoryManager().showInventory();
                        break;
                    case Input.Keys.F1:
                        screen.getQuestManager().showQuests();
                        break;
					case Input.Keys.S:
						addCommand(new PrintPosCommand());
						break;
					case Input.Keys.A:
						addCommand(new PlayerAttackCommand());
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
		
//		if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN) && 
//			!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT))
//		{
//			player.stop();
//			player.setMovement(MovementComponent.Movement.None);
//		}
//		
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		switch (screen.getState())
		{
			case Playing:
				switch (keycode) {
					case Input.Keys.UP:
						removeState(new MoveUpState());
						break;
					case Input.Keys.DOWN:
						removeState(new MoveDownState());
						break;
					case Input.Keys.LEFT:
						removeState(new MoveLeftState());
						break;
					case Input.Keys.RIGHT:
						removeState(new MoveRightState());
						break;
				}
				break;
				
			case Dialogue:
				switch(keycode)
				{
					
				}
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
}
