package Clases;

import java.util.ArrayList;
import java.util.List;

public class Coche {
	private String matricula;
	private String marca;
	private String modelo;
	private Enum<Color> color;
	private List<Reserva> reservas = new ArrayList<>();    
	
	
	public Coche(String matricula, String marca, String modelo,Clases.Color color) {
		super();
		this.matricula = matricula;
		this.marca = marca;
		this.modelo = modelo;
		this.color = color;
	}

	
	public List<Reserva> getReservas() {                  
	    return reservas;
	}

	public void addReserva(Reserva r) {                    
	    if (r != null) reservas.add(r);
	}
	
	public String getMatricula() {
		return matricula;
	}


	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}


	public String getMarca() {
		return marca;
	}


	public void setMarca(String marca) {
		this.marca = marca;
	}


	public String getModelo() {
		return modelo;
	}


	public void setModelo(String modelo) {
		this.modelo = modelo;
	}


	public Enum<Color> getColor() {
		return color;
	}


	public void setColor(Enum<Color> color) {
		this.color = color;
	}


	@Override
	public String toString() {
		return "Coche [matricula=" + matricula + ", marca=" + marca + ", modelo=" + modelo + ", color=" + color + "]";
	}


	public Object getPlazaAsignada() {
		// TODO Auto-generated method stub.
		return null;
	}


	public Object getInicioReserva() {
		// TODO Auto-generated method stub
		return null;
	}


	public Object getEstado() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
