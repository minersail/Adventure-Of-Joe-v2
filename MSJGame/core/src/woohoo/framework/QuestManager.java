package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.framework.quests.Quest;
import woohoo.framework.quests.Quest.QuestState;
import woohoo.screens.PlayingScreen;

public class QuestManager
{
	private IntMap<QuestUI> quests;
	private PlayingScreen screen;
	
	private Label header;
	private Table paneTable;
	private ScrollPane pane;
	
	public QuestManager(PlayingScreen scr, Skin skin)
	{
		quests = new IntMap<>();
		header = new Label("Quests", skin);
		paneTable = new Table();
		pane = new ScrollPane(paneTable);
		
		screen = scr;
		
		// All quests are loaded hidden
		FileHandle handle = Gdx.files.internal("data/quests.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());
        
        for (Element questEl : root.getChildrenByName("quest"))
        {		
			Quest quest = new Quest(questEl.getInt("id"), questEl.get("description"), screen.getAssets().get("ui/" + questEl.get("type") + "quest.png", Texture.class));
			
			quest.setState(QuestState.Current);
			quest.getIndicator().setPosition(questEl.getInt("locX"), questEl.getInt("locY"));
			
			QuestUI line = new QuestUI(quest, skin);
			quests.put(quest.getID(), line);
		}
		
		header.setSize(Gdx.graphics.getWidth(), 100);
		header.setPosition(0, Gdx.graphics.getHeight() - 100);
		header.setAlignment(Align.center);
		paneTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		paneTable.debugAll();
		pane.setSize(Gdx.graphics.getWidth(), 200);
		pane.setPosition(0, Gdx.graphics.getHeight() - 300);
	}
	
	public void displayQuest(int id)
	{
		paneTable.add(quests.get(id)).prefSize(Gdx.graphics.getWidth(), 100);
		paneTable.row();	
	}
	
	public void startQuest(int id)
	{
		screen.addEntity(quests.get(id).getQuest().getIndicator());
	}
	
	public void endQuest(int id)
	{
		screen.removeEntity(quests.get(id).getQuest().getIndicator());
	}
	
	public void showQuests()
	{
		screen.getUI().addActor(pane);
		screen.getUI().addActor(header);
		screen.setState(PlayingScreen.GameState.Quests);
	}
	
	public void closeQuests()
	{
		pane.remove();
		header.remove();
		screen.setState(PlayingScreen.GameState.Playing);
	}
	
	public Quest getQuest(int id)
	{
		return quests.get(id).getQuest();
	}
	
	public class QuestUI extends HorizontalGroup
	{
		private Label description;
		private Quest quest;
		
		public QuestUI(Quest q, Skin skin)
		{
			quest = q;
			description = new Label(quest.getDescription(), skin);
			description.setSize(Gdx.graphics.getWidth(), 100);
			description.setAlignment(Align.center);
			description.layout();
			//label.setWrap(true);
						
			super.addActor(description);
			super.setLayoutEnabled(false);
		}
		
		public Quest getQuest()
		{
			return quest;
		}
	}
}
