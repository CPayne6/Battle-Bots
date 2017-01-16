package bots;

import java.awt.Graphics;
import java.awt.Image;

import arena.BotInfo;
import arena.Bullet;

/**
 * This is a battlebot called Eminem
 * He has a bowl of spagetti as his picture because it is a funny joke
 * Eminem is designed to hunt down other bots, shoot them and kill them 
 * all while dodging bullets in order to not die
 * 
 * Last edited January the 30th
 * 
 * @author Chris
 *
 */
public class Eminem extends Bot{

	Image botImage;// Eminem's image
	private double x,y = 0;// the x and y values for the bot
	private double lastX,lastY;// the position that the bot was in before this iteration

	private boolean overheated=false;// tells if the bot is overheated
	private int lastMove=0;// the move that the bot made last iteration 

	int shotCounter=10;// a timer to make sure that Eminem does not shoot too fast

	@Override
	public void newRound() {

	}

	/**
	 * makes Eminem move and shoot
	 * 
	 * @return 1, 2, 3, 4, 5, 6, 7, or 8
	 */
	@Override
	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
		lastX=x; // set the last position of Eminem
		lastY=y;

		overheated = me.isOverheated();// check if the bot is overheated

		x=me.getX()+10;// save Eminem's position
		y=me.getY()+10;// 10 is added to center Eminem's position

		int returnVal = 0;

		// the methods of action (moveBot, dodgeTombstone, shootBullet and dodgeBullet) are organized in order of importance from least important to most important

		returnVal = moveBot(findClosestBot(liveBots,me.getBotNumber()));// move towards the closest bot

		if(deadBots.length>0)// if there are dead bots in the world
			returnVal = dodgeTombstone(findClosestBot(deadBots,me.getBotNumber()), returnVal);// move around a tombstone

		if(shotCounter>10 && !overheated)// if Eminem has not shot recently
			returnVal = shootBullet(liveBots, returnVal);
		else
			shotCounter++;

		returnVal = dodgeBullet(bullets, returnVal); // dodge a threatening bullet

		lastMove=returnVal; // save the move for future reference

		System.out.println("     ");

