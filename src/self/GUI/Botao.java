package self.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import self.input.ListenerManager;
import self.input.MouseListener;
import self.menu.Menu;
import self.util.Util;

public class Botao {
	private int x;
	private int y;
	private int width;
	private int height;
	private int offYTexto;
	
	private static final int APERTADO_ESCURO = 40;
	
	private Color corBorda;
	private Color corFundo;
	
	private Font font = Menu.fontOpcoes;
	
	private String texto;
	
	private BufferedImage img;
	
	private ActionListener action;
	
	private boolean ativo = true;
	private boolean apertado = false;
	private boolean isImg = false;
	
	public HoverArea ha;

	
	public Botao(final int x, final int y, final int width,final  int height, Color corBorda, Color corFundo, String texto, int offYTexto, final ActionListener action) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.corBorda = corBorda;
		this.corFundo = corFundo;
		this.texto = texto;
		this.action = action;
		this.offYTexto = offYTexto;
		ha = new HoverArea(x, y, width, height);
		ha.setAlphaMax(25);
		
		ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, new MouseListener() {
			public void acao(MouseEvent e) {
				if (e.getX() > x && e.getX() < x + width && e.getY() > y && e.getY() < y + height) {
					if (ativo)apertado = true;
				}
				
			}
		});	

		ListenerManager.addListener(ListenerManager.MOUSE_RELEASED, new MouseListener() {
			public void acao(MouseEvent e) {
				if (e.getX() > x && e.getX() < x + width && e.getY() > y && e.getY() < y + height) {
					if (apertado) action.actionPerformed(null);
					apertado = false;
				}
			}
		});
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Color getCorBorda() {
		return corBorda;
	}

	public void setCorBorda(Color corBorda) {
		this.corBorda = corBorda;
	}

	public Color getCorFundo() {
		return corFundo;
	}

	public void setCorFundo(Color corFundo) {
		this.corFundo = corFundo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setImg(BufferedImage img) {
		this.img = img;
		isImg = true;
	}

	public ActionListener getAction() {
		return action;
	}

	public void setAction(ActionListener action) {
		this.action = action;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
		ha.setPausado(!ativo);
	}
	
	public void pintar(Graphics2D g) {
		if (ativo) {
			if (apertado) {
				g.setColor(new Color(Util.limita(corFundo.getRed() - APERTADO_ESCURO, 0, 255), Util.limita(corFundo.getGreen() - APERTADO_ESCURO, 0, 255), Util.limita(corFundo.getRed() - APERTADO_ESCURO, 0, 255)));
			} else {
				g.setColor(corFundo);
			}
		} else {
			g.setColor(Color.BLACK);
		}
		
		g.fillRoundRect(x, y, width, height, 5, 5);
	
		if (ativo) {
			if (apertado) {
				g.setColor(new Color(Util.limita(corBorda.getRed() - APERTADO_ESCURO, 0, 255), Util.limita(corBorda.getGreen() - APERTADO_ESCURO, 0, 255), Util.limita(corBorda.getRed() - APERTADO_ESCURO, 0, 255)));
			} else {
				g.setColor(corBorda);
			}
		} else {
			g.setColor(Menu.corPadrao);
		}
		

		
		
		Util.paintRectOcoRound(g, x, y, width, height, 1, 5);
		
		if (ativo) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(new Color(255,255,255,100));
		}
		
		if (isImg) {
			g.drawImage(img, getX() + width/2 - img.getWidth()/2, getY() + height/2 - img.getHeight()/2, null);
			
		} else {
			g.setFont(font);
			int widthTexto = Util.getStringWidh(texto, font, g);
			int heightTexto = Util.getStringHeight(texto, font, g);
			g.drawString(texto, x + width/2 - widthTexto/2, y + heightTexto - height/2 + heightTexto/2 + offYTexto);
		}
		
		
		
	}
	
}
