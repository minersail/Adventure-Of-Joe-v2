package woohoo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import woohoo.framework.AssetLoader;
import woohoo.framework.InputHandler;
import woohoo.gameworld.GameRenderer;
import woohoo.gameworld.GameWorld;

public class PlayingScreen implements Screen
{
    private float runTime;

    // This is the constructor, not the class declaration
    public PlayingScreen()
    {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

		AssetLoader.load();
		GameRenderer.initialize();
        Gdx.input.setInputProcessor(new InputHandler());
    }

    @Override
    public void render(float delta)
    {
        runTime += delta;
        GameWorld.update(delta);
        GameRenderer.render(runTime);
    }

    @Override
    public void resize(int width, int height)
    {
        System.out.println("GameScreen - resizing");
    }

    @Override
    public void show()
    {
        System.out.println("GameScreen - show called");
    }

    @Override
    public void hide()
    {
        System.out.println("GameScreen - hide called");
    }

    @Override
    public void pause()
    {
        System.out.println("GameScreen - pause called");
    }

    @Override
    public void resume()
    {
        System.out.println("GameScreen - resume called");
    }

    @Override
    public void dispose()
    {

    }
}
