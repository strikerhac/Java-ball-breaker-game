package end.semester.project;

//                        Rao Nouman Ahmad And Hassaan Akbar Cheema
//                        131937 & 174351
//                        BSCS-6C



import java.util.Random;// We are genrating 4 random numbers to pick smilies randomly
import javax.swing.*;//This class will provide us features of JFrame,JComponent,Timer
import java.awt.*;//We canuse Dimension,Image,Toolkit,
                  //Graphics,Container,Color,Graphics2D,Font,Rectangle,AlphaComposite
import java.awt.event.*;//Here we can handle actions by using ActionEvent,ActionListener,KeyAdapter,KeyEvent
import java.applet.*;	//Used to include AudioClip we can also implement icon plug and play but
                        // we avoided that method
import java.net.*;	//We are using URL for handling sound clips
                        //and are handling exceptions using MalformedURLException


class Main_GamePain extends JComponent implements ActionListener
{
	static int X_Coordinte,Y_Coordinte;	
	static int Array_Coordinates[][];//Array is used for information about coordinates of Smiles
	LinkList Tetris_Object; //Piling Smilies are checked here by using this object
	Timer Delay_Timer,Delay_Timer_1,Delay_Timer_2,Animation_Timer;
           //Timers are used here for controlling the speed and delays of smilies 
          //We put Delays so that we can avoid overlapping of smilies and we 
          //
	Image Picture_Smilies[]=new Image[4];//Array of objects of type images to store smiles refrence
	Image Front_Pipe_Section,Back_Pipe_Section;//Front and back side of pipe to show smilies coming
                           // entring from ground pipe and coming out from other side of pipe
	String Sound_File_Names[]={"intro.mid","enter.wav","sound735.wav","blip.wav","sound65.wav","sound136.wav","tada.wav","sound713.wav"};
	AudioClip Audioclip_Sound[]=new AudioClip[8];//Array of objects of type 
                       //audioclip to store url refence of diffrent sound clips for diffrent instances in game
	Toolkit Tools;	//This  is used to load images		
	Random Random_Generator;//Helps in genrating smilies of diffrent colour randomly.
	int Rotation;//Used For changing the orentation of smilies
	int Length_Puyos;//lenght of smilies ie. width and height
	boolean Touched_Bottom;//to check if out coming smiley has touched bottom or still is in movement
	int Temp;	//Count of smilies to to check if tetris formed to remove them
	boolean Game_Started;	//Game is started or not
	boolean Game_Over,Game_Paused; // to Store conditon of game pause and game over
	int a,b; //Predicted Smilies that will come next are shown on window on left side they will
                // be stored here
	int Difficulty_Level,Game_Total,Pieces_Smiliey,Removed_Pieces;
               //Total pieces is number of joint smilies  generated
              //Removes pieces is number of removed smilies by forming Tetris
	int Minimum_Total;	
	int Animation;	//to build the pixel by pixel Animationation movement of generated Smilie is
                        // controlled by using this
	float alpha,alpha1;
	boolean Difficulty_Levelflag;
        public Main_GamePain(){
        }
	public Main_GamePain(int l,int r,int c)
	{
		Length_Puyos=l;//Length of Smilies is set by the this
                               //class where it is calculated and sent here
		X_Coordinte=r+1;	//x is used by adding in 12 to make genration of smilies easy
		     //extra one row is occupied by pipe at the top.
                                    //Only 12 x Coordinte are used for Smilies.
		Y_Coordinte=c;	    //6 columns Of Y  coordinates
		Encapsulated_Variables(); // varibles are intialized here so that values can be reset each
                // time after use and so that thier value doesnot change all time
		//Smilies are selected depending on the lenght of smiles and which is calculated by
                // using screen size and resoultion of coordinates
		
		//I used  two smilies here small one and a bit bigger one in size fo foloowing resolutions
		//(i)800*600 and below resolutions
		//(ii)1024*768 and above resolutions
		Smilies();
		Backround_Music();		
		Push_Smilies();				//Smilies will start piling
		Audioclip_Sound[0].loop();      // Sound clip will be looped here
		addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
				{
//when enter is pressed in condition game is not started it will invoke delay timer
//if game is over all varabiles will be reset to thier initialized values 
//and will bbe prepare to start again
					if(!Game_Started)		
					{
						Audioclip_Sound[0].stop();
						Audioclip_Sound[1].play();
						Define_Delay();
						Delay_Timer.start();
						Game_Started=true;
					}
					if(Game_Over)		
					{
						Encapsulated_Variables();
						Push_Smilies();
						Audioclip_Sound[0].loop();
						Game_Started=false;
					}
					if(Game_Paused)
					{
						Encapsulated_Variables();
						Push_Smilies();
						Audioclip_Sound[0].loop();
						Game_Started=false;
					}
					repaint();
				}
				else
				if(e.getKeyCode()==KeyEvent.VK_LEFT && !Touched_Bottom && !Game_Paused)
// We move Smilies left if each Smiliey is not Touching the bottom
				{
					Audioclip_Sound[3].play();
					Left_Push();
				}
				else
				if(e.getKeyCode()==KeyEvent.VK_RIGHT && !Touched_Bottom && !Game_Paused)
				{
					Audioclip_Sound[3].play();
					Right_Push();
				}
				else
				if(e.getKeyCode()==KeyEvent.VK_UP && !Game_Paused )
				{
					Audioclip_Sound[3].play();
					if(!Touched_Bottom)
					Change_Orentation();
					if(!Game_Started && Difficulty_Level<19)
                                            //We can change level before startingthe game here 
						Difficulty_Level++;
				}
				else
				if(e.getKeyCode()==KeyEvent.VK_DOWN && !Game_Paused)
				{
					Audioclip_Sound[3].play();
					Push_Down();
					if(!Game_Started && Difficulty_Level>0) 
                                            //We can decrease difficulty here
					Difficulty_Level--;
				}
				else
				if(e.getKeyCode()==KeyEvent.VK_P && Game_Started && !Game_Over)
				{
	//Here we will pasue the game or we will resume the game if it is paused
					if(Game_Paused)
					{
						Audioclip_Sound[1].play();
						Game_Paused=false;
						alpha1=0.0f;
						Delay_Timer.start();	
                                                //Game is resumed here
					}
					else
					{
						Audioclip_Sound[2].play();
						Delay_Timer.stop();
						Game_Paused=true;		
                                                //Game will be paused here
					}
				}
				else
				if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
				{
					Audioclip_Sound[2].play();
	//if game is paused and we press EScape key we will exit the game and it will be 
        //close but if ame is not paused it will be paused here
					if( Game_Started && !Game_Over)
					{
						if(Game_Paused)
						System.exit(0);		
                                              //End the game and exit
						else
						{
							Delay_Timer.stop();
							Game_Paused=true;		
                                              //Game will be paused
						}
					}
					else
					System.exit(0);
				}
			}
		});
		setFocusable(true);	//Here we are settingthe focus of keyboard and 
                // events to Our canvas of game pane class
	}
	public void Encapsulated_Variables()
