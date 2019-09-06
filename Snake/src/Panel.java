import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
class Window extends JFrame {

	public Window() {

		setTitle("Snake");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setPreferredSize(new Dimension(600,600));
		this.pack();
		setLocationRelativeTo(null);

	}
}

@SuppressWarnings("serial")
class Panel extends JPanel {

	final int NORTH = 0;
	final int EAST = 1;
	final int SOUTH = 2;
	final int WEST = 3;

	final int APPLE =   0;
	final int H_BODY =  1;
	final int H_BODY2 = 2;
	final int V_BODY =  3;
	final int V_BODY2 = 4;
	final int R_HEAD =  5;
	final int R_HEAD2 = 6;
	final int O_HEAD =  7;
	final int O_HEAD2 = 8;
	final int L_HEAD =  9;
	final int L_HEAD2 = 10;
	final int U_HEAD =  11;
	final int U_HEAD2 = 12;
	final int RU_TRANSITION = 13;
	final int RO_TRANSITION = 14;
	final int LU_TRANSITION = 15;
	final int LO_TRANSITION = 16;
	final int R_TAIL =  17;
	final int R_TAIL2 = 18;
	final int O_TAIL =  19;
	final int O_TAIL2 = 20;
	final int L_TAIL =  21;
	final int L_TAIL2 = 22;
	final int U_TAIL =  23;
	final int U_TAIL2 = 24;

	int fenster_x = 600;			// Breite Fenster
	int fenster_y = 600;			// Höhe Fenster
	int davor_fenster_x = 600;
	int davor_fenster_y = 600;
	int spielfeld_x = 20;						// Breite Spielfeld
	int spielfeld_y = 20;						// Höhe Spielfeld
	int breite= fenster_x/spielfeld_x;
	int hoehe= fenster_y/spielfeld_y;
	int snake_x[] = new int[ ( spielfeld_x * spielfeld_y) ];	// x-Pos Snake
	int snake_y[] = new int[ ( spielfeld_x * spielfeld_y) ];	// y-Pos Snake
	int snake_lenght = 3;					// Länge Snake
	int apple_x = 0;						// x-Pos Apple
	int apple_y = 0;						//y-Pos Apple
	boolean alive = true;					// Game Over?
	byte last_key_pressed = 0; // 0=N 1=E 2=S 3=W
	byte last_key_pressed_early = 0;
	int random = 0;							// random
	boolean tick = false;
	int direction_schweif = 0;

