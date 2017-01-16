package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import arena.BotInfo;
import arena.Bullet;

public class CPayneTRayne extends Bot{

	Image current;

	boolean cocked=true;
	boolean deadAbove,deadBelow,deadOnRight,deadOnLeft = false;
	private double x, y;
	private int shotCounter=20;

	public CPayneTRayne(){

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
		int myNumber=me.getBotNumber();

		BotInfo closestBot=findClosestBot(liveBots,myNumber);

		double otherX=closestBot.getX();
		double otherY=closestBot.getY();

		double distance=Math.sqrt((otherX-x)*(otherX-x)+(otherY-y)*(otherY-y));

		double xDistance;
		double yDistance;

		boolean greaterX;
		boolean greaterY;

		xDistance = Math.abs(otherX-x);
		if(otherX>x){
			greaterX=false;
		}
		else{
			greaterX=true;
		}
		yDistance = Math.abs(otherY-y);
		if(otherY>y){
			greaterY=false;
		}
		else{
			greaterY=true;
		}
		if((int)xDistance>45 && (int)yDistance>45){
			System.out.println("your");
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
		else if((int)xDistance==45 && (int) yDistance==45){
			System.out.println("mom");

			if(greaterX){
				returnVal=3;
			}
			else{
				returnVal=4;
			}

		}
		else if((int)xDistance>45 && yDistance<45){
			System.out.println("sucks");

			if(greaterY){
				returnVal=1;
			}
			else{
				returnVal=2;
			}

		}
		else if((int)yDistance>45 && xDistance<45){
			System.out.println("sucks");

			if(greaterY){
				returnVal=1;
			}
			else{
				returnVal=2;
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
		else{
			shotCounter--;
		}
		if(deadBots.length!=0 && deadBots[0]!=null){
			System.out.println("dodging tomb");
			returnVal=moveAroundTomb(findClosestBot(deadBots, myNumber),returnVal);
		}
		returnVal=dodgeBullet(bullets, returnVal);
		System.out.println("");
		System.out.println(returnVal);
		return returnVal;
	}

	public BotInfo findClosestBot(BotInfo[] bots, int myNumber){
		int i;
		double distance=10000;
		double otherX;
		double otherY;
		BotInfo closestBot=null;
		for(i=0;i<bots.length;i++){
			otherX=bots[i].getX();
			otherY=bots[i].getY();
			if(myNumber != bots[i].getBotNumber()){
				if(Math.sqrt((otherX-x)*(otherX-x)+(otherY-y)*(otherY-y))<distance){
					distance=Math.sqrt((otherX-x)*(otherX-x)+(otherY-y)*(otherY-y));
					closestBot=bots[i];

				}
			}
		}
		return closestBot;
	}

	public int moveAroundTomb(BotInfo deadBot, int returnVal){
		//double xDistance=Math.sqrt((deadBot.getX()-x)*(deadBot.getX()-x));
		//double yDistance=Math.sqrt((deadBot.getY()-y)*(deadBot.getY()-y));
		System.out.println("mom");

		if(deadBot!=null &&Math.sqrt((deadBot.getX()-x)*(deadBot.getX()-x)+(deadBot.getY()-y)*(deadBot.getY()-y))<=30){
			System.out.println("dick");
			for(int num=0;num<20;num++){

				if((returnVal==1 || returnVal==2) && ((int)deadBot.getX()==(int)x+num || (int)deadBot.getX()+num==(int)x)){
					System.out.println("why isn't this working??????");
					if(deadBot.getY()>y){
						deadBelow=true;
					}
					else{
						deadAbove=true;
					}
					System.out.println(deadAbove);
					if(deadBot.getX()<=x){
						return 4;
					}
					else{
						return 3;
					}
				}
				else if((returnVal==3 || returnVal==4) && ((int)deadBot.getY()==(int)y+num || (int)deadBot.getY()+num==(int)y)){
					if(deadBot.getX()>x){
						deadOnRight=true;
					}
					else{
						deadOnLeft=true;
					}
					if(deadBot.getY()<y){
						return 1;
					}
					else{
						return 2;
					}
				}

			}
		}
		System.out.println("hi");
		deadOnRight=false;deadOnLeft=false;deadAbove=false;deadBelow=false;
		return returnVal;

	}


	public int shootBullet(BotInfo[] liveBots, int returnVal){

		for(int i=0;i<liveBots.length;i++){

			for(int num=0;num<=20;num++){

				if((((int)liveBots[i].getY()==(int)y+num || (int)liveBots[i].getY()==(int)y-num)) && liveBots[i].getX()<x && (liveBots[i].getName().substring(0,6).equals("Sentry") || x-liveBots[i].getX()<70)){
					//	if((liveBots[i].getXSpeed()>0 && liveBots[i].getX()<x+10) || (liveBots[i].getXSpeed()<0 && liveBots[i].getX()>x+10)){
					shotCounter+=10;
					return 7;

					//	}
				}
				else if(((int)liveBots[i].getY()==(int)y+num || (int)liveBots[i].getY()==(int)y-num)&& liveBots[i].getX()>x && (liveBots[i].getName().substring(0,6).equals("Sentry") || liveBots[i].getX()-x<70)){
					//if((liveBots[i].getXSpeed()>0 && liveBots[i].getX()<x+10) || (bullets[i].liveBots()<0 && liveBots[i].getX()>x+10)){
					shotCounter+=10;
					return 8;
					//}
				}
				if(((int)liveBots[i].getX()==(int)x+num || (int)liveBots[i].getX()==(int)x-num)&& liveBots[i].getY() < y && (liveBots[i].getName().substring(0,6).equals("Sentry") || y-liveBots[i].getY()<70)){
					//if((liveBots[i].getYSpeed()>0 && liveBots[i].getY()<y+10) || (liveBots[i].getYSpeed()<0 && liveBots[i].getY()>y+10)){
					shotCounter+=10;
					return 5;
					//}
				}
				else if(((int)liveBots[i].getX()==(int)x+num || (int)liveBots[i].getX()==(int)x-num)&& liveBots[i].getY() > y && (liveBots[i].getName().substring(0,6).equals("Sentry") || liveBots[i].getX()-y<70)){
					//if((liveBots[i].getYSpeed()>0 && liveBots[i].getY()<y+10) || (liveBots[i].getYSpeed()<0 && liveBots[i].getY()>y+10)){
					shotCounter+=10;
					return 6;
					//}
				}

			}
		}
		return returnVal;
	}
	
	//
	public int dodgeBullet(Bullet[] bullets, int val){

		for(int i=0; i<bullets.length; i++){
			double bY = bullets[i].getY();
			double bX = bullets[i].getX();
			for(int num = 0 ; num < 10 ; num++){
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
		if(bullets.length>0){
			for(int i=0;i<bullets.length;i++){
				if(bullets[i].getX()-(x+10)<=50 && bullets[i].getY()-(y+10)<=50){

					for(int num=0;num<20;num++){

						if((int)bullets[i].getY()==(int)y+num){

							if((bullets[i].getXSpeed()>0 && bullets[i].getX()<x+num) || (bullets[i].getXSpeed()<0 && bullets[i].getX()>x+num)){
								if(bullets[i].getY()<y)
									return 2;
								else
									return 1;
							}
						}

						else if((int)bullets[i].getX()==(int)x+num){
							if((bullets[i].getYSpeed()>0 && bullets[i].getY()<y+num) || (bullets[i].getYSpeed()<0 && bullets[i].getX()>y+num)){
								System.out.println("balls");
								if(bullets[i].getX()<x)
									return 4;
								else
									return 3;
							}
						}
					}

					for(int num=0;num<20;num++){
						if((returnVal==2 && (int)bullets[i].getY()==(int)y+20+num) || (returnVal==1 && (int)bullets[i].getY()==(int)y-num)){
							if(bullets[i].getXSpeed()>0){
								returnVal=3;
							}
							else if(bullets[i].getXSpeed()<0){
								returnVal=4;
							}

						}

						else if((returnVal==3 && (int)bullets[i].getY()==(int)x-num) || (returnVal==4 && (int)bullets[i].getY()==(int)y+20+num)){
							if(bullets[i].getYSpeed()>0){
								returnVal=1;
							}
							else if(bullets[i].getYSpeed()<0){
								returnVal=2;
							}
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
		return "CPayne";
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
		String[] image={"mario_sprite.png"};
		return image;
	}

	@Override
	public void loadedImages(Image[] images) {
		// TODO Auto-generated method stub
		current=images[0];
	}

}
