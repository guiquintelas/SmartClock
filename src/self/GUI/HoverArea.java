package self.GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import self.input.ListenerManager;
import self.input.MouseListener;
import self.util.ActionQueue;
import self.util.Variator;
import self.util.VariatorNumero;

public class HoverArea {
	private int x;
	private int y;

	private int off = 0;

	private int width;
	private int height;

	private int arc = 0;

	private int alpha = 0;
	private int alphaMax = 120;

	private boolean ativo = false;
	private boolean round = false;
	private boolean pausado = false;
	private int ani = 0;
	//1 = fade in
	//2 = fade out

	private static final int DELAY_ANI = 5;

	private Variator varAlpha;

	public static ArrayList<HoverArea> todosHoverArea = new ArrayList<HoverArea>();

	public HoverArea(final int x, final int y, final int width, final int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		initListener();
		initVariator();

		todosHoverArea.add(this);
	}

	public HoverArea(final int x, final int y, final int width, final int height, int arc) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		round = true;
		this.arc = arc;

		initListener();
		initVariator();

		todosHoverArea.add(this);
	}

	private void initListener() {
		ListenerManager.addListener(ListenerManager.MOUSE_MOVED, new MouseListener() {
			public void acao(MouseEvent e) {
				if (pausado) return;
				if (e.getX() > x && e.getX() < x + width) {
					if (e.getY() > y && e.getY() < y + height) {
						setAtivo(true);
						return;
					}
				}

				setAtivo(false);
			}
		});

		ListenerManager.addListener(ListenerManager.MOUSE_EXITED, new MouseListener() {
			public void acao(MouseEvent e) {
				if (pausado) return;
				setAtivo(false);
			}
		});
	}

	private void initVariator() {
		varAlpha = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				alpha = (int) numero;

			}

			public double getNumero() {
				return alpha;
			}

			public boolean devoContinuar() {
				return ani != 0;
			}
		});
	}

	public void setOffSet(int off) {
		this.off = off;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void pintar(Graphics2D g) {
		if (!ativo || pausado) return;

		Composite comp = g.getComposite();
		//System.out.println(alpha);
		if (ani != 0) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		} else {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}

		g.setColor(new Color(255,255,255, alpha));

		if (round) {
			g.fillRoundRect(x - off / 2, y - off / 2, width + off, height + off, arc, arc);
		} else {
			g.fillRect(x - off / 2, y - off / 2, width + off, height + off);
		}

		g.setComposite(comp);

	}

	public boolean isPausado() {
		return pausado;
	}

	public void setPausado(boolean pausado) {
		this.pausado = pausado;
		if (pausado)setAtivo(false);
	}
	
	public void setAlphaMax(int alpha) {
		if (alpha < 255 && alpha > 0) alphaMax = alpha;
	}

	private void setAtivo(boolean ativo) {

		if (this.ativo != ativo) {

			if ((ani == 2 && ativo) || ani == 1 && !ativo) {
				varAlpha.variar(false);
			}

			if (ativo && ani != 1) {
				this.ativo = true;
				ani = 1;
				varAlpha.fadeInSin(alpha, alphaMax, DELAY_ANI);
				varAlpha.variar(true);
				varAlpha.addAcaoNaFila(new ActionQueue() {
					public boolean action() {
						ani = 0;
						return true;
					}
				});

			} else if (!ativo && ani != 2){
				ani = 2;
				varAlpha.fadeOutSin(alpha, 0, DELAY_ANI);
				varAlpha.variar(true);
				varAlpha.addAcaoNaFila(new ActionQueue() {
					public boolean action() {
						ani = 0;
						HoverArea.this.ativo = false;
						return true;
					}
				});
			}
		}
	}

}
