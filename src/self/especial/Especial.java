package self.especial;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import self.menu.Menu;
import self.principal.Janela;
import self.principal.Principal;
import self.util.ActionQueue;
import self.util.Variator;
import self.util.VariatorNumero;

public class Especial {
	public static boolean rodando = false;
	private static boolean aberto = false;
	private static boolean abrindo = false;
	private static int posJanela = 0;
	
	private static final int POS_ERRADA  = 0;
	private static final int POS_MOVENDO = 1;
	private static final int POS_CERTA   = 2;
	
	private static float alpha = 0;
	
	private static Variator varSize;
	private static Variator varAlpha;
	
	public static void init() {
		varSize = new Variator(new VariatorNumero() {
			int yJanela = 0;
			public void setNumero(double numero) {
				if (yJanela == 0) yJanela = Janela.y;
				
				Menu.height = (int)numero;
				Principal.janela.setLocation(Janela.x, yJanela - (int)(numero/2));
			}
			public double getNumero() {
				return Menu.height;
			}
			public boolean devoContinuar() {
				return true;
			}
		});
		
		varAlpha = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				if (numero > 1) numero = 1;
				alpha = (float)numero;
			}
			
			public double getNumero() {
				return alpha;
			}
			
			public boolean devoContinuar() {
				return true;
			}
		});
	}
	
	public static void iniciar() {
		System.out.println("Especial Ativado");
		rodando = true;
		
		if (Menu.aberto) Menu.fechar();
	}
	
	public static void update() {
		if (!rodando) return;
		
		moverJanela();
	}
	
	private static void moverJanela() {
		if (posJanela == POS_ERRADA && !Janela.isMovendo() && !Menu.aberto) {
			System.out.println("Começou a mover");
			Janela.moverJanela(Janela.WIDTH_TELA/2 - Janela.WIDTH/2, Janela.HEIGHT_TELA/2 - Janela.HEIGHT/2, 100);
			posJanela = POS_MOVENDO;
		}
		
		if (!Janela.isMovendo() && posJanela == POS_MOVENDO) {
			posJanela = POS_CERTA;
			abrir();
			System.out.println("Parou de mover");
		}
	}

	private static void abrir() {
		if (aberto || abrindo) return;
		
		abrindo = true;

		varSize.addEsperaNaFila(25);
		varSize.addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				varSize.fadeInSin(0, Janela.MENU_HEIGHT, 100);		
				varSize.variar(true);
				
				Menu.separadorVar.fadeInSin(0, Menu.DIVISOR_WIDTH_MAX, 100);
				Menu.separadorVar.variar(true);
				
				varAlpha.fadeInSin(0, 1, 100);
				varAlpha.variar(true);
							
				return true;
			}
		});
		
		Menu.sizeVar.addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				aberto = true;
				abrindo = false;
				return true;
			}
		});
		
		Menu.sizeVar.variar(true);
	}
	
	public static void pintar(Graphics2D g) {
		if (!rodando) return;
		
		if (aberto || abrindo) {
			Composite comp = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g.setColor(Color.DARK_GRAY);
			g.drawLine(Janela.WIDTH / 2 - Menu.divWidth / 2, Janela.HEIGHT - 5, Janela.WIDTH / 2 + Menu.divWidth / 2, Janela.HEIGHT - 5);
			g.setComposite(comp);
		}
		
	}

}
