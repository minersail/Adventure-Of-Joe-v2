package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.gameworld.gamestates.CutsceneState;
import woohoo.gameworld.gamestates.DialogueState;
import woohoo.gameworld.gamestates.GameState;
import woohoo.gameworld.gamestates.PlayingState;
import woohoo.screens.PlayingScreen;

public class DialogueManager
{
	private PlayingScreen screen;
    private DialogueComponent currentDialogue;
    private Image face;
	private Label message;
	private Label name;
	
	private final int MARGIN = 100;
	private final int NAMEWIDTH = 100;
	private final int NAMEHEIGHT = 100;
    
    public DialogueManager(PlayingScreen scr, Skin skin)
    {
		screen = scr;
				
		message = new Label("", skin);
		message.setSize(Gdx.graphics.getWidth() - MARGIN * 2 - NAMEWIDTH, 100);
		message.setPosition(MARGIN + NAMEWIDTH, 0);
		message.setAlignment(Align.center);
		message.setWrap(true);
		message.setFontScale(0.75f);
		
		name = new Label("", skin);
		name.setSize(NAMEWIDTH, NAMEHEIGHT);
		name.setPosition(MARGIN, 0);
		name.setAlignment(Align.bottom);
		name.setWrap(true);	
		name.setFontScale(0.4f);
		
		face = new Image();
		face.setSize(64, 64);
		face.setPosition(MARGIN + 18, 30);
		face.setAlign(Align.center);
    }
    
    public void startDialogue(DialogueComponent dia)
    {
        currentDialogue = dia;
		
		TextureRegionDrawable faceRegion = new TextureRegionDrawable(screen.getIDManager().getCharacter(dia.getCurrentLine().id()).getFace());
		
        message.setText(dia.getCurrentLine().text());
		name.setText(dia.getCurrentLine().name());
		face.setDrawable(faceRegion);
		
		screen.getUI().addActor(message);
		screen.getUI().addActor(name);
		screen.getUI().addActor(face);
		
		screen.setState(new DialogueState());
    }
	
	public void advanceDialogue()
	{
		currentDialogue.advance();
		if (currentDialogue.getCurrentLine() == null)
		{
			endDialogue(new PlayingState());
			return;
		}
		
		if (currentDialogue.getCurrentLine().id() == -1)
		{
			switch (currentDialogue.getCurrentLine().text())
			{
				case "LOOP":
					currentDialogue.loop();
					endDialogue(new PlayingState());
					break;
				case "BREAK":
					currentDialogue.advance();
					endDialogue(new PlayingState());
					break;
				case "CUTSCENE":
					currentDialogue.advance();
					endDialogue(new CutsceneState());
					break;
				default:
					break;
			}
			return;
		}
		
		TextureRegionDrawable faceRegion = new TextureRegionDrawable(screen.getIDManager().getCharacter(currentDialogue.getCurrentLine().id()).getFace());
		
		message.setText(currentDialogue.getCurrentLine().text());
		name.setText(currentDialogue.getCurrentLine().name());	
		face.setDrawable(faceRegion);
	}
	
	public void endDialogue(GameState newState)
	{
		message.remove();
		name.remove();
		face.remove();
		
		screen.setState(newState);
	}
	
	public DialogueComponent getCurrentDialogue()
	{
		return currentDialogue;
	}
}
