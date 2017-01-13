package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import woohoo.gameobjects.components.DialogueComponent;

public class DialogueManager
{
    private DialogueComponent currentDialogue;
    private TextureRegion displayBox;
	private Label message;
	private Stage ui;
    
    public DialogueManager(Stage s, Skin skin, TextureRegion tr)
    {
        displayBox = tr;
		ui = s;
		
		message = new Label("", skin);
		message.setWidth(Gdx.graphics.getWidth());
		message.setHeight(100);
		message.setPosition(0, 0);
		message.setAlignment(Align.center);
		message.setWrap(true);
				
		message.addListener(new InputListener() 
		{
            @Override
            public boolean keyDown(InputEvent event, int keycode) 
			{
                if (keycode == Keys.SPACE)
				{
					message.setText(currentDialogue.getNext());
				}
				
                return true;
            }
        });		
    }
    
    public void setDialogue(DialogueComponent dia)
    {
        currentDialogue = dia;
        
        message.setText(dia.getFirst());
		
		ui.addActor(message);
		ui.setKeyboardFocus(message);
    }
    
    public TextureRegion getBox()
    {
        return displayBox;
    }
}
