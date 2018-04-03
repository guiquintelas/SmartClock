package self.principal;

import java.io.IOException;
import java.time.LocalDateTime;

import self.menu.Menu;

public class Relogio {
	
	private int horas = 0;
	private int min = 0;
	private int seg = 0;
	
	private boolean isParado = false;
	
	public static int tickTacks = 1;
	
	public void init() {
		
	}
	
	public void update() {
		if (isParado) return;
		
		tickTacks++;
		if (tickTacks%50 == 0) {
			passarTempo();
			if (tickTacks > 200)checaTimer();

		} 	
	}
	
	public void reset() {
		horas = 0;
		min = 0;
		seg = 0;
		tickTacks = 1;
	}
	
	private void checaTimer() {
		for (int x = 1; x < 3; x ++) {
			Timer timerAtual = Menu.timers.get(x);
			if (timerAtual.getHoras() == horas && 
				timerAtual.getMin() == min && 
				timerAtual.getSeg() == seg) {
				
				try {
					timerAtual.executar();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private void passarTempo() {
		seg++;
		
		if (seg == 60) {
			min++;
			seg = 0;
		}
		
		if (min == 60) {
			horas++;
			min = 0;
		}
		
		if (horas == 24) horas = 0;
		
	}

	public String getTempo() {
		String tempo = "";
		
		if (horas == 0) {
			tempo = "00";
		} else if (horas < 10) {
			tempo = "0" + horas;
		} else {
			tempo = Integer.toString(horas);
		}
		
		if (min == 0) {
			tempo = tempo.concat(":00");
		} else if (min < 10) {
			tempo = tempo.concat(":0" + min);
		} else {
			tempo = tempo.concat(":" + min);
		}
		
		if (seg == 0) {
			tempo = tempo.concat(":00");
		} else if (seg < 10) {
			tempo = tempo.concat(":0" + seg);
		} else {
			tempo = tempo.concat(":" + seg);
		}
		
		return tempo;
	}

	public void sync() {
		horas = LocalDateTime.now().getHour();
		min = LocalDateTime.now().getMinute();
		seg = LocalDateTime.now().getSecond();
		
	}
	
	public void stopGo() {
		isParado = !isParado;
	}
	
	public boolean isParado() {
		return isParado;
	}

}
