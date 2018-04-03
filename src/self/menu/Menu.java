package self.menu;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import self.GUI.Botao;
import self.GUI.CheckBox;
import self.GUI.HoverArea;
import self.GUI.TimeCell;
import self.input.KeyListener;
import self.input.ListenerManager;
import self.input.MouseListener;
import self.principal.Janela;
import self.principal.Principal;
import self.principal.Tela;
import self.principal.Timer;
import self.util.ActionQueue;
import self.util.Util;
import self.util.Variator;
import self.util.VariatorNumero;

public class Menu {

	private static Timer timer1;
	private static Timer timer2;
	public static HashMap<Integer, Timer> timers = new HashMap<Integer, Timer>();

	public static Font fontOpcoes = new Font("DS-Digital", Font.PLAIN, 13);
	public static Font fontOpcoesG = new Font("DS-Digital", Font.PLAIN, 15);
	public static Font fontTimerB = new Font("DS-Digital", Font.PLAIN, 30);

	public static final Color corPadraoClara = new Color(100, 100, 100);
	public static final Color corPadrao = new Color(60, 60, 60);
	public static final Color corPadraoEscura = new Color(40, 40, 40);
	public static final Color corSel = new Color(255, 215, 0);
	public static final Color corSel1 = new Color(155, 115, 0);

	public static boolean aberto;
	public static boolean ani = false;

	public static int height = 0;
	public static int divWidth = 0;

	public static int timerSel = 1;

	private static float alpha = 0;

	private static CheckBox especial;
	private static CheckBox suspender;
	private static CheckBox reiniciar;
	private static CheckBox desligar;

	private static ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();

	private static final int widthTimeCell = 30;
	private static final int heightTimeCell = 23;
	private static final int div = 5;

	private static final int xS = Janela.WIDTH - widthTimeCell - Tela.BORDA - div;

	private static final int xOk = Janela.WIDTH - widthTimeCell * 4 - Tela.BORDA - div * 4;

	private static final int widthT12 = 34;
	private static final int heightMT = 30;
	private static final int heightT12 = (Janela.MENU_HEIGHT - heightMT - Tela.BORDA - 5) / 2 - 4;
	private static final int xT12 = Tela.BORDA + 5 + 3;
	private static final int yT1 = Janela.HEIGHT + 3 + heightMT;
	private static final int yT2 = yT1 + heightT12 + 3 + 3;
	

	private static final int ySMH = Janela.HEIGHT + Janela.MENU_HEIGHT - heightTimeCell - Tela.BORDA - div;

	private static final float DELAY_MENU = Janela.MENU_HEIGHT / 50f;
	public static final int DIVISOR_WIDTH_MAX = Janela.WIDTH - Tela.BORDA * 2 - 20;

	private static TimeCell timeCellHoras;
	private static TimeCell timeCellMin;
	private static TimeCell timeCellSeg;

	public static Variator sizeVar;
	public static Variator separadorVar;
	private static Variator alphaVar;

	
	private static Botao botaoSync;
	private static Botao botaoOk;
	private static Botao botaoStop;
	private static Botao botaoReset;
	
	private static final BufferedImage imgPlay = Util.carregarImg("/play.png");
	private static final BufferedImage imgPause = Util.carregarImg("/pause.png");