	public Panel() {

		snake_x[0] = 15;
		snake_y[0] = 15;
		snake_x[1] = 14;
		snake_y[1] = 15;
		snake_x[2] = 13;
		snake_y[2] = 15;

		this.setBackground(Color.decode("#f3e5d8"));

		create_apple();
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (alive==true) {
			g.drawImage(GraphicsElement.getImage(APPLE), apple_x*breite, apple_y*hoehe, this);

			switch (last_key_pressed) {
			case NORTH:							//NORTH
				g.drawImage(GraphicsElement.getImage(tick ? U_HEAD : U_HEAD2),  snake_x[0]*breite, snake_y[0]*hoehe, this);
				break;
			case EAST:							//EAST
				g.drawImage(GraphicsElement.getImage(tick ? L_HEAD : L_HEAD2),  snake_x[0]*breite, snake_y[0]*hoehe, this);
				break;
			case SOUTH:							//SOUTH
				g.drawImage(GraphicsElement.getImage(tick ? O_HEAD : O_HEAD2),  snake_x[0]*breite, snake_y[0]*hoehe, this);
				break;
			case WEST:							//WEST
				g.drawImage(GraphicsElement.getImage(tick ? R_HEAD : R_HEAD2),  snake_x[0]*breite, snake_y[0]*hoehe, this);
				break;

			default:
				System.out.println("HEAD ERROR");
				break;
			}

			for(int i=1;i<snake_lenght;i++) {
				if(i==snake_lenght-1) {
					if ( snake_x[snake_lenght-1]>snake_x[snake_lenght-2])
						direction_schweif = WEST;
					if ( snake_y[snake_lenght-1]>snake_y[snake_lenght-2])
						direction_schweif = NORTH;
					if ( snake_x[snake_lenght-1]<snake_x[snake_lenght-2])
						direction_schweif = EAST;
					if ( snake_y[snake_lenght-1]<snake_y[snake_lenght-2])
						direction_schweif = SOUTH;

					switch (direction_schweif) {
					case NORTH:							//NORTH
						g.drawImage(GraphicsElement.getImage(tick ? O_TAIL : O_TAIL2),  snake_x[snake_lenght-1]*breite, snake_y[snake_lenght-1]*hoehe, this);
						break;
					case EAST:							//EAST
						g.drawImage(GraphicsElement.getImage(tick ? R_TAIL : R_TAIL2),  snake_x[snake_lenght-1]*breite, snake_y[snake_lenght-1]*hoehe, this);
						break;
					case SOUTH:							//SOUTH
						g.drawImage(GraphicsElement.getImage(tick ? U_TAIL : U_TAIL2),  snake_x[snake_lenght-1]*breite, snake_y[snake_lenght-1]*hoehe, this);
						break;
					case WEST:							//WEST
						g.drawImage(GraphicsElement.getImage(tick ? L_TAIL : L_TAIL2),  snake_x[snake_lenght-1]*breite, snake_y[snake_lenght-1]*hoehe, this);
						break;

					default:
						System.out.println("TAIL ERROR");
						break;
					}
				} else { 
					int vorderer_nachbar = 0;
					if ( snake_x[i]>snake_x[i-1])
						vorderer_nachbar = 3;
					if ( snake_y[i]>snake_y[i-1])
						vorderer_nachbar = 0;
					if ( snake_x[i]<snake_x[i-1])
						vorderer_nachbar = 1;
					if ( snake_y[i]<snake_y[i-1])
						vorderer_nachbar = 2; 

					int hinterer_nachbar = 0;
					if ( snake_x[i]>snake_x[i+1])
						hinterer_nachbar = 3;
					if ( snake_y[i]>snake_y[i+1])
						hinterer_nachbar = 0;
					if ( snake_x[i]<snake_x[i+1])
						hinterer_nachbar = 1;
					if ( snake_y[i]<snake_y[i+1])
						hinterer_nachbar = 2; 

					if ((hinterer_nachbar==NORTH && vorderer_nachbar==SOUTH) || (hinterer_nachbar==SOUTH && vorderer_nachbar==NORTH)) {
						g.drawImage(GraphicsElement.getImage(tick ? V_BODY : V_BODY2),  snake_x[i]*breite, snake_y[i]*hoehe, this);
					}

					if ((hinterer_nachbar==EAST && vorderer_nachbar==WEST) || (hinterer_nachbar==WEST && vorderer_nachbar==EAST)) {
						g.drawImage(GraphicsElement.getImage(tick ? H_BODY : H_BODY2),  snake_x[i]*breite, snake_y[i]*hoehe, this);
					}

					if ((hinterer_nachbar==EAST && vorderer_nachbar==SOUTH) || (hinterer_nachbar==SOUTH && vorderer_nachbar==EAST)) {
						g.drawImage(GraphicsElement.getImage(RU_TRANSITION),  snake_x[i]*breite, snake_y[i]*hoehe, this);
					}

					if ((hinterer_nachbar==NORTH && vorderer_nachbar==EAST) || (hinterer_nachbar==EAST && vorderer_nachbar==NORTH)) {
						g.drawImage(GraphicsElement.getImage(RO_TRANSITION),  snake_x[i]*breite, snake_y[i]*hoehe, this);
					}

					if ((hinterer_nachbar==2 && vorderer_nachbar==3) || (hinterer_nachbar==3 && vorderer_nachbar==2)) {
						g.drawImage(GraphicsElement.getImage(LU_TRANSITION),  snake_x[i]*breite, snake_y[i]*hoehe, this);
					}

					if ((hinterer_nachbar==3 && vorderer_nachbar==0) || (hinterer_nachbar==0 && vorderer_nachbar==3)) {
						g.drawImage(GraphicsElement.getImage(LO_TRANSITION),  snake_x[i]*breite, snake_y[i]*hoehe, this);
					}
				} 
			} 

		} 
		else
		{
			Font f = new Font("Calibri", Font.BOLD, fenster_x/30);
			FontMetrics metrics = getFontMetrics(f);
			g.setFont(f);
			g.drawString("Game over - Score "+calc_score(), (fenster_x/2)-metrics.stringWidth("Game over"), fenster_y/2);
		}
		repaint();
	}

	private void create_apple() {				// prüft, ob Positionen Snake != Apple und ruft ran_x() und ran_y() auf

		ran_x();
		for (int i=0; i < snake_lenght; i++)
		{
			if (snake_x[i]==apple_x)
			{
				ran_x();
				continue;
			}
		}

		ran_y();
		for (int i=0; i < snake_lenght; i++)
		{
			if (snake_y[i]==apple_y)
			{
				ran_y();
				continue;
			}
		}
	}

	public void ran_y() {					// generiert Zufallszahl für y-Pos Apple
		random = (int) (Math.random()*spielfeld_y);
		apple_y = random;
	}

	public void ran_x() {					// generiert Zufallszahl für x-Pos Apple
		random = (int) (Math.random()*spielfeld_x);
		apple_x = random;
	}

	private boolean update() {					// Prüft, ob Spiel zuende + viele Funktionsaufrufe

		update_last_key_pressed();
		move_snake();
		pre_draw();
		//console_print();
		check_snake_apple();
		check_snake_dead();
		fenster_x = this.getWidth();			// Breite Fenster
		fenster_y = this.getHeight();			// Höhe Fenster
		if (fenster_x!=davor_fenster_x || fenster_y!=davor_fenster_y)
		{
			davor_fenster_x = fenster_x;
			davor_fenster_y = fenster_y;
			pre_draw();
			GraphicsElement.scaleImages(breite, hoehe);
		}

		tick = !tick;

		return alive;
	}


