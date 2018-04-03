package self.util;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import self.principal.Principal;


public class Util {
	
	public static BufferedImage carregarImg(String path) {
		BufferedImage img = null;

		try {
			img = ImageIO.read(Principal.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return img;
	}
	
	public static BufferedImage carregarQuickImg(String soNome) {
		return carregarImg("/res/" + soNome + ".png");
	}
	
	public static int limita(int num,int min, int max) {
		if (num > max) num = max;
		if (num < min) num = min;
		
		return num;
	}

	public static ArrayList<BufferedImage> carregarArrayBI(BufferedImage spriteSheet, int xInicial, int yInicial, int width, int height, int quantasBI) {
		ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();
		boolean inicial = true;

		for (int y = yInicial; y < spriteSheet.getHeight(); y += height) {
			for (int x = 0; x < spriteSheet.getWidth(); x += width) {
				if (inicial) {
					x = xInicial;
					inicial = false;
				}
				
				imgs.add(spriteSheet.getSubimage(x, y, width, height));
				if (imgs.size() >= quantasBI) {
					return imgs;
				}
			}
		}
		
		System.out.println("deu ruim");
		return imgs;
	}
	
	public static ArrayList<BufferedImage> carregarArrayBIVertical(BufferedImage spriteSheet, int xInicial, int yInicial, int width, int height, int quantasBI) {
		ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();
		boolean inicial = true;

		for (int x = xInicial; x < spriteSheet.getWidth(); x += width) {
			for (int y = 0; y < spriteSheet.getHeight(); y += height) {
				if (inicial) {
					y = yInicial;
					inicial = false;
				}
				
				imgs.add(spriteSheet.getSubimage(x, y, width, height));
				if (imgs.size() >= quantasBI) {
					return imgs;
				}
			}
		}
		
		System.out.println("deu ruim");
		return imgs;
	}

	public static double randomDouble(double min, double max) {
		return min + (Math.random() * (max - min));
	}
	
	public static float randomFloat(float min, float max) {
		return min + (float)(Math.random() * (max - min));
	}

	public static int randomInt(int min, int max) {
		return min + (int) (Math.random() * (max - min) + 0.5);
	}
	
	public static int getStringWidh(String string, Font font, Graphics2D g) {
		if (string == null || string == "") {
			return 0;
		}
		
		FontMetrics fm = g.getFontMetrics(font);
		try {
			return fm.stringWidth(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public static int getStringHeight(String string, Font font, Graphics2D g) {
		if (string == null || font == null || string == "") return 0;
		 FontRenderContext frc = g.getFontRenderContext();
		 return (int)font.getLineMetrics(string, frc).getHeight();
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<BufferedImage> removeFirsts(ArrayList<BufferedImage> imgs, int indexs) {
		ArrayList<BufferedImage> imgsTemp = (ArrayList<BufferedImage>)imgs.clone();
		for (int i = 0; i < indexs; i++) {
			imgsTemp.remove(0);
		}
		return imgsTemp;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<BufferedImage> removeLasts(ArrayList<BufferedImage> imgs, int indexs) {
		ArrayList<BufferedImage> imgsTemp = (ArrayList<BufferedImage>)imgs.clone();
		for (int i = 0; i < indexs; i++) {
			imgsTemp.remove(imgs.size() - 1);
		}
		return imgsTemp;
	}
	
	public static void paintRectOco(Graphics2D g, int x, int y, int width, int height, int size) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D)img.getGraphics();
		g2.setColor(g.getColor());
		g2.fillRect(0, 0, width, height);
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(size,size, width - size*2, height - size*2);
		
		g.drawImage(img, x, y, null);
		g2.dispose();
		img.flush();
	}
	
	public static void paintRectOcoRoundEx(Graphics2D g, int x, int y, int width, int height, int size, int arc) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D)img.getGraphics();
		g2.setColor(g.getColor());
		g2.setComposite(g.getComposite());
		g2.setRenderingHints(g.getRenderingHints());
		g2.fillRoundRect(0, 0, width, height, arc, arc);
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(size,size, width - size*2, height - size*2);
		
		g.drawImage(img, x, y, null);
		g2.dispose();
		img.flush();
	}

	
	public static void paintRectOcoRoundIn(Graphics2D g, int x, int y, int width, int height, int size, int arc) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D)img.getGraphics();
		g2.setColor(g.getColor());
		g2.setComposite(g.getComposite());
		g2.setRenderingHints(g.getRenderingHints());

		g2.fillRect(0, 0, width, height);
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRoundRect(size,size, width - size*2, height - size*2, arc, arc);
		
		g.drawImage(img, x, y, null);
		g2.dispose();
		img.flush();
	}

	
	public static void paintRectOcoRound(Graphics2D g, int x, int y, int width, int height, int size, int arc) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D)img.getGraphics();
		g2.setColor(g.getColor());
		g2.setComposite(g.getComposite());
		g2.setRenderingHints(g.getRenderingHints());
		g2.fillRoundRect(0, 0, width, height, arc, arc);
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRoundRect(size,size, width - size*2, height - size*2, arc, arc);
		
		g.drawImage(img, x, y, null);
		g2.dispose();
		img.flush();
	}

	
	public static boolean compararAngulos(double angulo1, double angulo2) {
		while (angulo1 > 180)angulo1 -= 360;
		while (angulo1 < -180)angulo1 += 360;
		while (angulo2 > 180)angulo2 -= 360;
		while (angulo2 < -180)angulo2 += 360;
		
		return angulo1 > angulo2;
	}
 }
