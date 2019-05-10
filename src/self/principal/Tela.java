package self.principal;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import self.GUI.HoverArea;
import self.especial.Especial;
import self.menu.Menu;
import self.util.ActionQueue;
import self.util.Util;
import self.util.Variator;
import self.util.VariatorNumero;

@SuppressWarnings("serial")
public class Tela extends JPanel {
	public BufferedImage buffer = new BufferedImage(Janela.WIDTH, Janela.HEIGHT + Janela.MENU_HEIGHT, BufferedImage.TYPE_INT_ARGB);
	public static Graphics2D g;

	private Font font = new Font("DS-Digital", Font.PLAIN, 50);
	private Relogio relogio;

	public static final int BORDA = 3;

	public static boolean aniInit = true;
	public boolean aniInitTempo = false;

	private float alpha = 0;

	private static Variator alphaVar;

	public void init(Relogio relogio) {
		this.relogio = relogio;

		Dimension dim = new Dimension(Janela.WIDTH, Janela.HEIGHT + Janela.MENU_HEIGHT);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);
		setBackground(new Color(0, 0, 0, 0));

		g = (Graphics2D) buffer.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setFont(font);
		repaint();

		alphaVar = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				if (numero < 0) numero = 0;
				if (numero > 1) numero = 1;
				alpha = (float) numero;

			}

			public double getNumero() {
				return alpha;
			}

			public boolean devoContinuar() {
				return aniInit || aniInitTempo;
			}
		});

		alphaVar.fadeInSin(0, 1, 35);
		alphaVar.variar(true);
		alphaVar.addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				aniInit = false;
				aniInitTempo = true;
				alpha = 0;
				alphaVar.fadeInSin(0, 1, 100);
				alphaVar.variar(true);
				return true;
			}
		});

		alphaVar.addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				aniInitTempo = false;
				return true;
			}
		});
	}

	public void paintComponent(Graphics g) {

		if (!Principal.pintar && !Menu.ani) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g;
		Composite comp = g2.getComposite();
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(0, 0, Janela.WIDTH, Janela.HEIGHT + Janela.MENU_HEIGHT);
		g2.setComposite(comp);

		pintar();

		if (aniInit) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2.drawImage(buffer, 0, 0, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		} else {
			g2.drawImage(buffer, 0, 0, this);
		}

		Principal.pintar = false;

		repaint();
	}

	public void pintar() {
		Composite comp = getGrafico().getComposite();
		getGrafico().setComposite(AlphaComposite.Clear);
		getGrafico().fillRect(0, 0, Janela.WIDTH, Janela.HEIGHT + Janela.MENU_HEIGHT);
		getGrafico().setComposite(comp);

		getGrafico().setColor(Color.BLACK);
		getGrafico().fillRoundRect(BORDA, BORDA, getWidth() - BORDA * 2, Janela.HEIGHT + Menu.height - BORDA * 2, 15, 15);

		if (!aniInitTempo && !aniInit) {
			pintarTempo();
		} else if (aniInitTempo) {
			int rand = Util.randomInt(0, 20 + (int) (alpha * 80));
			if (rand > 4) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
				pintarTempo();
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			}

		}

		pintarBotao();
		Menu.pintar(getGrafico(), Menu.height);
		Especial.pintar(g);
		pintarHoverArea(g);

		getGrafico().setColor(Color.DARK_GRAY);
		Util.paintRectOcoRound(g, 0, 0, Janela.WIDTH, Janela.HEIGHT + Menu.height, BORDA, 20);

	}

	private void pintarHoverArea(Graphics2D g) {
		for (int x = 0; x < HoverArea.todosHoverArea.size(); x++) {
			HoverArea.todosHoverArea.get(x).pintar(g);
		}

	}

	private void pintarBotao() {
		getGrafico().setColor(Color.WHITE);
		getGrafico().setFont(new Font("arial", Font.PLAIN, 12));
		getGrafico().drawString("x", Janela.WIDTH - 11, 11);

		getGrafico().drawString("_", Janela.WIDTH - 25, 9);

	}

	private void pintarTempo() {
		getGrafico().setColor(Color.WHITE);
		getGrafico().setFont(font);
		String tempo = relogio.getTempo();
		int tempoW = Util.getStringWidh(tempo, font, g);
		int tempoH = Util.getStringHeight(tempo, font, g);
		getGrafico().drawString(tempo, Janela.WIDTH / 2 - tempoW / 2, Janela.HEIGHT / 2 + tempoH / 2 - 7);

	}

	private Graphics2D getGrafico() {

		return g;

	}

	public BufferedImage getBuffer() {

		return buffer;

	}

}
