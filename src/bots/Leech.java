package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Arrays;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;


public class Leech extends Bot {

	double targetPrevX;
	double targetPrevY;
	double x;
	double y;

	int FIRENOW;

	final double BOT_SPEED=BattleBotArena.BOT_SPEED;
	final double BULLET_SPEED=BattleBotArena.BULLET_SPEED;

	final double arenaRight=BattleBotArena.RIGHT_EDGE;
	final double arenaBottom=BattleBotArena.BOTTOM_EDGE;
	final double arenaLeft=BattleBotArena.LEFT_EDGE;
	final double arenaTop=BattleBotArena.TOP_EDGE;

	final double eventHorizon=(RADIUS/BOT_SPEED)*BULLET_SPEED;

	Image currentImage;
	Image[] images;

	final int animationTimer=5;
	int animationClock;

	final int shootTimer=5;
	int shootClock;

	int[] priority;

	public void newRound() {
		// TODO Auto-generated method stub

	}


	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {

		x=me.getX()+RADIUS;
		y=me.getY()+RADIUS;
		priority=new int[0];
		BotInfo target=liveBots[0];
		for (BotInfo i:liveBots) {
			if (  Math.abs(x-i.getX()+RADIUS)+Math.abs(y-i.getY()+RADIUS)<Math.abs(x-target.getX()+RADIUS)+Math.abs(y-target.getY()+RADIUS)  ) {
				target=i;
			}
		}
		double targetX=target.getX()+RADIUS;
		double targetY=target.getY()+RADIUS;
		double xDis=Math.abs(x-targetX);
		double yDis=Math.abs(y-targetY);

		boolean inPositionX=xDis<=1+RADIUS+RADIUS;
		boolean inPositionY=yDis<=RADIUS;

		if (FIRENOW!=0) {
			if (FIRENOW==1) {
				addPriority(BattleBotArena.FIRERIGHT);
			}
			else {
				addPriority(BattleBotArena.FIRELEFT);
			}
			FIRENOW=0;
		}
		else if (xDis<RADIUS+RADIUS) {
			if (x>targetX) {
				addPriority(BattleBotArena.RIGHT);
			}
			else {
				addPriority(BattleBotArena.LEFT);
			}
		}
		else if (!inPositionX||!inPositionY) {
			if (!inPositionX) {
				if (x<=targetX) {
					addPriority(BattleBotArena.RIGHT);
				}
				else {
					addPriority(BattleBotArena.LEFT);
				}
			}
			if (!inPositionY) {
				if (y<targetY) {
					addPriority(BattleBotArena.DOWN);
				}
				else {
					addPriority(BattleBotArena.UP);
				}
			}
		}
		else if (yDis<=RADIUS||(target.getLastMove()==BattleBotArena.DOWN&&y>targetY)||(target.getLastMove()==BattleBotArena.UP&&y<targetY)) {
			if (x<targetX) {
				//addPriority(BattleBotArena.FIRERIGHT);
				if (targetY<=y) {
					addPriority(BattleBotArena.DOWN);
				}
				else {
					addPriority(BattleBotArena.UP);
				}
				FIRENOW=1;
			}
			else {
				//addPriority(BattleBotArena.FIRELEFT);
				if (targetY<y) {
					addPriority(BattleBotArena.DOWN);
				}
				else {
					addPriority(BattleBotArena.UP);
				}
				FIRENOW=2;
			}
		}



		targetPrevX=target.getX()+RADIUS;
		targetPrevY=target.getY()+RADIUS;

		int move=getBestMove(bullets,deadBots);

		//System.out.println("LeechBot: "+yDis+" | "+targetY);

		setImage(move);

		return move;
	}

	private void setImage(int move) {
		if (animationClock==animationTimer) {
			switch (move) {
			case (BattleBotArena.DOWN) :
			case (BattleBotArena.FIREDOWN) :
				if(currentImage!=images[0]) {
					currentImage=images[0];
				}
				else {
					currentImage=images[1];
				}
			break;
			case (BattleBotArena.UP) :
			case (BattleBotArena.FIREUP) :
				if(currentImage!=images[2]) {
					currentImage=images[2];
				}
				else {
					currentImage=images[3];
				}
			break;
			case (BattleBotArena.LEFT) :
			case (BattleBotArena.FIRELEFT) :
				if(currentImage!=images[4]) {
					currentImage=images[4];
				}
				else {
					currentImage=images[5];
				}
			break;
			case (BattleBotArena.RIGHT) :
			case (BattleBotArena.FIRERIGHT) :
				if(currentImage!=images[6]) {
					currentImage=images[6];
				}
				else {
					currentImage=images[7];
				}
			break;
			}
			animationClock=0;
		}
		else {
			animationClock++;
		}

	}


	public void addPriority(int move) {
		priority=Arrays.copyOf(priority, priority.length+1);
		priority[priority.length-1]=move;
	}

