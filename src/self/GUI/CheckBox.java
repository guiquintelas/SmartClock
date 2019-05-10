package self.GUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import self.input.KeyListener;
import self.input.ListenerManager;
import self.input.MouseListener;
import self.menu.Menu;
import self.util.ActionQueue;
import self.util.Util;
import self.util.Variator;
import self.util.VariatorNumero;

public class CheckBox {
	private int x;
	private int y;
	private int size;
	private int hotkey;
	
	private boolean check = false;
	
	private int aniCheck1;
	private int aniCheck2;
	
	private boolean isAniCheck1 = true;
	private boolean isAniCheck2 = false;
	private boolean hover = false;
	private boolean aniAtiva = false;
	
	private static final int DELAY_ANI = 5;
	
	protected Variator varAniCheck1;
	protected Variator varAniCheck2;
	
	protected ActionListener al;
	
	protected int grupo = 0;
	
	private int r = Menu.corPadrao.getRed();
	private int g = Menu.corPadrao.getGreen();
	private int b = Menu.corPadrao.getBlue();
	
	private Variator varR;
	private Variator varG;
	private Variator varB;
	
	private static ArrayList<CheckBox> todasCheckBox = new ArrayList<CheckBox>();
	
	public CheckBox(final int x,final  int y,final  int size,  int hotkey, final ActionListener al) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.al = al;
		this.hotkey = hotkey;
		
		
		ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, new MouseListener() {
			public void acao(MouseEvent e) {
				if (e.getX() > x && e.getX() < x + size) {
					if (e.getY() > y && e.getY() < y + size) {
						if (check) 
							return;
						
						setCheck(true);
					}
				}
				
			}
		});
		
		ListenerManager.addListener(ListenerManager.KEY_PRESSED, new KeyListener() {
			public void acao(KeyEvent e) {
				if (e.getKeyCode() == CheckBox.this.hotkey) {
					setCheck(true);
				}
			}
		});
		
		ListenerManager.addListener(ListenerManager.MOUSE_MOVED, new MouseListener() {
			public void acao(MouseEvent e) {
				if (e.getX() > x && e.getX() < x + size && e.getY() > y -3 && e.getY() < y + size + 3) {
					aniHover(true);
					hover = true;
					
				} else {
					aniHover(false);
					hover = false;
					
				}
				
			}
		});
		
		varR = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				numero = Util.limita((int)numero, 0, 255);
				r = (int)numero;
			}
			
			public double getNumero() {
				return r;
			}
			
			public boolean devoContinuar() {
				return true;
			}
		});
		
		varG = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				numero = Util.limita((int)numero, 0, 255);
				g = (int)numero;
			}
			
			public double getNumero() {
				return g;
			}
			
			public boolean devoContinuar() {
				return true;
			}
		});
		
		varB = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				numero = Util.limita((int)numero, 0, 255);
				b = (int)numero;
			}
			
			public double getNumero() {
				return b;
			}
			
			public boolean devoContinuar() {
				return true;
			}
		});
		
		varAniCheck1 = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				aniCheck1 = (int)(numero + .5);
				
			}
			
			public double getNumero() {
				return aniCheck1;
			}
			
			public boolean devoContinuar() {
				return check;
			}
		});
		
		varAniCheck2 = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				aniCheck2 = (int)(numero + .5);
				
			}
			
			public double getNumero() {
				return aniCheck2;
			}
			
			public boolean devoContinuar() {
				return check;
			}
		});
		
		todasCheckBox.add(this);
	}
	
	private void aniHover(boolean ativo) {
		if (this.hover != ativo) {
			

			varR.variar(false);
			varG.variar(false);
			varB.variar(false);
			varR.clearFila();
			
			if (ativo) {
				aniAtiva = true;
				varR.fadeInSin(r, Menu.corSel.getRed(), DELAY_ANI);
				varG.fadeInSin(g, Menu.corSel.getGreen(), DELAY_ANI);
				varB.fadeInSin(b, Menu.corSel.getBlue(), DELAY_ANI);
				varR.addAcaoNaFila(new ActionQueue() {
					public boolean action() {
						aniAtiva = false;
						return true;
					}
				});
				

			} else {
				aniAtiva = true;
				varR.fadeOutSin(r, Menu.corPadrao.getRed(), DELAY_ANI);
				varG.fadeOutSin(g, Menu.corPadrao.getGreen(), DELAY_ANI);
				varB.fadeOutSin(b, Menu.corPadrao.getBlue(), DELAY_ANI);
				
				varR.addAcaoNaFila(new ActionQueue() {
					public boolean action() {
						aniAtiva = false;
						return true;
					}
				});
			}
			
			varR.variar(true);
			varG.variar(true);
			varB.variar(true);
			
		}
		
		
	}

	public void setCheck(boolean check) {
		setCheck(check, true);
	}
	
	public void setCheck(boolean check, boolean triggerAction) {
		if (check) {
			if (this.check) return;

			if (triggerAction)
				al.actionPerformed(null);
			
			for (int x = 0; x < todasCheckBox.size(); x++) {
				CheckBox cbAtual = todasCheckBox.get(x);
				
				if (grupo != 0 && cbAtual.grupo == grupo) {
					cbAtual.check = false;
				}					
			}

			this.check = true;	
			isAniCheck1 = true;
			varAniCheck1.fadeInSin(0, size - 4, 5);
			varAniCheck1.variar(true);
			varAniCheck1.addAcaoNaFila(new ActionQueue() {
				public boolean action() {
					varAniCheck1.fadeInSin(0, size - 4, 8);
					varAniCheck1.variar(true);
					varAniCheck2.fadeOutSin(size + 4, 0, 8);
					varAniCheck2.variar(true);
					isAniCheck1 = false;
					isAniCheck2 = true;
					return true;
				}
			});
			
			varAniCheck1.addAcaoNaFila(new ActionQueue() {
				public boolean action() {
					isAniCheck2 = false;
					return true;
				}
			});
		} else {
			varAniCheck1.clearFila();
			varAniCheck1.variar(false);
			varAniCheck2.clearFila();
			varAniCheck2.variar(false);
			isAniCheck1 = false;
			isAniCheck2 = false;
			this.check = false;
		}
	}
	
	public void pintar(Graphics2D g) {
		if (!check) {
			if (aniAtiva) {
				g.setColor(new Color(r,this.g,b));
			} else if (hover) {
				g.setColor(Menu.corSel);	
			} else {
				g.setColor(Menu.corPadrao);
			}
			
		} else {
			g.setColor(Menu.corPadrao);
		}
		
		
		g.drawRoundRect(x, y, size, size, 3, 3);
		
		if (check) {
			g.setColor(Menu.corSel);
			
			
			
			int ani1 = size - 4;
			if (isAniCheck1) ani1 = aniCheck1;
			g.drawLine(x + 2, y + 2, x + 2 + ani1, y + 2 + ani1);
			
			if (!isAniCheck1) {
				int ani2 = size - 4;
				int ani3 = 0;
				if (isAniCheck2) {
					ani2 = aniCheck1;
					ani3 = aniCheck2;
				}
				//max x size - 2 min y -2
				g.drawLine(x + 2, y + size + 2, x + 2 + ani2 , y + ani3 -2);
			}
			
		}
	}
	
	public void setGrupo(int grupo) {
		this. grupo = grupo;
	}
	
	public boolean getStatus() {
		return check;
	}
}
