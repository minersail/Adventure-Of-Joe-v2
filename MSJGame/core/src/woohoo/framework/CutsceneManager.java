package woohoo.framework;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.util.ArrayList;
import woohoo.ai.aistates.MoveToState;
import woohoo.ai.aistates.StayState;
import woohoo.framework.events.EventListeners;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameobjects.components.AnimMapObjectComponent.AnimationState;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameobjects.components.PositionComponent.Orientation;
import woohoo.gameworld.Mappers;
import woohoo.screens.PlayingScreen;
import woohoo.screens.PlayingScreen.GameState;

public class CutsceneManager
{
    private PlayingScreen screen;
    
    private ArrayList<Entity> cutsceneEntities;
    private ArrayList<CutsceneAction> cutsceneActions;
	
	private EventListeners listeners;
    
    private CutsceneAction currentAction;
    
    public CutsceneManager(PlayingScreen scr)
    {
        screen = scr;
        cutsceneEntities = new ArrayList<>();
        cutsceneActions = new ArrayList<>();
		listeners = new EventListeners();
    }
    
    /**
	 * Updates only those entities that are free to move in the current cutscene
	 * @param delta elapsed time since last frame
	 */
	public void update(float delta)
	{
		screen.getEngine().update(delta);
		
        if (currentAction.isDone(delta))
        {
            int nextAction = cutsceneActions.indexOf(currentAction) + 1;
            
            if (nextAction >= cutsceneActions.size())
            {
                endCutscene();
            }
            else
            {
                currentAction = cutsceneActions.get(cutsceneActions.indexOf(currentAction) + 1);
                currentAction.start();
            }
        }
		
		listeners.notifyAll(this);
	}
    
    public void startCutscene(int cutsceneID)
    {
        FileHandle handle = Gdx.files.local("data/cutscenes.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());
        Element cutscene = root.getChild(cutsceneID);
        
        for (Element e : cutscene.getChildrenByName("entity"))
        {
            cutsceneEntities.add(screen.getEngine().getEntity(e.get("name")));
        }
		
		DialogueComponent component = new DialogueComponent(cutsceneID, true);
        
        for (Element e : cutscene.getChildrenByName("action"))
        {
            CutsceneAction action;
            
			switch (e.get("type"))
			{
				case "move":
					action = new MovementAction(e.get("name"), e.getFloat("locX"), e.getFloat("locY"), e.getFloat("speed"));
					cutsceneActions.add(action);
					break;
				case "dialogue":
					action = new DialogueAction(component);
					cutsceneActions.add(action);
					break;
				case "rotate":
					action = new RotateAction(e.get("name"), e.get("direction"));
					cutsceneActions.add(action);
					break;
				case "kill":
					action = new KillAction(e.get("name"));
					cutsceneActions.add(action);
					break;
				case "animate":
					action = new AnimateAction(e.get("name"), e.get("animation"), e.getFloat("time", 1));
					cutsceneActions.add(action);
					break;
				default:
					break;
			}
        }
        
        screen.setState(GameState.Cutscene);
        currentAction = cutsceneActions.get(0);
        currentAction.start();
    }
    
    public void endCutscene()
    {
		for (Entity entity : cutsceneEntities)
		{
			if (Mappers.players.has(entity)) // This is the player
			{
				entity.remove(AIComponent.class);
				Mappers.movements.get(entity).velocity.setZero();
			}
		}
		
        cutsceneEntities.clear();
        cutsceneActions.clear();
        screen.setState(GameState.Playing);
    }
	
	public EventListeners getListeners()
	{
		return listeners;
	}
	
	public int getActionsLeft()
	{
		return cutsceneActions.size() - 1 - cutsceneActions.indexOf(currentAction);
	}
    
    public interface CutsceneAction
    {
        public void start();
        
        /**
         * Performs a cutscene action. isDone() will be called until it returns true
		 * @param delta how much time has passed since last check. Helpful for time-based events
         * @return whether or not the act will end
         */
        public boolean isDone(float delta);
    }
    
    public class MovementAction implements CutsceneAction
    {
        private MovementComponent movement;
		private AIComponent brain;
        private Vector2 targetPosition;
		private float oldSpeed; // original speed;
		private float tempSpeed; // temporary speed; allows entities to move faster during cutscenes
        
        public MovementAction(String characterName, float targetX, float targetY, float speed)
        {
			movement = Mappers.movements.get(screen.getEngine().getEntity(characterName));
			brain = Mappers.ai.get(screen.getEngine().getEntity(characterName));
			
            targetPosition = new Vector2(targetX, targetY);
			oldSpeed = movement.speed;
			tempSpeed = speed;
        }
        
        @Override
        public void start()
        {
			brain.timeStep = 0.05f;
			brain.state = new MoveToState(targetPosition);
			movement.speed = tempSpeed;
        }
        
        @Override
        public boolean isDone(float delta) 
        {
			if (brain.state instanceof StayState)
			{
				movement.speed = oldSpeed;
				brain.resetTimeStep();
				return true;
			}
			return false;
        }   
    }
    
    public class DialogueAction implements CutsceneAction
    {
        private DialogueComponent dialogue;
        
        public DialogueAction(DialogueComponent component)
        {
            dialogue = component;
        }
        
        @Override
        public void start()
        {
            screen.getDialogueManager().startDialogue(dialogue);
        }
        
        @Override
        public boolean isDone(float delta) 
        {
			// GameState gets switched to Dialogue during start(), then goes back to cutscene during endDialogue()
            return screen.getState() == GameState.Cutscene;
        }   
    }
    
    public class RotateAction implements CutsceneAction
    {
        private PositionComponent entityPosition;
        private Orientation orientation;
        
        public RotateAction(String characterName, String dir)
        {
			entityPosition = Mappers.positions.get(screen.getEngine().getEntity(characterName));
            orientation = Orientation.fromString(dir);
        }
        
        @Override
        public void start()
        {
            entityPosition.orientation = orientation;
        }
        
        @Override
        public boolean isDone(float delta) 
        {
            return true; // Instant
        }   
    }
    
    public class KillAction implements CutsceneAction
    {
        private Entity entity;
		private float actionTime;
        
        public KillAction(String characterName)
        {
            entity = screen.getEngine().getEntity(characterName);
        }
        
        @Override
        public void start()
        {
            Mappers.lives.get(entity).damage(Mappers.lives.get(entity).maxHealth);
        }
        
        @Override
        public boolean isDone(float delta) 
        {
            actionTime += delta;
			// Time it takes to play out death animation, maybe customize later
			return actionTime > 1.5f;
        }   
    }
	
	public class AnimateAction implements CutsceneAction
    {
        private AnimMapObjectComponent animationComponent;
		private AnimationState state;
		private float actionTime;
		private float maxTime;
        
        public AnimateAction(String characterName, String animation, float time)
        {
			animationComponent = Mappers.animMapObjects.get(screen.getEngine().getEntity(characterName));
			maxTime = time;
			
			state = AnimationState.fromString(animation);
        }
        
        @Override
        public void start()
        {
			animationComponent.animState = state;
        }
        
        @Override
        public boolean isDone(float delta) 
        {
            actionTime += delta;
			// Time it takes to play out death animation, maybe customize later
			return actionTime > maxTime;
        }   
    }
}
