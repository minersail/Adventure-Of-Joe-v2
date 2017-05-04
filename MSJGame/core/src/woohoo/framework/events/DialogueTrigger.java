package woohoo.framework.events;

import woohoo.framework.DialogueManager;

public class DialogueTrigger implements EventTrigger<DialogueManager>
{
    private int triggerID;
    
    public DialogueTrigger(int id)
    {
        triggerID = id;
    }
    
    @Override
    public boolean check(DialogueManager dialogueManager)
    {
        return dialogueManager.getCurrentDialogue().getCurrentLine().getInt("triggerid") == triggerID;
    }
}