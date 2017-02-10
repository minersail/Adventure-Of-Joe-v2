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
        
        for (Element e : cutscene.getChildrenByName("action"))
        {
            CutsceneAction action;
            
            if (e.get("type").equals("move"))
            {
                action = new MovementAction(e.get("name"), e.getFloat("locX"), e.getFloat("locY"));
                cutsceneActions.add(action);
            }
            else if (e.get("type").equals("dialogue"))
            {
                action = new DialogueAction(e.getInt("id"));
                cutsceneActions.add(action);
            }
        }
        
        currentAction = cutsceneActions.get(0);
        currentAction.start();
        screen.setState(GameState.Cutscene);
    }
    
    public void endCutscene()
    {
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
        
        public MovementAction(String characterName, float targetX, float targetY)
        {
            character = (Character)screen.getEngine().getEntity(characterName);
            targetPosition = new Vector2(targetX, targetY);
        }
        
        @Override
        public void start()
        {
            character.getComponent(AIComponent.class).enable(true);
            character.getComponent(AIComponent.class).setAIMode(AIComponent.AIMode.MoveTo);
            character.getComponent(AIComponent.class).setTargetPosition(targetPosition);
        }
        
        @Override
        public boolean isDone() 
        {
            return character.getComponent(AIComponent.class).getAIMode() == AIComponent.AIMode.Stay;
        }   
    }
    
    public class DialogueAction implements CutsceneAction
    {
        private DialogueComponent dialogue;
        
        public DialogueAction(int dialogueID)
        {
            dialogue = new DialogueComponent(dialogueID, true);
        }
        
        @Override
        public void start()
        {
            screen.getDialogueManager().startDialogue(dialogue);
        }
        
        @Override
        public boolean isDone() 
        {
            return dialogue.getCurrentLine().id() == -1;
        }   
    }
}
