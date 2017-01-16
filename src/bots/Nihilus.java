package bots;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Arrays;

import arena.BotInfo;
import arena.Bullet;

public class Nihilus extends Bot {
	Image current;
	int targetPos = 0;
	int targetNum = 0;
	double oldX;
	double oldy;
	@Override
	public void draw(Graphics g, int x, int y) {
		g.drawImage(current, x, y, Bot.RADIUS*2, Bot.RADIUS*2, null);
	}

	@Override
	public void newRound() {
	}

	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
		int best = 0;
		if((targetNum == me.getBotNumber()||!Arrays.asList(liveBots).contains(targetNum))||liveBots[targetNum].isOverheated()||deadBots[targetNum].isOverheated()&&liveBots.length> 1){
			targetPos = hunt(liveBots,me);
			targetNum = liveBots[targetPos].getBotNumber();
			

		}

		if(bullets.length > 0 && best == 0){
			best = dodge(bullets, me);
		}
		if(best == 0){
			best = attack(liveBots, me, targetPos);
		}
		if(best == 0){
			best = stalk(liveBots, me, targetPos);
		}



		oldX = me.getX();
		return best;
	}

	public int dodge(Bullet[] bullets, BotInfo me){
		for(int i =0;i < bullets.length;i++){
			double mex =  me.getX()+10; double mey =  me.getY()+10; double bulletx = bullets[i].getX(); double bullety = bullets[i].getY(); double xspeed = bullets[i].getXSpeed(); double yspeed = bullets[i].getYSpeed();

			if(yspeed !=0 && (bulletx < mex + 10 && bulletx > mex - 10) && Math.abs(mey - bullety) <= 45){
				if(mex - bulletx > 0){
					return 4;
				}
				else if(mex - bulletx <= 0){
					return 3;
				}
			}
			if(xspeed !=0 && (bullety < mey + 10 && bullety > mey - 10) && Math.abs(mex - bulletx) <= 45){
				if(mey - bullety > 0){
					return 2;
				}
				else if(mey - bullety <= 0){
					return 1;
				}
			}
		}
		return 0;
	}

	public int stalk(BotInfo[]liveBots, BotInfo me, int targetPos){
		double mex =  me.getX()+10; double mey =  me.getY()+10; double enemyx = liveBots[targetPos].getX()+10; double enemyy = liveBots[targetPos].getY()+10;
		if(Math.abs(mex-enemyx)*Math.abs(mex-enemyx) + Math.abs(mey-enemyy)*Math.abs(mey-enemyy) > 2500){

			if(Math.abs(mex-enemyx) > Math.abs(mey-enemyy)){
				if(mex - enemyx < 0){
					return 4;
				}
				if(mex - enemyx >0){
					return 3;
				}
			}else if(Math.abs(mex-enemyx) <= Math.abs(mey-enemyy)){
				if(mey - enemyy < 0){
					return 2;
				}
				if(mey - enemyy >0){
					return 1;
				}
			}
		}

		return 0;
	}

	public int attack(BotInfo[]liveBots, BotInfo me, int targetPos){
		double mex =  me.getX()+10; double mey =  me.getY()+10; double enemyx = liveBots[targetPos].getX()+10; double enemyy = liveBots[targetPos].getY()+10;

		if((enemyx < mex + 15 && enemyx > mex - 15)){
			if(mey - enemyy < 0){
				return 6;
			}
			if(mey - enemyy > 0){
				return 5;
			}
		}
		if((enemyy < mey + 15 && enemyy > mey - 15)){
			if(mex - enemyx < 0){
				return 8;
			}
			if(mex - enemyx > 0){
				return 7;
			}
		}
		return 0;
	}

	public int hunt(BotInfo[]liveBots, BotInfo me){
		double closest = 9999999;
		int closestBot=0;

		for(int i = 0; i < liveBots.length;i++){
			double mex =  me.getX()+10; double mey =  me.getY()+10; double enemyx = liveBots[i].getX()+10; double enemyy = liveBots[i].getY()+10;
			//get hypontenuse
			double distance = (Math.sqrt(Math.pow(Math.abs(enemyx-mex), 2) + Math.pow((Math.abs(enemyy-mey)), 2)));
					if(distance < closest){
						closest = distance;
						closestBot = i; 
					}
		}
		return closestBot;
	}

	@Override
	public String getName() {
		return"Nihilus";
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
		String[] paths = {"Nihilus.png"};
		return paths;
	}



	@Override
	public void loadedImages(Image[] images) {
		current = images[0];

	}

}