//Intializng all those encapsualated varaibles
	{
		Array_Coordinates=new int[X_Coordinte][Y_Coordinte];
		Rotation=1;
		Touched_Bottom=true;
		Temp=0;
		Game_Started=false;
		Game_Over=false;
		Game_Paused=false;
		a=0;
		b=0;
		Difficulty_Level=0;
		Game_Total=0;
		Pieces_Smiliey=-1;
		Removed_Pieces=0;
		Minimum_Total=50;
		Animation=0;
		alpha=0.0f;
		alpha1=0.0f;
		Difficulty_Levelflag=true;
		Tools= Toolkit.getDefaultToolkit();
		Random_Generator=new Random();
		Delay_Timer=new Timer(1000,this);
//Invokes action event for each 1075 milli seconds when delay timer is Started
		Delay_Timer.setInitialDelay(0);
//Invokes first event after 0 ms when Delay Timer starts
		Delay_Timer_1=new Timer(1000,this);
		Delay_Timer_2=new Timer(500,this);
		Animation_Timer=new Timer(50,this);
		Animation_Timer.start();			//starting the Delay Timer
	}
	public void Smilies()//Smilies will be loaded here in array 
                //and pipes will be  loaded here in objects
	{
		String s="";
		if(Length_Puyos>=42)
		s="_";
		for(int i =0;i<Picture_Smilies.length;i++)
		Picture_Smilies[i]=Tools.getImage("images\\puyo_"+s+(i+1)+".png");
		Front_Pipe_Section=Tools.getImage("images\\pipe"+s+"1.png");
		Back_Pipe_Section=Tools.getImage("images\\pipe"+s+".png");
	}
	public void Backround_Music()//loading music clips into the AudioClip array
	{
		try{
			for(int i=0;i<Audioclip_Sound.length;i++)	
                     //Loading all the sound music clips
			Audioclip_Sound[i]=Applet.newAudioClip(new URL("file:"+System.getProperty("user.dir")+"\\sounds\\"+Sound_File_Names[i]));
			
		}
		catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
    }
	public void Define_Delay()
	{
		int delay=0,delay1=0;
		for(int i=0;i<=Difficulty_Level;i++)
      //delays will be changed here depending on the difficulty level of game
		{
			delay+=20*(4-i/5);	
			delay1+=4-i/5;
		}
		if(Difficulty_Level==20)
		{
			delay+=25;
			delay1+=1;
		}
		Delay_Timer.setDelay(1075-delay);
		Animation_Timer.setDelay(52-delay1);
		Animation_Timer.restart();
	}
	public void Push_Smilies()//Smilies will appear at top of window and starting end of pipe
	{
		
		//Here we arechecking if top is filled or not if yes coming smiliey will be blocked 
                // and game will be over
		int p;
		if(Y_Coordinte%2==0)
		p=Y_Coordinte/2-1;
		else
		p=Y_Coordinte/2;
		if(Array_Coordinates[0][p]==0 && Array_Coordinates[1][p]==0)
		{
			Audioclip_Sound[4].play();
			Array_Coordinates[0][p]=a;
                        //A and b will be assigned randomly genrated smilies
			Array_Coordinates[1][p]=b;
		}
		else
		{
			Audioclip_Sound[2].play();
			Delay_Timer.stop();		
			Game_Over=true;		//game is over
			return;
		}
		int r;	// Next coming smilies are genrated here and can be seen on right hand side of
                // window they are loaded and are predicted before coming out of pipe
                  
		//Odd numbers 1,3,5,7(for 4 colors) are used for generating Smilies which are in movement
                
		//Even numbers 2,4,6,8 are used for smilies that are on bottom of the window
                
		while((r=Random_Generator.nextInt(8))%2==0);
		a=r;
		while((r=Random_Generator.nextInt(8))%2==0);
		b=r;
		Pieces_Smiliey++;
		Rotation=1;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==Delay_Timer)	//If Event is generated by the Delay_Timer object
		{
			Audioclip_Sound[5].play();
			Smilies_Motion();
		}
		else if(e.getSource()==Delay_Timer_1)
         //If Event is generated by the Delay_Timer_1 object
		{
			Audioclip_Sound[6].play();
			erase_Smilieys();
		}
		else if(e.getSource()==Delay_Timer_2)
		{
			Audioclip_Sound[7].play();
			Pile_Empty_Space();			
			Delay_Timer_2.stop();
		}
                  //Delay_Timer_1 and Delay_Timer_2 are used for make delay
                    //b/w erasing smilies and filling empty spaces
		repaint();
	}
	public void Smilies_Motion()//Pushing Smilies Downword
	{
		int flag=0;
		for(int i=X_Coordinte-1;i>=0;i--)
		for(int j=0;j<Y_Coordinte;j++)
		if(Array_Coordinates[i][j]%2==1)
		{
			if(i==X_Coordinte-1)	//cheching every pushing smiley ie. on last row 12
			{
				Array_Coordinates[i][j]+=1;
              //instanceon whuch  smiley Touches Bottom the ground ie. 12 row
				Touched_Bottom=true;		
               //feel as it has completed the falling method
									//Touched Bottom
			}
			else if(Array_Coordinates[i+1][j]==0)		//if the next row is empty
			{
				Array_Coordinates[i+1][j]=Array_Coordinates[i][j];	
              //Every smiliey in moment increase the row number 
				Array_Coordinates[i][j]=0;
				flag=1;			//to build the movement for that smiley
			}
			else
			{
				Array_Coordinates[i][j]+=1;		
              //If there is already a smiley in next row stop pushinh down
				Touched_Bottom=true;		
                      //It will be done by making it even
			}
			Animation=0;			
          //for pixel by pixel Animationation of Smilieys here Animationation starts
		}
		if(flag==0)			
       //if flag is not set mean that there is no smiley is in moment
		erase_Smilieys();		
                 //so remove smiley which form Tetris
	}
	public void erase_Smilieys()
	{
		int flag=0;
		for(int i=0;i<X_Coordinte;i++)
		for(int j=0;j<Y_Coordinte;j++)
		if(Array_Coordinates[i][j]>0)			
                       //for all color smilies
		{
			Temp=1;
			Tetris_Object=new LinkList(i,j);
                          // Linklist for every Smiliey
			Posibilty_4_Consectuive(i,j);
                         //check that LinkLIst attached to the Tetris Object smilies of same color
			if(Temp>=4)		//if Tetris  forms 
			{
				Clear_Smilies();//Deleting  Smilies which form Tetris 
				flag=1;
		
			}
		}
		if(flag==1)
		{
			Delay_Timer.stop();	  //stop the generating Smilies
                               //by invoking action listner with help of timer delay
			Delay_Timer_1.start();	 //erase smilies if there is any other form Tetris with delay
			Delay_Timer_2.start();	 
//fill empty with remaining by the law of gravity with delay
			return;
		}	
		Delay_Timer_1.stop();			
//If there are no more smilies that are forming tetris than stop timer delay forrepushing smilies
		Minimum_Total=50;			
//minimum game score is in encapsulated variables
		Push_Smilies();		
//start generating Smilies again
		if(!Delay_Timer.isRunning())
		Delay_Timer.start();
	}
	public void Posibilty_4_Consectuive(int x,int y)
	{
		if(y<Y_Coordinte-1 && Array_Coordinates[x][y]==Array_Coordinates[x][y+1] && !existsInTetris(x,y+1))
		{							
//Check of same colour smilies on right side
			Temp++;				
//Check for smiley that is currently bot added to tetris
			Pile_Smilies(x,y+1);		
//If there is then add that to the current Tetri
			Posibilty_4_Consectuive(x,y+1);	
//Check for LinkList that can connected to any same color Smilies
		}
		if(x<X_Coordinte-1 && Array_Coordinates[x][y]==Array_Coordinates[x+1][y] && !existsInTetris(x+1,y))
		{
			Temp++;
			Pile_Smilies(x+1,y);		
                     //check at the down side
			Posibilty_4_Consectuive(x+1,y);
		}
		if(y>0 && Array_Coordinates[x][y]==Array_Coordinates[x][y-1] && !existsInTetris(x,y-1))
		{
			Temp++;
			Pile_Smilies(x,y-1);		
                         //check at the left side
			Posibilty_4_Consectuive(x,y-1);
		}
		
		if(x>0 && Array_Coordinates[x][y]==Array_Coordinates[x-1][y] && !existsInTetris(x-1,y))
		{
			Temp++;
			Pile_Smilies(x-1,y);		
                               //check at the up side
			Posibilty_4_Consectuive(x-1,y);
		}
	}
	public void Pile_Smilies(int x,int y)
              //adding another Linklist to the present Tetris
	{
		Tetris_Object.setNext(new LinkList(x,y));
		Tetris_Object.getNext().setPrev(Tetris_Object);
          //It is totally the linked list concept used here
		Tetris_Object=Tetris_Object.getNext();
	}
	public boolean existsInTetris(int x,int y)//comparing with the all the nodes in present Tetris_Object 
	{										//that it is already exists or not
		LinkList n=Tetris_Object;
		while(n!=null)
		{
			if(n.Return_X_Coordinate()==x && n.Return_Y_Coordinate()==y)
			return true;
			n=n.getPrev();
		}
		return false;
	}
	public void Clear_Smilies()
	{
		LinkList n=Tetris_Object;
		while(n!=null)
		{
			Array_Coordinates[n.Return_X_Coordinate()][n.Return_Y_Coordinate()]=0;	//repushing Smilieys which are in Tetris_Object
			n=n.getPrev();				
		}
		Removed_Pieces+=Temp;
		if(Removed_Pieces>=50);//Change the Difficulty Level of the game depending on
		{				//number of removed Smilieys.
			if(Difficulty_Levelflag)
			Difficulty_Level+=1;
			else
			Difficulty_Level-=1;				//Ofter playing the final Difficulty_Level(here 20) the Difficulty_Level decreased by 1
			if(Difficulty_Level==20)			//It is decreased up to 15 and then it increases 20
			Difficulty_Levelflag=false;		//So The Game is endlessly continued if player can play all Difficulty_Levels perfectly without any minstake at any Difficulty_Level.
			if(Difficulty_Level==15 && !Difficulty_Levelflag)
			Difficulty_Levelflag=true;
			Define_Delay();
			Removed_Pieces=0;
		}	
		Game_Total+=Minimum_Total*(Temp-3)*Temp;//Score is calculated by using this formula
		Minimum_Total=Minimum_Total*Temp;
//Minimum Total in formula depends on the number of smiles formed in last Tetris
	//Scoring totally depends on the length of chain  
 //number of chains formed by current chain in tetris at a single time
	}
	public void Pile_Empty_Space()
                          //vacated places formed by removed 
                //Smilies are filled with the other smilies by the law of gravity
	{
		for(int i=X_Coordinte-2;i>=0;i--)
		for(int j=0;j<Y_Coordinte;j++)
		if(Array_Coordinates[i][j]>0)	//for all Smilies
		{
			int k;
			for(k=i+1;k<=X_Coordinte-1;k++)
			if(Array_Coordinates[k][j]>0)			
                 //Smilies that exist below current smiley untill the level of ground 
			{
				Array_Coordinates[k-1][j]=Array_Coordinates[i][j];
                                //then continue to move  it
				if(i!=k-1)
				Array_Coordinates[i][j]=0;
				break;
			}
			else if(k==X_Coordinte-1)		
                               //Condition in which Smilies that exist below
          //current smiley untill the level of ground 
			{
				Array_Coordinates[X_Coordinte-1][j]=Array_Coordinates[i][j];
            //then continue to move  it
				if(i!=X_Coordinte-1)
				Array_Coordinates[i][j]=0;
			}
		}
	}
	public void Left_Push()	
                   //to move the coming down smilies one step left 
	{					
		for(int i=0;i<X_Coordinte;i++)
		for(int j=0;j<Y_Coordinte;j++)
		if(Array_Coordinates[i][j]>0 && Array_Coordinates[i][j]%2==1 && j>0 )
           //for all the coming down Smilies which are not in first column or left column
		{
			
			if(j<Y_Coordinte-1 && Array_Coordinates[i][j+1]%2==1 && Array_Coordinates[i][j-1]==0)	//if two Smiliey are in horizontal
			{													//if there is no Smiliey in left
					Array_Coordinates[i][j-1]=Array_Coordinates[i][j];		
         //move two smiles to one step left
					Array_Coordinates[i][j]=Array_Coordinates[i][j+1];
					Array_Coordinates[i][j+1]=0;
			}
			else
			if(Array_Coordinates[i][j-1]==0 && Array_Coordinates[i+1][j-1]==0 )
//if two Smiles are in vertical i.e y coordinates are same & there is no smilies in left
			{
				Array_Coordinates[i][j-1]=Array_Coordinates[i][j];
				Array_Coordinates[i+1][j-1]=Array_Coordinates[i+1][j];
                                //move two smilies to one step left
				Array_Coordinates[i][j]=0;
				Array_Coordinates[i+1][j]=0;
			}
			return;
		}
	}
	public void Right_Push()	
             //to move the coming down smilies that are in motion one step right
	{
		for(int i=0;i<X_Coordinte;i++)
		for(int j=Y_Coordinte-1;j>=0;j--)
		if(Array_Coordinates[i][j]>0 && Array_Coordinates[i][j]%2==1 && j<Y_Coordinte-1)
//every down pushing smilies which are not in last column or right column
		{
			if(j>0 && Array_Coordinates[i][j-1]%2==1 && Array_Coordinates[i][j+1]==0)
//if two smilies are in horizontal i.e rows
			{											//if there is no Smiliey in right
					Array_Coordinates[i][j+1]=Array_Coordinates[i][j];
					Array_Coordinates[i][j]=Array_Coordinates[i][j-1];				//move two Smiliey to one step right
					Array_Coordinates[i][j-1]=0;
			}
			else
			if(Array_Coordinates[i][j+1]==0 && Array_Coordinates[i+1][j+1]==0 )
//if two smilies  are in vertical i.e same x coorrdinate & there is no smiley in right
			{
				Array_Coordinates[i][j+1]=Array_Coordinates[i][j];
				Array_Coordinates[i+1][j+1]=Array_Coordinates[i+1][j];			//move two Smiliey to one step right
				Array_Coordinates[i][j]=0;
				Array_Coordinates[i+1][j]=0;
			}
			return;			
		}
	}
	public void Change_Orentation()	
