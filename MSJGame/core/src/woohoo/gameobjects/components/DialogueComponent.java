package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.util.ArrayList;

public class DialogueComponent implements Component
{
    private ArrayList<DialogueLine> sequence;
    private int index;
    
	/**
	 * Holds all the dialogue component for an NPC, unless event tag is true.
	 * If event tag is true, holds all the dialogue for an event.
	 * @param id ID to locate the dialogue in XML
	 * @param event Whether this is for an NPC or an event
	 */
	public DialogueComponent(int id, boolean event)
    {
        sequence = new ArrayList<>();
        
        FileHandle handle = Gdx.files.internal(event ? "data/eventdialogue.xml" : "data/dialogue.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());       
        Element dialogue = root.getChild(id);
        
        for (Element e : dialogue.getChildrenByName("line"))
        {
            sequence.add(new DialogueLine(e.get("name"), e.get("text"), e.getInt("character")));
        }
    }
    
    public DialogueLine getCurrentLine()
    {
        if (index < sequence.size())
            return sequence.get(index);
        else
            return null; //sequence.get(sequence.size() - 1);
    }
	
	public void advance()
	{
		index++;
	}
	
	/*
	Re-sets the dialogue to the previous break, or to beginning if none are found
	*/
	public void loop()
	{
		boolean foundLoop = false;
		for (int i = index - 1; i >= 0; i--)
		{
			if (sequence.get(i).id() == -1)
			{
				index = i + 1;
				foundLoop = true;
			}
		}
		
		if (!foundLoop) index = 0;
	}
	
	public class DialogueLine
	{
		private String name;
		private String text;
		private int id;
		
		public DialogueLine(String name, String text, int id)
		{
			this.name = name;
			this.text = text;
			this.id = id;
		}
		
		public String name() { return name; }
		public String text() { return text; }
		public int id() { return id; }
	}
}
