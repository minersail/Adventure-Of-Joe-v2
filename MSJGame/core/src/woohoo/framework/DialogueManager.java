package woohoo.framework;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import woohoo.gameobjects.components.DialogueComponent;

public class DialogueManager
{
    private DialogueComponent currentDialogue;
    private TextureRegion displayBox;
    
    public DialogueManager(TextureRegion tr)
    {
        displayBox = tr;
    }
    
    public void setDialogue(DialogueComponent dia)
    {
        currentDialogue = dia;
    }
    
    public void runDialogue()
    {
        
    }
    
    public TextureRegion getBox()
    {
        return displayBox;
    }
}
