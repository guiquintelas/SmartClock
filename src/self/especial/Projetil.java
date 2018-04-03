package self.especial;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;

import self.principal.Janela;

public class Projetil {
	private double x;
	private double y;
	private int width;
	private int height;
	
	private double angulo;
	private double speed = 6;
	
	private JFrame janela;
	private TelaPro tela;
	
	
	public static ArrayList<Projetil> todosProjetil = new ArrayList<Projetil>();
	
	public Projetil(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		tela = new TelaPro(new Dimension(width, height), this);
		janela = new JFrame();
		janela.setUndecorated(true);
		janela.setBackground(new Color(0,0,0,0));
		janela.setResizable(false);
		janela.setLayout(null);
		janela.setContentPane(tela);
		janela.setLocation(x, y);
		janela.setAlwaysOnTop(true);
		janela.pack();
		janela.setVisible(true);
		
		
		
		todosProjetil.add(this);
	}
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public void update() {
		updateAngulo();
		mover();
	}
	
	private void updateAngulo() {
		double anguloNovo;
		
		anguloNovo = Math.toDegrees(Math.atan2(Janela.yMouseTela - getYCentro(), Janela.xMouseTela - getXCentro()));

	    if(anguloNovo < 0){
	        anguloNovo += 360;
	    }
		
	    angulo = anguloNovo;
	}
	
	private int getXCentro() {
		return (int)(x + width/2);
	}
	
	private int getYCentro() {
		return (int)(y + height/2);
	}

	private void mover() {
		x += Math.sin(Math.toRadians(angulo + 90)) * speed;
		y -= Math.cos(Math.toRadians(angulo + 90)) * speed;
		System.out.println(angulo);
		
		janela.setLocation(getX(), getY());
	}

	public void pintar(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillOval(0, 0, width, height);
	}
	
}