//to Change Orentation the coming down smilies in by 90 degrees
	{
		for(int i=0;i<X_Coordinte;i++)
		for(int j=0;j<Y_Coordinte;j++)
		if(Array_Coordinates[i][j]>0 && Array_Coordinates[i][j]%2==1)
                                  //for all the coming down Smilieys 
		{
	//alway consider the smilies left and top 
			//inspection is from up to down
			
 //Two smilies can make only four different positions Rotationating 90 degrees each time
			if(Rotation==1)	//first postion=vertical
			{
//pushing down smiliey to the left of first smilies makes 90 degrees Rotationaion to get second postion
		if(j>0 && Array_Coordinates[i][j-1]==0)//is left coordinate is empty or not
				{
					Array_Coordinates[i][j-1]=Array_Coordinates[i+1][j];
					Array_Coordinates[i+1][j]=0;		
                    //If it is empty coordinate move there
					Rotation=2;				
                                //change to second orientation
				}
				else if(j<Y_Coordinte-1 && Array_Coordinates[i][j+1]==0)
   //if left side is not empty, go for right orientation
				{
					Array_Coordinates[i][j+1]=Array_Coordinates[i][j];
            //push present smiliey to the right
					Array_Coordinates[i][j]=Array_Coordinates[i+1][j];
                //push down smiley to the current coordinate
					Array_Coordinates[i+1][j]=0;
					Rotation=2;				
                           //change to second orientation
				}
			}//second orientation =horizontal i.e x coordinate
			else if(Rotation==2 && i>1)
      //pushing current smiliey to the up of right smilies
                            //makes 90 degrees Rotationaion to get third orientation
			{
				Array_Coordinates[i-1][j+1]=Array_Coordinates[i][j];
      //push current smiliey to the up of the right smiley
				Array_Coordinates[i][j]=0;
				Rotation=3;					
                    //change to third orientation
			}
 //third orientation= Vertical invert to the first postion in two smilies orientation
			else if(Rotation==3)			
//pushing current smiliey to the up of right smiley
                            //makes 90 degrees Rotationaion to get fourth orientation
			{
				if(j<Y_Coordinte-1 && Array_Coordinates[i+1][j+1]==0)
//is right side of the down Smiley is empty or not
				{
					Array_Coordinates[i+1][j+1]=Array_Coordinates[i][j];	
//If it is empty space push there
					Array_Coordinates[i][j]=0;				
					Rotation=4;						
//change to fourth orientation
				}
				else if(j>0 && Array_Coordinates[i+1][j-1]==0)
//if left side of the down smiley is empty or not
				{
					Array_Coordinates[i+1][j-1]=Array_Coordinates[i+1][j];
           //push smiley down to the left
					Array_Coordinates[i+1][j]=Array_Coordinates[i][j];	
                                        //push current smiley down
					Array_Coordinates[i][j]=0;
					Rotation=4;				
                                        //change to fourth orientation
				}
			}
//fourth orientation= Horizontal flip to the second orientation in two Smilieys orientation
			else if(Rotation==4 && i<X_Coordinte-1)
//pushing right Smiliey to the down of the current
                            //Smiliey makes 90 degrees Rotationaion to get first orientation
			{
				if(Array_Coordinates[i+1][j]==0)
//if down orientation is empty or not
				{
					Array_Coordinates[i+1][j]=Array_Coordinates[i][j+1];
//push right Smiliey to the down of the current Smiliey
					Array_Coordinates[i][j+1]=0;
					Rotation=1;					
//change to first orientation
				}
			}
			return;
		}
	}
	public void Push_Down()//Pushing Smilieys one step down
	{
		for(int i=X_Coordinte-1;i>=0;i--)
		for(int j=0;j<Y_Coordinte;j++)
		if(Array_Coordinates[i][j]%2==1)//For all pushing Smilieys
		{
			if(i==X_Coordinte-1)	//if Smiliey is in last row
			{
				Array_Coordinates[i][j]=Array_Coordinates[i][j]+1;
               //declaring it as grounded
				Touched_Bottom=true;
			}
			else if(Array_Coordinates[i+1][j]>0 && Array_Coordinates[i+1][j]%2==0)
//if next row of Smiliey contains another Smiliey
			{
				Array_Coordinates[i][j]=Array_Coordinates[i][j]+1;
//then stop the current Smiliey at the current postion
				Touched_Bottom=true;
			}
			else 
			{
				Array_Coordinates[i+1][j]=Array_Coordinates[i][j];
//Push current Smiliey one step down
				Array_Coordinates[i][j]=0;
			}
		}
		repaint();
	}
        @Override
        //Here i used and checked for diffrent forums and videos specially
       // Specially oracle forum for java consulted most for complete understanding of methods
	public void paint(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0,0,Length_Puyos*Y_Coordinte,Length_Puyos*X_Coordinte);
