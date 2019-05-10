package self.principal;

import java.awt.Toolkit;
import java.io.IOException;

import self.especial.Especial;

public class Timer {
	private int horas = 0;
	private int horasT = 0;
	private int min = 0;
	private int minT = 0;
	private int seg = 0;
	private int segT = 0;
	
	public int funcao = 0;
	public int funcaoT = 0;
	
	private boolean salvo = true;
	
	public static final int DESLIGAR = 4;
	public static final int REINICIAR = 3;
	public static final int SUSPENDER = 2;
	public static final int ESPECIAL = 1;
	
	public Timer() {
		
	}
	
	public Timer(int h, int m, int s) {
		horas = h;
		horasT = h;
		min = m;
		minT = m;
		seg = s;
		segT = s;

		funcao = DESLIGAR;
	}
	
	int getHoras() {
		return horas;
	}
	
	int getMin() {
		return min;
	}
	
	int getSeg() {
		return seg;
	}
	
	public String getHorasTemp() {
		if (horasT < 10) {
			return "0" + horasT;
		} else {
			return horasT + "";
		}
	}
	public void setHoras(int horas) {
		if (horas > 23) horas = 23;
		if (horas < 0) horas = 0;
		this.horasT = horas;
		salvo = false;
	}
	public String getMinTemp() {
		if (minT < 10) {
			return "0" + minT;
		} else {
			return minT + "";
		}
	}
	public void setMin(int min) {
		if (min > 59) min = 59;
		if (min < 0) min = 0;
		this.minT = min;
		salvo = false;
	}
	public String getSegTemp() {
		if (segT < 10) {
			return "0" + segT;
		} else {
			return segT + "";
		}
	}
	
	public void setSeg(int seg) {
		if (seg > 59) seg = 59;
		if (seg < 0) seg = 0;
		this.segT = seg;
		salvo = false;
	}
	
	public void rodaSeg(int wheel) {
		segT += wheel;
		salvo = false;
		if (segT > 59) segT = 0;
		if (segT < 0) segT = 59;
	}
	
	public void rodaMin(int wheel) {
		minT += wheel;
		salvo = false;
		if (minT > 59) minT = 0;
		if (minT < 0) minT = 59;
	}
	
	public void rodaHora(int wheel) {
		horasT += wheel;
		salvo = false;
		if (horasT > 23) horasT = 0;
		if (horasT < 0) horasT = 23;
	}
	
	public void setFuncao(int fun) {
		this.funcaoT = fun;
		salvo = false;
	}
	
	void executar() throws IOException {
		switch (funcao) {
		case DESLIGAR:
			Runtime.getRuntime().exec("shutdown -s -t 0");
			break;
			
		case ESPECIAL:
			Toolkit.getDefaultToolkit().beep();
			Especial.iniciar();
			break;
			
		case REINICIAR:
			Runtime.getRuntime().exec("shutdown -r -t 0");
			break;
			
		case SUSPENDER:
			Runtime.getRuntime().exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
			break;

		default:
			System.err.println("valor fora do esperado 0 ou > 4");
			break;
		}
	}
	
	public boolean isSalvo() {
		return salvo;
	}
	
	public boolean salvar() {
		if (funcaoT == 0) return false;
		
		horas = horasT;
		min = minT;
		seg = segT;
		funcao = funcaoT;
	
		salvo = true;
		
		return true;
	}
	
	public void cancelar() {
		horasT = horas;
		minT = min;
		segT = seg;
		funcaoT = funcao;
		
		salvo = true;
	}
	
	
}
