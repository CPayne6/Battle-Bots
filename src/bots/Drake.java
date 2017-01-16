package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import arena.BotInfo;
import arena.Bullet;

public class Drake extends Bot{

	Image current;

	boolean cocked=true;

	private double x, y;
	private int shotCounter=20;
	public Drake(){

	}
	@Override
	public void newRound() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
		int returnVal=0;
		x=me.getX();
		y=me.getY();
		BotInfo closestBot=null;
		double distance=10000;
		double otherX;
		double otherY;
		for(int i=0;i<liveBots.length;i++){
			otherX=liveBots[i].getX();
			otherY=liveBots[i].getY();
			if(me.getBotNumber() != liveBots[i].getBotNumber()){
				if(Math.sqrt((otherX-x)*(otherX-x)+(otherY-y)*(otherY-y))<distance){
					distance=Math.sqrt((otherX-x)*(otherX-x)+(otherY-y)*(otherY-y));
					closestBot=liveBots[i];
					
				}
			}

			//				if(x<otherX && y<otherY){
			//				if((otherX-x)*(otherX-x)+(otherY-y)*(otherY-y)<distance){
			//				distance=(otherX-x)*(otherX-x)+(otherY-y)*(otherY-y);
			//		}
			//}
			//				else if(x<otherX && y>otherY){
			//				if((otherX-x)*(otherX-x)+(y-otherY)*(y-otherY)<distance){
			//				distance=(otherX-x)*(otherX-x)+(y-otherY)*(y-otherY);
			//		}
			//}
			//				else if(x>otherX && y<otherY){
			//				if((x-otherX)*(x-otherX)+(otherY-y)*(otherY-y)<distance){
			//				distance=(x-otherX)*(x-otherX)+(otherY-y)*(otherY-y);
			//		}
			//}
			//				else if(x>otherX && y>otherY){
			//				if((x-otherX)*(x-otherX)+(y-otherY)*(y-otherY)<distance){
			//				distance=(x-otherX)*(x-otherX)+(otherY)*(otherY-y);
			//		}
			//}

		}

		otherX=closestBot.getX();
		otherY=closestBot.getY();

		double xDistance;
		double yDistance;

		boolean greaterX;
		boolean greaterY;


		if(otherX>x){
			xDistance = otherX-x;
			greaterX=false;
		}
		else{
			xDistance = x-otherX;
			greaterX=true;
		}

		if(otherY>y){
			yDistance = otherY-y;
			greaterY=false;
		}
		else{
			yDistance = y-otherY;
			greaterY=true;
		}
		if(distance>100){
			if(xDistance>yDistance){
				if(greaterX){
					returnVal=3;
				}
				else{
					returnVal=4;
				}
			}
			else{
				if(greaterY){
					returnVal=1;
				}
				else{
					returnVal=2;
				}
			}
		}
		else{
			if(greaterX){
				returnVal=3;
			}
			else{
				returnVal=4;
			}
		}
		if(shotCounter<0){
			returnVal=shootBullet(liveBots,returnVal);
		}
		else
			shotCounter--;
		returnVal=dodgeBullet(bullets, returnVal);

		return returnVal;
	}


	public int shootBullet(BotInfo[] liveBots, int returnVal){
		for(int i=0;i<liveBots.length;i++){
			if(liveBots[i].getX()-(x+10)<=50 || liveBots[i].getY()-(y+10)<=50){
				for(int num=0;num<=20;num++){

					if((int)liveBots[i].getY()==(int)y+num && liveBots[i].getX()<x+10){
						//	if((liveBots[i].getXSpeed()>0 && liveBots[i].getX()<x+10) || (liveBots[i].getXSpeed()<0 && liveBots[i].getX()>x+10)){
						shotCounter+=20;
						return 7;
						
						//	}
					}
					else if((int)liveBots[i].getY()==(int)y+num && liveBots[i].getX()>x+10){
						//if((liveBots[i].getXSpeed()>0 && liveBots[i].getX()<x+10) || (bullets[i].liveBots()<0 && liveBots[i].getX()>x+10)){
						shotCounter+=20;
						return 8;
						//}
					}
					if((int)liveBots[i].getX()==(int)x+num && liveBots[i].getY() < y+10){
						//if((liveBots[i].getYSpeed()>0 && liveBots[i].getY()<y+10) || (liveBots[i].getYSpeed()<0 && liveBots[i].getY()>y+10)){
						shotCounter+=20;
						return 5;
						//}
					}
					else if((int)liveBots[i].getX()==(int)x+num && liveBots[i].getY() > y+10){
						//if((liveBots[i].getYSpeed()>0 && liveBots[i].getY()<y+10) || (liveBots[i].getYSpeed()<0 && liveBots[i].getY()>y+10)){
						shotCounter+=20;
						return 6;
						//}
					}
				}
			}
		}
		return returnVal;
	}

	public int dodgeBullet(Bullet[] bullets, int val){

		for(int i=0; i<bullets.length; i++){
			double bY = bullets[i].getY();
			double bX = bullets[i].getX();
			for(int num = 0 ; num < 15 ; num++){
				if((bullets[i].getYSpeed() > 0 && bX < x) || (bullets[i].getYSpeed() < 0 && bX > x)){
					if((int)bX==(int)x+num){
						val=3;
					}
					else if((int)bX==(int)x-num){
						val=4;
					}
				}
				else if((bullets[i].getXSpeed() > 0 && bY < y) || (bullets[i].getXSpeed() < 0 && bY > y)){
					if((int)bY==(int)y+num){
						val=1;
					}
					else if((int)bY==(int)x-num){
						val=2;
					}
				}
			}

		}

		return val;
	}
	
	public int dodgeBullet(Bullet[] bullets, int returnVal, int e){

		for(int i=0;i<bullets.length;i++){
			if(bullets[i].getX()-(x+10)<=50 || bullets[i].getY()-(y+10)<=50){
				for(int num=0;num<=9;num++){

					if((int)bullets[i].getY()==(int)y+num){
						if((bullets[i].getXSpeed()>0 && bullets[i].getX()<x+10) || (bullets[i].getXSpeed()<0 && bullets[i].getX()>x+10)){
							return 2;
						}
					}
					else if((int)bullets[i].getY()==(int)y+num+10){
						if((bullets[i].getXSpeed()>0 && bullets[i].getX()<x+10) || (bullets[i].getXSpeed()<0 && bullets[i].getX()>x+10)){
							return 1;
						}
					}
					if((int)bullets[i].getX()==(int)x+num){
						if((bullets[i].getYSpeed()>0 && bullets[i].getY()<y+10) || (bullets[i].getYSpeed()<0 && bullets[i].getY()>y+10)){
							return 4;
						}
					}
					else if((int)bullets[i].getX()==(int)x+num+10){
						if((bullets[i].getYSpeed()>0 && bullets[i].getY()<y+10) || (bullets[i].getYSpeed()<0 && bullets[i].getY()>y+10)){
							return 3;
						}
					}
				}
			}
		}
		return returnVal;
	}


	@Override
	public void draw(Graphics g, int x, int y) {
		g.drawImage(current, x, y, null);
		if (!cocked)
		{
			g.setColor(Color.red);
			g.fillRect(x+3, y+3, RADIUS*2-6, RADIUS*2-6);
		}

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "CPayneTRayne";
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
		String[] image={"kylo rens nose.png"};
		return image;
	}

	@Override
	public void loadedImages(Image[] images) {
		// TODO Auto-generated method stub
		current=images[0];
	}

}