//background fill with white color
		g.setColor(Color.black);
		g.fillRect(Length_Puyos*Y_Coordinte,0,Length_Puyos*3,Length_Puyos*X_Coordinte);
//Score board is filled with black color
		Graphics2D g2=(Graphics2D)g;
       // Actully type casting Here to use full features of Graphics 2D
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON );
	//designing Total Score board wi.th gradient colors as borders
		g2.setPaint(new GradientPaint(Y_Coordinte*Length_Puyos,0,new Color(50,50,50),Y_Coordinte*Length_Puyos+Length_Puyos/2,0,new Color(200,200,200),false));
		g2.fill(new Rectangle(Y_Coordinte*Length_Puyos,0,Length_Puyos/2,X_Coordinte*Length_Puyos));
		g2.setPaint(new GradientPaint((Y_Coordinte+2)*Length_Puyos+Length_Puyos/2,0,new Color(200,200,200),(Y_Coordinte+3)*Length_Puyos,0,new Color(50,50,50),false));
		g2.fill(new Rectangle((Y_Coordinte+2)*Length_Puyos+Length_Puyos/2,0,Length_Puyos/2,X_Coordinte*Length_Puyos));
		g2.setPaint(Color.white);
		
		g.fill3DRect((Y_Coordinte+3/2)*Length_Puyos,Length_Puyos,Length_Puyos,Length_Puyos*2,true);
	//Coming smiley is presented here
		g.drawImage(Picture_Smilies[a/2],(Y_Coordinte+3/2)*Length_Puyos,Length_Puyos,Length_Puyos,Length_Puyos,null);
		g.drawImage(Picture_Smilies[b/2],(Y_Coordinte+3/2)*Length_Puyos,Length_Puyos*2,Length_Puyos,Length_Puyos,null);
	
	//Difficulty and Number of pices and Total Score
		g.fill3DRect((Y_Coordinte)*Length_Puyos+Length_Puyos/2,(X_Coordinte+1)*Length_Puyos/2-5,Length_Puyos*2,Length_Puyos-2,true);
		g.fill3DRect((Y_Coordinte)*Length_Puyos+Length_Puyos/2,(X_Coordinte+1)*Length_Puyos/2+Length_Puyos-5,Length_Puyos*2,Length_Puyos-2,true);
		g.fill3DRect((Y_Coordinte)*Length_Puyos+Length_Puyos/2,(X_Coordinte+1)*Length_Puyos/2+2*Length_Puyos-5,Length_Puyos*2,Length_Puyos,true);
		g2.setPaint(Color.black);
	//Font for the text to be display I like Century mot so i used it here
		g2.setFont(new Font("Century",Font.PLAIN,Length_Puyos/4));
		g2.drawString("Level: "+Difficulty_Level,(Y_Coordinte)*Length_Puyos+Length_Puyos/2,X_Coordinte*Length_Puyos/2+Length_Puyos);
		g2.drawString("Pieces: "+Pieces_Smiliey,(Y_Coordinte)*Length_Puyos+Length_Puyos/2,X_Coordinte*Length_Puyos/2+2*Length_Puyos);
		g2.drawString("Score:"+Game_Total,(Y_Coordinte)*Length_Puyos+Length_Puyos/2,X_Coordinte*Length_Puyos/2+3*Length_Puyos);
	//back part of pipe that is entry part of smiley not exit part will be drawn here
		int p;
		if(Y_Coordinte%2==0)
		p=Y_Coordinte/2;
		else
		p=Y_Coordinte/2+1;
		g.drawImage(Back_Pipe_Section,p*Length_Puyos-Length_Puyos,0,Length_Puyos,Length_Puyos,null);
		for(int i=0;i<X_Coordinte;i++)
		for(int j=0;j<Y_Coordinte;j++)
		if(Array_Coordinates[i][j]>0)
		{
			int k=Array_Coordinates[i][j];
			if(k%2==0)
//if Smiliey value is even then simply display it
			{
				k=(int)k/2;
				g.drawImage(Picture_Smilies[k-1],j*Length_Puyos,i*Length_Puyos,Length_Puyos,Length_Puyos,null);
			}
			else
			{		
//if Smiliey value is odd then add Animationation to it
				k=(int)k/2+1;
				g.drawImage(Picture_Smilies[k-1],j*Length_Puyos,(i-1)*Length_Puyos+Animation,Length_Puyos,Length_Puyos,null);
				Animation+=2;
				if(Animation>=Length_Puyos)
				Animation=Length_Puyos;			
				if(Touched_Bottom && i==2)
				Touched_Bottom=false;
			}
			
		}
