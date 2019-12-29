package end.semester.project;


//                        Rao Nouman Ahmad And Hassaan Akbar Cheema
//                        131937 & 174351
//                        BSCS-6C



import javax.swing.*;//This class will provide us features of JFrame,JComponent,Timer
import java.awt.*;//We canuse Dimension,Image,Toolkit,
                  //Graphics,Container,Color,Graphics2D,Font,Rectangle,AlphaComposite

public class Tetris_Puyo extends JFrame
{
	Main_GamePain Main_Object;		
        //GamePane is ourown defiend subclass of JComponent on which smilies will be pushed		
	int X_length,Y_length,X_Coordinate,Y_Coordinate,puyo_len;
        //contains length of every smiley reason is it is acting as square
	Dimension screenSize;	
        //Contains the dimension of the screen interms of resolution
	public Tetris_Puyo() 
	{
		super("Tetris Puyo");
		Y_Coordinate=6;			
// Can be given any possible value, Our game,( X_Coordinate*Y_Coordinate) depends on this value	
//Width and Height contains the screen
                //resolution in terms of resoultion which we can treat as coordinates.
                
                
		//If the Window size is static then it is different to see one one computer to another.
                
		//Put the window at the center of the screen at any resolution and 
                //to adjust the window size and smiley size some calculations will be performed here.
                
		//Usually resolution(for pc) is windth*height format with 8:6 ratio.
                
		X_Coordinate=Y_Coordinate*2-3;
		screenSize= Toolkit.getDefaultToolkit().getScreenSize();
		X_length=screenSize.width;
		Y_length=screenSize.height;
		
		//For Smiley game(X_Coordinate=12,colums=6) window take 1:2(wd*ht)
                //ratio in screen resolution.
		//So in 8 parts of width(of screen) 2 parts is assigned to window widht and
		//and in 6 parts of height(of screen) 4 parts is assigned to window height to place
		
		
		puyo_len=(X_length/8)*2/Y_Coordinate;			//or (height/6)*4/12 
		Main_Object=new Main_GamePain(puyo_len,X_Coordinate,Y_Coordinate);
		Container O1=getContentPane();
		O1.add(Main_Object);
		setResizable(false);
		//Showing window at the center of the screen with total score diffculty and  pieces data
		//3 smilies width is added to the window for Total score board
		setBounds ((X_length/8)*3-puyo_len*3/2,
                          (Y_length/6)*1-puyo_len,(X_length/8)*2+puyo_len*3+6,
                          (Y_length/6)*4+25+puyo_len);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String args[]) {
		System.out.println("Nouman Ahmad And Hassaan Akbar...");
		JFrame.setDefaultLookAndFeelDecorated(true);
                //to set the look and feel for frame as defined int Java
		new Tetris_Puyo();
	}
}