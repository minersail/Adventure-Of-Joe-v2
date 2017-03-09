package woohoo.framework.fixturedata;

/*
Base class for data to be stored in box2d fixtures

Differs from contact commands as these data classes
	1. Store data as opposed to code
	2. Are attached to fixtures through getUserData()

ContactCommands are callbacks, managed through ContactManager
FixtureData will be attached to every single fixture

ContactCommands will use FixtureDatas to determine how to proceed with the contact
*/
public class FixtureData 
{   
    public FixtureData()
    {
    }
}