//front part of pipe where smiles exit and enter in canvas  is drawn as image here
		g.drawImage(Front_Pipe_Section,p*Length_Puyos-Length_Puyos,0,Length_Puyos,Length_Puyos,null);
		
		if(!Game_Started)//if game is not Started show this message
		{
			g2.setPaint(Color.pink);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.8f));
			g2.fill(new Rectangle(0,X_Coordinte*Length_Puyos/4,(Y_Coordinte+3)*Length_Puyos,(X_Coordinte+1)*Length_Puyos/2));
			g2.setPaint(Color.blue);
			g2.setFont(new Font("Century",Font.PLAIN,Length_Puyos/2));
			g2.drawString("  Difficulty: "+Difficulty_Level,Length_Puyos*3,X_Coordinte*Length_Puyos/3);
			g2.setFont(new Font("Century",Font.PLAIN,Length_Puyos/3));
			g2.drawString("Use the <up> and <down> Arrow keys for Difficulty",Length_Puyos/4,(X_Coordinte+2)*Length_Puyos/3); 
			g2.setFont(new Font("Century",Font.PLAIN,Length_Puyos/2));
			g2.drawString(" Press <Enter> to start the Game",Length_Puyos,X_Coordinte*Length_Puyos/2);
			g2.setFont(new Font("Century",Font.PLAIN,Length_Puyos/3));
			g2.drawString("  Use the left,right and down arrow keys to move the Smilies",0,(X_Coordinte+1)*Length_Puyos/2); 
			g2.drawString("  Up arrow key Changes Orentations of Smilies.",Length_Puyos,(X_Coordinte+2)*Length_Puyos/2);
			g2.drawString("  When 4 or more Smilies of the same colour are piled",Length_Puyos/3,(X_Coordinte+3)*Length_Puyos/2);
			g2.drawString("                       they disappear. ",Length_Puyos,(X_Coordinte+4)*Length_Puyos/2);
			g2.drawString("              Press <p> to pause the Game",Length_Puyos,(X_Coordinte+5)*Length_Puyos/2);
			g2.drawString("            Press <Escape> to exit the Game",Length_Puyos,(X_Coordinte+6)*Length_Puyos/2);
		}
		if(Game_Over)
