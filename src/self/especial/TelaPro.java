package self.especial;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

import self.principal.Principal;


@SuppressWarnings("serial")
public class TelaPro extends JPanel {
	private BufferedImage buffer;
	private Graphics2D g;
	private int width = 0;
	private int height = 0;
	
	private boolean pintar = false;
	
	private Projetil projetil;
	
	private Timer timerDelay; 
	
	public TelaPro(Dimension dim, Projetil projetil) {
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		setBackground(new Color(0, 0, 0, 0));
		
		width = dim.width;
		height = dim.height;
		this.projetil = projetil;
		
		buffer = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D)buffer.getGraphics();
		
		timerDelay = new Timer(5, new ActionListener() {
			int tickAtual = Principal.tickTotal;
			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= tickAtual + 1) {
					pintar = true;
					tickAtual = Principal.tickTotal;
				}
				
			}
		});
		
		timerDelay.start();
	}
	
	public void paintComponent(Graphics g) {
		if (!pintar) {
			repaint();
			return;
		}
		pintar = false;
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Composite comp = g2.getComposite();
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(0, 0, width, height);
		g2.setComposite(comp);
		
		projetil.pintar(this.g);
		
		g2.drawImage(buffer, 0, 0, this);
		repaint();
	}
	
	
}