	public static void init() {
		timer1 = new Timer();
		timer2 = new Timer();

		timers.put(1, timer1);
		timers.put(2, timer2);

		timeCellHoras = new TimeCell(Janela.WIDTH - widthTimeCell * 3 - Tela.BORDA - div * 3, Janela.HEIGHT + Janela.MENU_HEIGHT - heightTimeCell - Tela.BORDA - div, 30, heightTimeCell, TimeCell.HORAS);
		timeCellMin = new TimeCell(Janela.WIDTH - widthTimeCell * 2 - Tela.BORDA - div * 2, Janela.HEIGHT + Janela.MENU_HEIGHT - heightTimeCell - Tela.BORDA - div, 30, heightTimeCell, TimeCell.MINUTOS);
		timeCellSeg = new TimeCell(Janela.WIDTH - widthTimeCell - Tela.BORDA - div, Janela.HEIGHT + Janela.MENU_HEIGHT - heightTimeCell - Tela.BORDA - div, 30, heightTimeCell, TimeCell.SEGUNDOS);

		initCheckBox();

		new HoverArea(xT12, yT1, widthT12, heightT12, 7).setAlphaMax(60);
		new HoverArea(xT12, yT2, widthT12, heightT12, 7).setAlphaMax(60);
		
		botaoSync = new Botao(xT12 + 10, Janela.HEIGHT + Tela.BORDA, 55, heightMT - Tela.BORDA*2, corPadrao, corPadraoEscura, "SYNC" , 9, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Principal.relogio.sync();
				
			}
		});
		
		botaoOk = new Botao(xOk, ySMH, widthTimeCell+1, heightTimeCell+1, corSel1, corPadrao, "OK", -4, new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				if (timers.get(timerSel).salvar()) {
					botaoOk.setAtivo(false);				
				}
			}
		});
		botaoOk.setFont(TimeCell.font);
		botaoOk.ha.setAlphaMax(60);
		botaoOk.setAtivo(false);
		
		botaoStop = new Botao(botaoSync.getX() + botaoSync.getWidth() + 6, botaoSync.getY(), 40, botaoSync.getHeight(), botaoSync.getCorBorda(), botaoSync.getCorFundo(), "", 9, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Principal.relogio.stopGo();
				
				if (Principal.relogio.isParado()) {
					botaoStop.setImg(imgPlay);
				} else {
					botaoStop.setImg(imgPause);
				}
			}
		});
		botaoStop.setImg(imgPause);
		
		botaoReset = new Botao(botaoStop.getX() + botaoStop.getWidth() + 6, botaoStop.getY(), 55, botaoStop.getHeight(), botaoStop.getCorBorda(), botaoStop.getCorFundo(), "RESET", 9, new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				Principal.relogio.reset();
			}
		});

		initListener();
		initVariator();

	}
	
	private static void initCheckBox() {
		especial = new CheckBox(Janela.WIDTH - 25, yT1 + 16, 10, KeyEvent.VK_E, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timers.get(timerSel).setFuncao(Timer.ESPECIAL);
			}
		});
		especial.setGrupo(1);
		suspender = new CheckBox(Janela.WIDTH - 25, yT1 + 31, 10, KeyEvent.VK_U, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timers.get(timerSel).setFuncao(Timer.SUSPENDER);
			}
		});
		suspender.setGrupo(1);
		reiniciar = new CheckBox(Janela.WIDTH - 25, yT1 + 46, 10, KeyEvent.VK_R, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timers.get(timerSel).setFuncao(Timer.REINICIAR);
			}
		});
		reiniciar.setGrupo(1);
		desligar = new CheckBox(Janela.WIDTH - 25, yT1 + 61, 10, KeyEvent.VK_D, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timers.get(timerSel).setFuncao(Timer.DESLIGAR);
			}
		});
		desligar.setGrupo(1);

		checkBoxes.add(especial);
		checkBoxes.add(suspender);
		checkBoxes.add(reiniciar);
		checkBoxes.add(desligar);
		
	}

	private static void initVariator() {
		sizeVar = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				height = (int) numero;
			}

			public double getNumero() {
				return height;
			}

			public boolean devoContinuar() {
				return true;
			}
		});

		separadorVar = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				divWidth = (int) numero;
			}

			public double getNumero() {
				return divWidth;
			}

			public boolean devoContinuar() {
				return true;
			}
		});

		alphaVar = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				alpha = (float) numero;
			}

			public double getNumero() {
				return alpha;
			}

			public boolean devoContinuar() {
				return true;
			}
		});
	}

	private static void initListener() {
		ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, new MouseListener() {
			public void acao(MouseEvent e) {

				if (e.getX() > xT12 && e.getX() < xT12 + widthT12) {
					if (e.getY() > yT1 && e.getY() < yT1 + heightT12) {
						if (timerSel != 1) {
							timers.get(timerSel).cancelar();
							timerSel = 1;

							for (int x = 0; x < checkBoxes.size(); x++) {
								checkBoxes.get(x).setCheck(false);

							}
							if (timers.get(timerSel).funcao != 0) checkBoxes.get(timers.get(timerSel).funcao - 1).setCheck(true);
						}

						return;
					}
				}

				if (e.getX() > xT12 && e.getX() < xT12 + widthT12) {
					if (e.getY() > yT2 && e.getY() < yT2 + heightT12) {
						if (timerSel != 2) {
							timers.get(timerSel).cancelar();
							timerSel = 2;

							for (int x = 0; x < checkBoxes.size(); x++) {
								checkBoxes.get(x).setCheck(false);

							}
							if (timers.get(timerSel).funcao != 0) checkBoxes.get(timers.get(timerSel).funcao - 1).setCheck(true);
						}

						return;
					}
				}

			}
		});
		
		ListenerManager.addListener(ListenerManager.KEY_PRESSED, new KeyListener() {
			public void acao(KeyEvent e) {
				
				if (e.getKeyCode() == KeyEvent.VK_ENTER && !timers.get(timerSel).isSalvo() && aberto && timers.get(timerSel).funcaoT != 0) {
					timers.get(timerSel).salvar();
				}
				
			}
		});
	}

	public static void abrir() {
		if (aberto) return;

		aberto = true;

		int espera = (int) ((Janela.MENU_HEIGHT - height) / DELAY_MENU);

		System.out.println("Começou a abrir");
		sizeVar.variar(false);
		sizeVar.clearFila();
		sizeVar.fadeInSin(height, Janela.MENU_HEIGHT, espera);
		sizeVar.variar(true);
		ani = true;
		sizeVar.addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				ani = false;
				System.out.println("abriu");
				return true;
			}
		});

		separadorVar.variar(false);
		separadorVar.fadeInSin(divWidth, DIVISOR_WIDTH_MAX, espera);
		separadorVar.variar(true);

		alphaVar.variar(false);
		alphaVar.fadeInSin(alpha, 1, espera);
		alphaVar.variar(true);
	}

	public static void fechar() {
		if (!aberto) return;

		int espera = (int) (height / DELAY_MENU);

		System.out.println("Começou a fechar");
		sizeVar.variar(false);
		sizeVar.clearFila();
		sizeVar.fadeOutSin(height, 0, espera);
		sizeVar.variar(true);
		ani = true;
		sizeVar.addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				ani = false;
				aberto = false;
				System.out.println("fechou");
				// diminuirTela();
				return true;
			}
		});

		separadorVar.variar(false);
		separadorVar.fadeOutSin(divWidth, 0, espera);
		separadorVar.variar(true);

		alphaVar.variar(false);
		alphaVar.fadeOutSin(alpha, 0, espera);
		alphaVar.variar(true);
	}

	public static void update() {
		updateBotaoOk();
		
		
	}

	private static void updateBotaoOk() {
		if (!timers.get(timerSel).isSalvo() && timers.get(timerSel).funcaoT != 0) {
			botaoOk.setAtivo(true);
		} else {
			botaoOk.setAtivo(false);
		}	
	}

	public static void pintar(Graphics2D g, int menuHeight) {
		if (alpha != 1) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

		g.setColor(Color.DARK_GRAY);
		g.drawLine(Janela.WIDTH / 2 - divWidth / 2, Janela.HEIGHT - 5, Janela.WIDTH / 2 + divWidth / 2, Janela.HEIGHT - 5);

		//g.setColor(corPadrao);
		//g.drawRoundRect(Tela.BORDA + 5, Janela.HEIGHT, 40, Janela.MENU_HEIGHT - Tela.BORDA - 5, 5, 5);

		pintarBotoesTimer(g);
		pintarTimeDisplay(g);
		//pintarBotaoOk(g);
		pintarOpcoes(g);
		
		botaoSync.pintar(g);
		botaoOk.pintar(g);
		botaoStop.pintar(g);
		botaoReset.pintar(g);

		if (alpha != 1) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

		//limpa a parte q nao aparece na animação de abre e fecha
		Composite comp = g.getComposite();
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(0, menuHeight + Janela.HEIGHT, Janela.WIDTH, Janela.HEIGHT + Janela.MENU_HEIGHT - menuHeight);
		g.setComposite(comp);
		
		

	}

	private static void pintarOpcoes(Graphics2D g) {

		g.setColor(corPadrao);
		g.drawRoundRect(xOk, yT1, xS - xOk + widthTimeCell, ySMH - yT1 - 5, 5, 5);

		g.setColor(Color.BLACK);
		g.fillRect(xOk, yT1, 17, 13);

		g.setFont(fontOpcoesG);
		g.setColor(Color.WHITE);
		g.drawString("PC", xT12 + widthT12 + 10, yT1 + Util.getStringHeight("PC", fontOpcoesG, g) - 5);

		g.setFont(fontOpcoes);
		g.drawString("Desligar", xT12 + widthT12 + 17, yT1 + ySMH - yT1 - 10);
		g.drawString("Reiniciar", xT12 + widthT12 + 17, yT1 + ySMH - yT1 - 25);
		g.drawString("Suspender", xT12 + widthT12 + 17, yT1 + ySMH - yT1 - 40);
		g.drawString("Especial", xT12 + widthT12 + 17, yT1 + ySMH - yT1 - 55);

		g.setColor(corPadraoClara);
		g.drawLine(110, yT1 + ySMH - yT1 - 10 - 5, Janela.WIDTH - 30, yT1 + ySMH - yT1 - 10 - 5);
		g.drawLine(110, yT1 + ySMH - yT1 - 25 - 5, Janela.WIDTH - 30, yT1 + ySMH - yT1 - 25 - 5);
		g.drawLine(120, yT1 + ySMH - yT1 - 40 - 5, Janela.WIDTH - 30, yT1 + ySMH - yT1 - 40 - 5);
		g.drawLine(110, yT1 + ySMH - yT1 - 55 - 5, Janela.WIDTH - 30, yT1 + ySMH - yT1 - 55 - 5);

		especial.pintar(g);
		suspender.pintar(g);
		desligar.pintar(g);
		reiniciar.pintar(g);

	}


	private static void pintarBotoesTimer(Graphics2D g) {
		g.setColor(new Color(255, 215, 0, 150));
		g.fillRoundRect(xT12, yT1, widthT12, heightT12, 5, 5);

		g.setColor(new Color(255, 215, 0, 150));
		g.fillRoundRect(xT12, yT2, widthT12, heightT12, 5, 5);

		g.setColor(Color.BLACK);
		g.setFont(fontTimerB);
		g.drawString("1", xT12 + 12, yT1 + heightT12 / 2 + Util.getStringHeight("1", fontTimerB, g) - 20);
		g.drawString("2", xT12 + 10, yT2 + heightT12 / 2 + Util.getStringHeight("2", fontTimerB, g) - 20);

		if (timerSel == 1) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRoundRect(xT12, yT2, widthT12, heightT12, 5, 5);
		} else {
			g.setColor(new Color(0, 0, 0, 150));

			g.fillRoundRect(xT12, yT1, widthT12, heightT12, 5, 5);
		}

	}

	private static void pintarTimeDisplay(Graphics2D g) {
		timeCellHoras.pintar(g);
		timeCellMin.pintar(g);
		timeCellSeg.pintar(g);
	}

}
