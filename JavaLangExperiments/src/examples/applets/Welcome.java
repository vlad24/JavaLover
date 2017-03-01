package examples.applets;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Welcome extends Applet {
	
public void paint(Graphics g) {
	int xn = 0;
	int yn = 0;
	int hue = 1;
	int saturation = 1;
	int brightness = 1;
	int i = 0;
	Random r = new Random();
	while(i < 500){
		g.setColor(Color.getHSBColor(hue, saturation, brightness));
		g.fillRect(xn, yn, 15, 15);
		hue += 120;
		saturation += 130;
		brightness += 80;
		xn = (xn + r.nextInt()) % 500;
		yn = (yn + 21) % 500;
		i++;
	}
	}
} 