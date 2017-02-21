package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.util.ArrayList;
import woohoo.gameobjects.BaseEntity;
import woohoo.gameobjects.Player;
import woohoo.gameobjects.Character;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.gameobjects.components.MapObjectComponent.Direction;
import woohoo.screens.PlayingScreen;
import woohoo.screens.PlayingScreen.GameState;

public class CutsceneManager
{
    private PlayingScreen screen;
    
    private ArrayList<BaseEntity> cutsceneEntities;
    private ArrayList<CutsceneAction> cutsceneActions;
    
    private CutsceneAction currentAction;
    
    public CutsceneManager(PlayingScreen scr)
    {
        screen = scr;
        cutsceneEntities = new ArrayList<>();
        cutsceneActions = new ArrayList<>();
    }
    
    /**
	 * Updates only those entities that are free to move in the current cutscene
	 * @param delta elapsed time since last frame
	 */
	public void update(float delta)
	{
		for (BaseEntity entity : cutsceneEntities)
		{
			entity.update(delta);
			
			if (entity instanceof Player)
			{
				screen.getEngine().adjustCamera((Player)entity);
			}
		}
        
        if (currentAction.isDone())
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
	}
    
    public void startCutscene(int cutsceneID)
    {
        FileHandle handle = Gdx.files.internal("data/cutscenes.xml");
        
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
            
            if (e.get("type").equals("move"))
            {
                action = new MovementAction(e.get("name"), e.getFloat("locX"), e.getFloat("locY"), e.getFloat("speed"));
                cutsceneActions.add(action);
            }
            else if (e.get("type").equals("dialogue"))
            {
                action = new DialogueAction(component);
                cutsceneActions.add(action);
            }
            else if (e.get("type").equals("rotate"))
            {
                action = new RotateAction(e.get("name"), e.get("direction"));
                cutsceneActions.add(action);
            }
            else if (e.get("type").equals("kill"))
            {
                action = new KillAction(e.get("name"));
                cutsceneActions.add(action);
            }
        }
        
        screen.setState(GameState.Cutscene);
        currentAction = cutsceneActions.get(0);
        currentAction.start();
    }
    
    public void endCutscene()
    {
		for (BaseEntity entity : cutsceneEntities)
		{
			if (entity instanceof Player)
			{
				entity.getComponent(AIComponent.class).setAIMode(AIComponent.AIMode.Input);
				((Player)entity).stop();
			}
		}
		
        cutsceneEntities.clear();
        cutsceneActions.clear();
        screen.setState(GameState.Playing);
    }
    
    public interface CutsceneAction
    {
        public void start();
        
        /**
         * Performs a cutscene action. isDone() will be called until it returns true
         * @return whether or not the act will end
         */
        public boolean isDone();
    }
    
    public class MovementAction implements CutsceneAction
    {
        private Character character;
        private Vector2 targetPosition;
		private float oldSpeed; // original speed;
		private float tempSpeed; // temporary speed; allows entities to move faster during cutscenes
        
        public MovementAction(String characterName, float targetX, float targetY, float speed)
        {
            character = (Character)screen.getEngine().getEntity(characterName);
            targetPosition = new Vector2(targetX, targetY);
			oldSpeed = character.getSpeed();
			tempSpeed = speed;
        }
        
        @Override
        public void start()
        {
            character.getComponent(AIComponent.class).setTimeStep(0.05f);
            character.setTarget(targetPosition);
			character.setSpeed(tempSpeed);
        }
        
        @Override
        public boolean isDone() 
        {
			if (character.getComponent(AIComponent.class).getAIMode() == AIComponent.AIMode.Stay)
			{
				character.setSpeed(oldSpeed);
				character.getComponent(AIComponent.class).resetTimeStep();
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
        public boolean isDone() 
        {
			// GameState gets switched to Dialogue during start(), then goes back to cutscene during endDialogue()
            return screen.getState() == GameState.Cutscene;
        }   
    }
    
    public class RotateAction implements CutsceneAction
    {
        private Character character;
        private Direction direction;
        
        public RotateAction(String characterName, String dir)
        {
            character = (Character)screen.getEngine().getEntity(characterName);
            
            switch (dir.toLowerCase())
            {
                case "up":                    
                    direction = Direction.Up;
                    break;
                case "left":
                    direction = Direction.Left;
                    break;
                case "right":
                    direction = Direction.Right;
                    break;
                case "down":
                default:
                    direction = Direction.Down;
                    break;
            }
        }
        
        @Override
        public void start()
        {
            character.setDirection(direction);
        }
        
        @Override
        public boolean isDone() 
        {
            return true; // Instant
        }   
    }
    
    public class KillAction implements CutsceneAction
    {
        private Character character;
        
        public KillAction(String characterName)
        {
            character = (Character)screen.getEngine().getEntity(characterName);
        }
        
        @Override
        public void start()
        {
            character.die();
        }
        
        @Override
        public boolean isDone() 
        {
            return true; // Instant
        }   
    }
}
