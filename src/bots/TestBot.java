package bots;

import java.awt.Graphics;
import java.awt.Image;

import arena.BotInfo;
import arena.Bullet;

public class TestBot extends Bot{

	@Override
	public void newRound() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
		System.out.println(me.getX()+", "+me.getY());
		return 2;
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTeamName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String outgoingMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void incomingMessage(int botNum, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] imageNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadedImages(Image[] images) {
		// TODO Auto-generated method stub
		
	}

}