	private void update_last_key_pressed() {

		last_key_pressed = last_key_pressed_early;
	}

	private void pre_draw() {					// Grafik
		breite= fenster_x/spielfeld_x;
		hoehe= fenster_y/spielfeld_y;
	}

	@SuppressWarnings("unused")
	private void console_print() {			// DEBUG  AUSGABE AUF KONSOLE  DEBUG

		System.out.println("____________________________________________________________");
		for(int y=0; y<spielfeld_y; y++)
		{
			for(int x=0; x<spielfeld_x; x++)
			{
				console_print_schleife(x,y);
				System.out.print(" ");
			}
			System.out.println("|");
		}
		System.out.println("▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔");
	}

	private void console_print_schleife(int x, int y) {
		boolean printed = false;
		for(int i=0; i<snake_lenght; i++)
		{
			if(snake_x[i]==x && snake_y[i]==y && printed==false)
			{
				if (i==0)
				{
					switch (last_key_pressed) {
					case 0:
						System.out.print("^");
						printed = true;
						break;

					case 1:
						System.out.print(">");
						printed = true;
						break;

					case 2:
						System.out.print("▼");
						printed = true;
						break;

					case 3:
						System.out.print("<");
						printed = true;
						break;

					default:
						break;
					}
				}
				else {
					System.out.print("•");
					printed = true;
				}
			}
		}
		if (apple_x==x && apple_y==y && printed==false) {
			System.out.print("A");
			printed = true;
		}
		if (printed==false)
			System.out.print(" ");

	}

	private void move_snake() {			// Kopieren der Pos der Glieder + Bewegung des Kopfes

		for(int i = snake_lenght-1; i >= 1; i--)
		{
			snake_x[i] = snake_x[i-1];
			snake_y[i] = snake_y[i-1];
		}


		switch (last_key_pressed) {
		case 0: // NORTH
			snake_y[0] -= 1;
			break;
		case 1: // EAST
			snake_x[0] += 1;
			break;
		case 2: // SOUTH
			snake_y[0] += 1;
			break;
		case 3: // WEST
			snake_x[0] -= 1;
			break;
		default:
			break;
		}

	}

	private void check_snake_dead() {				// Prüft, ob Snake sich selbst oder die Wand beißt

		if (snake_x[0] < 0 || snake_x[0] > spielfeld_x)
			alive = false;
		if (snake_y[0] < 0 || snake_y[0] > spielfeld_y)
			alive = false;

		if (snake_lenght>3)
		{
			for(int n=1; n<snake_lenght;n++)
			{
				if (snake_x[0]==snake_x[n] && snake_y[0]==snake_y[n])
					alive=false;
			}
		}

	}

	private void check_snake_apple() {				// Prüft, ob Pos Snake == Apple

		if (snake_x[0]==apple_x && snake_y[0]==apple_y)
		{
			snake_x[snake_lenght] = snake_x[snake_lenght-1];
			snake_y[snake_lenght] = snake_y[snake_lenght-1];
			snake_lenght++;
			create_apple();
		}
	}

	private void set_last_key(int key) {
		switch (key) {
		case 38:
		{	
			if (last_key_pressed!=2)
				last_key_pressed_early = 0;		// NORTH
			//System.out.println("DEBUG: UP");
			break;
		}
		case 39:
		{
			if (last_key_pressed!=3)
				last_key_pressed_early = 1;		// EAST
			//System.out.println("DEBUG: RIGHT");
			break;
		}
		case 40:
		{
			if (last_key_pressed!=0)
				last_key_pressed_early = 2;		// SOUTH
			//System.out.println("DEBUG: DOWN");
			break;
		}
		case 37:
		{
			if (last_key_pressed!=1)
				last_key_pressed_early = 3;		// WEST
			//System.out.println("DEBUG: LEFT");
			break;
		}
		default:
			break;
		}
	}

	public int calc_score() {

		return snake_lenght-3;
	}

	public static void main(String[] args) {	// Startet das Spiel

		boolean weiter = true;
		int zahler = 0;
		int sleep = 400;

		GraphicsElement.scaleImages(30, 30);

		Panel panel = new Panel();
		Window window = new Window();
		window.add(panel);


		// panel.setVisible(true);
		window.setVisible(true);


		window.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				panel.set_last_key(e.getExtendedKeyCode());		// W 87  A 65  S 83  D 68
				System.out.println(e.getExtendedKeyCode());
			}
		});

		while(weiter)
		{
			weiter = panel.update();
			panel.fenster_x = panel.getWidth();			// Breite Fenster
			panel.fenster_y = panel.getHeight();			// Höhe Fenster
			window.setTitle("Snake      Score: "+panel.calc_score());
			zahler++;
			if (zahler%10==0 && sleep>99)
			{
				sleep -= 10;
			}
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//while(true) {
		//	panel.fenster_x = panel.getWidth();			// Breite Fenster
		//	panel.fenster_y = panel.getHeight();			// Höhe Fenster}
		//}

	}
}