//if game is over dim the game by using simple alpha compsit method and show message
		{
			g2.setPaint(Color.white);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			if(alpha<0.9f)
			alpha=alpha+0.02f;
//Draw Animationation of alpha compsition
			g2.fill(new Rectangle(0,0,Length_Puyos*Y_Coordinte,Length_Puyos*X_Coordinte));
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
			g2.setPaint(Color.red);
			g2.setFont(new Font("Century",Font.PLAIN,Length_Puyos/2));
			g2.drawString("Game Over",Length_Puyos*3/2,X_Coordinte*Length_Puyos/2);
			g2.setPaint(Color.blue);
			g2.setFont(new Font("Century",Font.PLAIN,Length_Puyos/3));
			g2.drawString("Press <Enter> to restart the Game",Length_Puyos/2,(X_Coordinte+1)*Length_Puyos/2);
		}
		if(Game_Paused)
//if game is paused dim the game by using using simple alpha compsit method and show messag
		{
			g2.setPaint(Color.white);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha1));
			if(alpha1<0.9f)
			alpha1=alpha1+0.02f;
			g2.fill(new Rectangle(0,0,Length_Puyos*Y_Coordinte,Length_Puyos*X_Coordinte));
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
			g2.setPaint(Color.blue);
			g2.setFont(new Font("Century",Font.PLAIN,Length_Puyos/2));
			g2.drawString("Game Paused",Length_Puyos*3/2,X_Coordinte*Length_Puyos/2);
			g2.setFont(new Font("Italic",Font.PLAIN,Length_Puyos/3));
			g2.drawString("   Press <p> to resume the Game",Length_Puyos/2,(X_Coordinte+1)*Length_Puyos/2);
			g2.drawString("  Press <Escape> to exit the Game",Length_Puyos/2,(X_Coordinte+2)*Length_Puyos/2);
			g2.drawString(" Press <Enter> to restart the Game",Length_Puyos/2,(X_Coordinte+3)*Length_Puyos/2);
		}
	}
}