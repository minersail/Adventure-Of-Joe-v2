package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import woohoo.gameobjects.components.DialogueComponent;

public class DialogueManager
{
    private DialogueComponent currentDialogue;
    private TextureRegion displayBox;
	private Stage dialogueUI;
    
    public DialogueManager(TextureRegion tr, FitViewport vp, BitmapFont font)
    {
        displayBox = tr;
		dialogueUI = new Stage(vp);
		
		final Label label = new Label("Hello", new LabelStyle(font, Color.GREEN));
		label.setWidth(1);
		label.setHeight(1);
		label.setPosition(6, 10);
		label.setFontScale(0.025f);
				
		label.addListener(new InputListener() 
		{
            @Override
            public boolean keyDown(InputEvent event, int keycode) 
			{
                if (keycode == Keys.SPACE)
				{
					label.setText("Bye");
				}
				
                return true;
            }
        });		
		
		dialogueUI.addActor(label);
		dialogueUI.setKeyboardFocus(label);
    }
    
    public void startDialogue(DialogueComponent dia)
    {
        currentDialogue = dia;
		Gdx.input.setInputProcessor(dialogueUI);
    }
    
    public void runDialogue(float delta)
    {
        dialogueUI.act(delta);
		dialogueUI.draw();
		System.out.println(dialogueUI.getActors().get(0).getHeight());
    }
    
    public TextureRegion getBox()
    {
        return displayBox;
    }
}
