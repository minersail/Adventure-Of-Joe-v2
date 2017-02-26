package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.util.ArrayList;
import woohoo.framework.quests.Quest;
import woohoo.framework.quests.Quest.QuestState;
import woohoo.screens.PlayingScreen;

public class QuestManager
{
	private ArrayList<Quest> quests;
	private PlayingScreen screen;
	
	private ScrollPane pane;
	private Table paneTable;
	
	public QuestManager(PlayingScreen scr, Skin skin)
	{
		quests = new ArrayList<>();
		paneTable = new Table();
		pane = new ScrollPane(paneTable);
		screen = scr;
		
		FileHandle handle = Gdx.files.internal("data/quests.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());
        
        for (Element questEl : root.getChildrenByName("quest"))
        {		
			Quest quest = new Quest()
			{				
				@Override
				public void indicate()
				{
					
				}
				
				@Override
				public void reward()
				{
					
				}
			};
			quest.setID(questEl.getInt("id"));
			quest.setState(QuestState.Current);
			quests.add(quest);
			
			QuestUI line = new QuestUI(quest, skin);
			
			paneTable.add(line).prefSize(Gdx.graphics.getWidth(), 100);
			paneTable.row();
		}
		paneTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		paneTable.debug();
		pane.setPosition(0, Gdx.graphics.getHeight() - 200);
		pane.setSize(Gdx.graphics.getWidth(), 200);
	}
	
	public void showQuests()
	{
		screen.getUI().addActor(pane);
		((QuestUI)((Table)pane.getWidget()).getCells().get(0).getActor()).getChildren().first().setWidth(1024);
	}
	
	public void indicateAll()
	{
		for (Quest quest : quests)
		{
			quest.indicate();
		}
	}
	
	public Quest getQuest(int id)
	{
		for (Quest quest : quests)
		{
			if (quest.getID() == id)
				return quest;
		}
		
		return null;
	}
	
	public class QuestUI extends HorizontalGroup
	{
		private Label label;
		private Quest quest;
		
		public QuestUI(Quest q, Skin skin)
		{
			quest = q;
			label = new Label(quest.getID() + "", skin);
			label.setSize(Gdx.graphics.getWidth(), 100);
			
			super.addActor(label);
			super.setSize(Gdx.graphics.getWidth(), 100);
		}
	}
}