		return returnVal;
	}

	/**
	 * finds the closest bot to Eminem
	 * can be used with dead bots or live bots
	 * 
	 * @param bots
	 * @param myNumber
	 * @return	closest bot
	 */
	public BotInfo findClosestBot(BotInfo[] bots, int myNumber){
		double distance=10000;
		double bX;
		double bY;
		BotInfo closestBot=null;
		for(int i=0;i<bots.length;i++){// cycles through the given bot array
			bX=bots[i].getX()+10;//saves the x and y of the bot
			bY=bots[i].getY()+10;
			if(myNumber != bots[i].getBotNumber()){ // if the bot is not Eminem
				if(Math.sqrt((bX-x)*(bX-x)+(bY-y)*(bY-y))<distance){// if the bot is closer than the prior closest
					distance=Math.sqrt((bX-x)*(bX-x)+(bY-y)*(bY-y));// make it the new closest 
					closestBot=bots[i];

				}
			}
		}
		return closestBot;
	}

	/**
	 * moves the bot around the map based on where the other bots are located
	 * 
	 * @param bot
	 * @return 1, 2, 3 or 4
	 */
	public int moveBot(BotInfo bot){
		// adding 10 to the x and y coordinates centers the position of the bot
		double bY = bot.getY()+10;// sets the x and y of the enemy bot to bX and bY for readability 
		double bX = bot.getX()+10;
		double xDistance=Math.abs(bX-x);// sets the distances on the x and y axis for readability
		double yDistance=Math.abs(bY-y);
		double distance=Math.sqrt(xDistance*xDistance + yDistance*yDistance);// calculates the distance of the enemy bot to Eminem
		int val=0;// declares the value that is returned

		if(yDistance<9 && xDistance < 60 && !bot.isOverheated()){// if Eminem is too close to the enemy on the y axis
			if(y>=bY){// move away from the bot vertically 
				val=2;
			}
			else{
				val=1;
			}
		}
		else if(xDistance<9 && yDistance < 60 && !bot.isOverheated()){// if Eminem is too close to the enemy on the x axis
			if(x>=bX){// move away from the bot horizontally 
				val=4;
			}
			else{
				val=3;
			}
		}
		else if(distance>=84){// if the bot is far away
			if(xDistance>yDistance){// move towards the bot
				if(x>bX){
					val=3;
				}
				else{
					val=4;
				}
			}
			else{
				if(y>bY){
					val=1;
				}
				else{
					val=2;
				}
			}
		}
		else if(yDistance>=60 && xDistance<60 && (xDistance>9 || (xDistance>8 && bot.isOverheated())) && !overheated){//makes the bot move closer in the x direction but not too close
			if(x>bX){
				val=3;
			}
			else{
				val=4;
			}
		}
		else if(xDistance>=60 && yDistance<60 && (yDistance>9 || (yDistance>8 && bot.isOverheated())) && !overheated){// makes the bot move closer in the y direction but not too close
			if(y>bY){
				val=1;
			}
			else{
				val=2;
			}
		}
		else if(xDistance<8){// if eminem is too close to the enemy in the x axis
			if(x>=bX){//move away from the bot horizontally
				val=4;
			}
			else{
				val=3;
			}
		}
		else if(yDistance<8){// if eminem is too close to the enemy in the y axis
			if(y>=bY){//move away from the bot vertically
				val=2;
			}
			else{
				val=1;
			}
		}
		else if(xDistance>=9 && yDistance>=9){
			if(xDistance<yDistance){
				if(x>bX){
					val=3;
				}
				else{
					val=4;
				}
			}
			else{
				if(y>bY){
					val=1;
				}
				else{
					val=2;
				}
			}
		}




		return val;
	}

	/**
	 * enables the bot to move around tombstones
	 * 
	 * @param bot
	 * @param val
	 * @return val, 1, 2, 3 or 4
	 */
	public int dodgeTombstone(BotInfo bot, int val){

		if(lastX==x && (lastMove==3 || lastMove==4)){// if the bot tried to move in the x direction and did not move
			if((int)(Math.random()*2)==0){
				val=2;//move down
			}
			else{
				val=1;// move up
			}
		}
		else if(lastY==y && (lastMove==1 || lastMove==2)){// if the bot tried to move in the y direction and did not move
			if((int)(Math.random()*2)==0){
				val=4;//move right
			}
			else{
				val=3;//move left
			}
		}

		return val;
	}

	/**
	 * 
	 * @param bots
	 * @param val
	 * @return val, 5, 6, 7, or 8
	 */
	public int shootBullet(BotInfo[] bots, int val){

		for(int i=0; i<bots.length; i++){// this loop cycles through the array of bots
			// this also centers the position of the bot
			double bX=bots[i].getX()+10;// sets a variable for x and y to make code easier to read
			double bY=bots[i].getY()+10;// bX and bY are the x and y coordinates of the targeted bot

			// if the bot is within 150 pixels in the x and y, or if the bot is a sentry
			if(((Math.abs(bX-x)<150 && Math.abs(bY-y)<150))){
				for(int num=0;num<10;num++){// loops 9 times, num is the variable that is added and subtracted from bX and bY
					if((int)bY+num==(int)y || (int)bY-num==(int)y){//if the bot can be hit by a bullet in the x axis
						if(bX>x){// if the enemy bot is to the right
							val=8;// shoot right
						}
						else{
							val=7;// shoot left
						}
						shotCounter=0; //reset the shot counter
					}
					else if((int)bX+num==(int)x || (int)bX-num==(int)x){// if the bot can be hit by a bullet in the y axis
						if(bY>y){// if the enemy bot is below Eminem
							val=6;//shoot down
						}
						else{
							val=5;// shoot up
						}
						shotCounter=0;// reset the shot counter
					}

				}
			}
		}
		return val;
	}

	/**
	 * This method makes Eminem dodge bullets when necessary
	 * @param bullets array
	 * @param val
	 * @return val, 1, 2, 3 or 4
	 */
	public int dodgeBullet(Bullet[] bullets, int val){
		// runs through the bullets array
		for(int i=0; i<bullets.length; i++){
			double bY = bullets[i].getY();// saves the x and y of the bullet
			double bX = bullets[i].getX();
			if(Math.abs(bX-x)<=50 && Math.abs(bY-y)<=50){// if the bullet is close enough to be a threat
				for(double num = 0 ; num < 11.5 ; num+=0.5){
					if((bullets[i].getYSpeed() > 0 && bY < y) || (bullets[i].getYSpeed() < 0 && bY > y)){// if the bullet is coming towards Eminem
						if(bX==x+num || (bX>=660 && x>=650)){// if the bullet is going to hit Eminem on his right side or he is along a wall
							System.out.println("dodging left");
							val=3;
						}
						if(bX==x-num || (bX<=20 && x<=30)){// if the bullet is going to hit Eminem on his left side or he is along a wall
							System.out.println("dodging right");
							val=4;
						}
					}
					else if((bullets[i].getXSpeed() > 0 && bX < x) || (bullets[i].getXSpeed() < 0 && bX > x)){// if the bullet is coming towards Eminem
						if(bY==y+num || (bY>=460 && y>=450)){// if the bullet is going to hit the bottom of Eminem or he is along a wall
							val=1;
							System.out.println("dodging up");
						}
						if(bY==y-num || (bY<=30 && y<=40)){// if the bullet is going to hit the top of Eminem or he is along a wall
							val=2;
							System.out.println("dodging down");
						}
					}
				}
			}
		}

		return val;
	}





	/**
	 * draws the image of the bot
	 */
	@Override
	public void draw(Graphics g, int x, int y) {
		g.drawImage(botImage, x, y, null);

	}

	/**
	 * This method lets all the other inferior bots know his name
	 * 
	 * @return the bot's name
	 */
	@Override
	public String getName() {
		return "Eminem";
	}

	/**
	 * Eminem works alone
	 */
	@Override
	public String getTeamName() {

		return null;
	}

	/**
	 * Eminem does not waste moves to send a message
	 */
	@Override
	public String outgoingMessage() {

		return null;
	}

	/**
	 * redundant because Eminem is inclusive and does not want to harm the opponent's self esteem
	 */
	@Override
	public void incomingMessage(int botNum, String msg) {


	}

	/**
	 * adds the image to the array of images
	 */
	@Override
	public String[] imageNames() {
		String[] image =  {"moms_spagetti.png"};
		return image;
	}

	/**
	 * sets the bot's image
	 */
	@Override
	public void loadedImages(Image[] images) {
		botImage=images[0];
	}

}