	public int getBestMove(Bullet[] bullets,BotInfo[] deadBots) {
		BotInfo tombstone=null;
		//double tombDisX=0;
		//double tombDisY=0;
		//if (deadBots.length>0) {
		//	tombstone=deadBots[0];
		//	for (BotInfo i:deadBots) {
		//		if (  Math.abs(x-i.getX()+RADIUS)+Math.abs(y-i.getY()+RADIUS)<Math.abs(x-tombstone.getX()+RADIUS)+Math.abs(y-tombstone.getY()+RADIUS)  ) {
		//			tombstone=i;
		//			tombDisX=Math.abs(x-tombstone.getX()+RADIUS);
		//			tombDisY=Math.abs(y-tombstone.getY()+RADIUS);
		//		}
		//	}
		//}
		//System.out.println(tombDisX+" | "+tombDisY+" | "+tombstone);
		for (int i:priority) {
			boolean undesirable=false;
			switch (i) {
			case (BattleBotArena.DOWN) :
				if (y+BOT_SPEED>=arenaBottom-RADIUS-RADIUS) {
					undesirable=true;
				}
				//else if (tombstone!=null&&y+BOT_SPEED>=tombstone.getY()-RADIUS-RADIUS&&tombDisX<=RADIUS+RADIUS){
				//	undesirable=true;
				//	System.out.println("tombstone down");
				//}
				else {	
					for (Bullet p:bullets) {
						if ((p.getY()<y+RADIUS+RADIUS+BOT_SPEED&&p.getY()>y+BOT_SPEED)&&((p.getXSpeed()>0&&p.getX()<x)||(p.getXSpeed()<0&&p.getX()>x))&&(Math.abs(x-p.getX())<eventHorizon)) {
							undesirable=true;
							break;
						}
					}
				}
			break;
			case (BattleBotArena.UP) :
				if (y-BOT_SPEED<=arenaTop+RADIUS+RADIUS) {
					undesirable=true;
				}
				//else if (tombstone!=null&&y-BOT_SPEED<=tombstone.getY()+RADIUS+RADIUS&&tombDisX<=RADIUS+RADIUS){
				//	undesirable=true;
				//	System.out.println("tombstone up");
				//}
				else {
					for (Bullet p:bullets) {
						if ((p.getY()<y+RADIUS+RADIUS-BOT_SPEED&&p.getY()>y-BOT_SPEED)&&((p.getXSpeed()>0&&p.getX()<x)||(p.getXSpeed()<0&&p.getX()>x))&&(Math.abs(x-p.getX())<eventHorizon)) {
							undesirable=true;
							break;
						}
					}
				}
			break;
			case (BattleBotArena.LEFT) :
				if (x-BOT_SPEED<=arenaLeft+RADIUS+RADIUS) {
					undesirable=true;
				}
				//else if (tombstone!=null&&x-BOT_SPEED<=tombstone.getX()+RADIUS+RADIUS&&tombDisY<=RADIUS+RADIUS){
				//	undesirable=true;
				//	System.out.println("tombstone left");
				//}
				else {
					for (Bullet p:bullets) {
						if ((p.getX()<x+RADIUS+RADIUS-BOT_SPEED&&p.getX()>x-BOT_SPEED)&&((p.getYSpeed()>0&&p.getY()<y)||(p.getYSpeed()<0&&p.getY()>y))&&(Math.abs(y-p.getY())<eventHorizon)) {
							undesirable=true;
							break;
						}
					}
				}
			break;
			case (BattleBotArena.RIGHT) :
				if (x+BOT_SPEED>=arenaRight-RADIUS-RADIUS) {
					undesirable=true;
				}
				//else if (tombstone!=null&&x+BOT_SPEED>=tombstone.getX()-RADIUS-RADIUS&&tombDisY<=RADIUS+RADIUS) {
				//	undesirable=true;
				//	System.out.println("tombstone right");
				//}
				else {
					for (Bullet p:bullets) {
						if ((p.getX()<x+RADIUS+RADIUS+BOT_SPEED&&p.getX()>x+BOT_SPEED)&&((p.getYSpeed()>0&&p.getY()<y)||(p.getYSpeed()<0&&p.getY()>y))&&(Math.abs(y-p.getY())<eventHorizon)) {
							undesirable=true;
							break;
						}
					}
				}
			break;
			}
			if (!undesirable) {
				return i;
			}
		}
		return priority[0];
	}

	public void draw(Graphics g, int x, int y) {
		if (currentImage != null)
			g.drawImage(currentImage, x-2, y-2, 24, 24, null);
		else
		{
			g.setColor(Color.lightGray);
			g.fillOval(x, y, Bot.RADIUS*2, Bot.RADIUS*2);
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "LeechBot";
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
		return new String[] {"leech_down0.png", "leech_down1.png" ,"leech_up0.png", "leech_up1.png" ,"leech_left0.png", "leech_left1.png" ,"leech_right0.png", "leech_right1.png"};
	}

	@Override
	public void loadedImages(Image[] images) {
		this.images=images;
		currentImage=images[0];
	}

}
