package self.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import self.input.KeyListener;
import self.input.ListenerManager;
import self.input.MouseListener;
import self.input.MouseWheelListener;
import self.menu.Menu;
import self.util.Util;

public class TimeCell {
	private int x;
	private int y;
	private int width;
	private int height;

	private boolean sel = false;

	private int tipo;

	private HoverArea ha;

	public static final int HORAS = 0;
	public static final int SEGUNDOS = 1;
	public static final int MINUTOS = 2;

	public static Font font = new Font("DS-Digital", Font.PLAIN, 25);

	public TimeCell(int x, int y, int width, int height, final int tipo) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tipo = tipo;

		ha = new HoverArea(x + 1, y + 1, width - 1, height - 1, 5);
		ha.setAlphaMax(80);

		initListeners();

	}

	private void initListeners() {
		ListenerManager.addListener(e -> {
			if (!sel) return;
			switch (tipo) {
				case HORAS:
					Menu.timers.get(Menu.timerSel).rodaHora(e.getWheelRotation());
					break;

				case MINUTOS:
					Menu.timers.get(Menu.timerSel).rodaMin(e.getWheelRotation());
					break;

				case SEGUNDOS:
					Menu.timers.get(Menu.timerSel).rodaSeg(e.getWheelRotation());
					break;

				default:
					System.err.println("Time sel com tipo errado");
					break;
			}

		});

		ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, (MouseListener) e -> {
			if (e.getX() > x && e.getX() < x + width) {
				if (e.getY() > y && e.getY() < y + height) {
					if (!sel) sel = true;
				} else {
					sel = false;
				}
			} else {
				sel = false;
			}

		});
		
		ListenerManager.addListener(ListenerManager.KEY_PRESSED, (KeyListener) e -> {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				sel = false;
			}

			if (e.getKeyCode() == KeyEvent.VK_H && tipo == HORAS) {
				sel = true;

			} else 	if (e.getKeyCode() == KeyEvent.VK_M && tipo == MINUTOS) {
				sel = true;

			} else 	if (e.getKeyCode() == KeyEvent.VK_S && tipo == SEGUNDOS) {
				sel = true;

			} else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_M || e.getKeyCode() == KeyEvent.VK_H){
				sel = false;
			}
		});

		ListenerManager.addListener(ListenerManager.KEY_TYPED, (KeyListener) e -> {
			if (!sel) return;

			String key = "";
			char keyc = e.getKeyChar();


			try {
				int keyi = Integer.parseInt(Character.toString(keyc));

				if (keyi < 0 || keyi > 9) return;
				key = keyi + "";
			} catch (Exception ignored) {}


			if (key.equals("")) return;

			if (tipo == HORAS) {
				char pnum = Menu.timers.get(Menu.timerSel).getHorasTemp().charAt(1);
				if (Integer.parseInt(Character.toString(pnum)) > 2) pnum = '2';
				Menu.timers.get(Menu.timerSel).setHoras(Integer.parseInt(pnum + key));
				System.out.println(key);
			}

			if (tipo == MINUTOS) {
				char pnum = Menu.timers.get(Menu.timerSel).getMinTemp().charAt(1);
				if (Integer.parseInt(Character.toString(pnum)) > 5) pnum = '5';
				Menu.timers.get(Menu.timerSel).setMin(Integer.parseInt(pnum + key));
			}

			if (tipo == SEGUNDOS) {
				char pnum = Menu.timers.get(Menu.timerSel).getSegTemp().charAt(1);
				if (Integer.parseInt(Character.toString(pnum)) > 5) pnum = '5';
				Menu.timers.get(Menu.timerSel).setSeg(Integer.parseInt(pnum + key));
			}

		});

	}

	public int getX() {
		return x;
	}

	public void pintar(Graphics2D g) {
		if (sel) {
			g.setColor(Menu.corSel1);
		} else {
			g.setColor(Color.BLACK);
		}
		g.fillRoundRect(x, y, width, height, 4, 4);

		if (sel) {
			g.setColor(Menu.corSel);
		} else {
			g.setColor(Menu.corPadrao);
		}
		g.drawRoundRect(x, y, width, height, 4, 4);

		g.setFont(font);

		if (sel) {
			g.setColor(Color.BLACK);
		} else {
			g.setColor(Color.WHITE);
		}

		if (tipo == HORAS) g.drawString(Menu.timers.get(Menu.timerSel).getHorasTemp(), x - Util.getStringWidh(Menu.timers.get(Menu.timerSel).getHorasTemp(), font, g) / 2 + 14, y + 20);
		if (tipo == MINUTOS) g.drawString(Menu.timers.get(Menu.timerSel).getMinTemp(), x - Util.getStringWidh(Menu.timers.get(Menu.timerSel).getMinTemp(), font, g) / 2 + 14, y + 20);
		if (tipo == SEGUNDOS) g.drawString(Menu.timers.get(Menu.timerSel).getSegTemp(), x - Util.getStringWidh(Menu.timers.get(Menu.timerSel).getSegTemp(), font, g) / 2 + 14, y + 20);
	}
}
