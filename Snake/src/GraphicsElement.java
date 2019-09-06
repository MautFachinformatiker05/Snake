import java.awt.Image;

import javax.swing.ImageIcon;

public class GraphicsElement {

	public final static int APPLE = 0;
	
	static Image[] s_array;
	
	static String fileNames[] = {"Apfel.png","h_Körper.png","h_Körper2.png","v_Körper.png","v_Körper2.png",
								"r_Kopf.png","r_Kopf2.png","o_Kopf.png","o_Kopf2.png","l_Kopf.png","l_Kopf2.png","u_Kopf.png","u_Kopf2.png",
								"Biegung_ru.png","Biegung_ro.png","Biegung_lu.png","Biegung_lo.png",
								"r_Schweif.png","r_Schweif2.png","o_Schweif.png","o_Schweif2.png","l_Schweif.png","l_Schweif2.png","u_Schweif.png","u_Schweif2.png"}; 
	
	
	public static void scaleImages(int breite, int hoehe) {
		
		int imageCount = fileNames.length;
		
		s_array = new Image[imageCount];
		
		for(int i = 0; i < imageCount; i++) {
	
			ImageIcon imageIcon = new ImageIcon(fileNames[i]);
			Image scaledImage  = imageIcon.getImage().getScaledInstance(breite, hoehe, Image.SCALE_DEFAULT);
			s_array[i] = scaledImage; 
		}
				
	}
	

	public static Image getImage(int index) {
		
		return s_array[index];
		
	}
	
}
