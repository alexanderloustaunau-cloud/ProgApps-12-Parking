package Clases;

public class Plaza {
private int numero;
private boolean ocupada;
private Coche coche;



public Plaza(int numero, boolean ocupada, Coche coche) {
	super();
	this.numero = numero;
	this.ocupada = ocupada;
	this.coche = coche;
}
public int getNumero() {
	return numero;
}
public void setNumero(int numero) {
	this.numero = numero;
}
public boolean isOcupada() {
	return ocupada;
}
public void setOcupada(boolean ocupada) {
	this.ocupada = ocupada;
}
public Coche getCoche() {
	return coche;
}
public void setCoche(Coche coche) {
	this.coche = coche;
}
@Override
public String toString() {
	return "Plaza [numero=" + numero + ", ocupada=" + ocupada + ", coche=" + coche + "]";
}
public void ocupar(Coche coche) {
	this.coche=coche;
	this.ocupada=true;
}
public void liberar() {
	this.coche = null;
	this.ocupada=false;
}


}

