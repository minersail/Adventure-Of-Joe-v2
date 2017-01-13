package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DialogueComponent implements Component
{
    private ArrayList<String> sequence;
    private int index;
    
    public DialogueComponent(int id)
    {
        sequence = new ArrayList<>();
        
        FileHandle handle = Gdx.files.internal("data/dialogue.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());       
        Element dialogue = root.getChild(id);
        
        for (Element e : dialogue.getChildrenByName("line"))
        {
            sequence.add(e.get("text"));
        }
    }
    
    public String getNext()
    {
        index++;
        if (index < sequence.size())
            return sequence.get(index);
        else
            return "<BREAK>";
    }
    
    public String getFirst()
    {
        return sequence.get(0);
    }
}
