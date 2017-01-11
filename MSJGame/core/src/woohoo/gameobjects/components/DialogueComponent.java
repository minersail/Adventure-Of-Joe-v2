package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import woohoo.framework.DialogueManager;

public class DialogueComponent implements Component
{
    private DialogueManager manager;
    //private String file
    
    public DialogueComponent(DialogueManager dm)//, String textfile
    {
        manager = dm;
    }
}
