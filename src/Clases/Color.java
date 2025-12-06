package Clases;

public enum Color {
    BLANCO("Blanco"),
    NEGRO("Negro"),
    AZUL("Azul"),
    ROJO("Rojo"),
    VERDE("Verde"),
    AMARILLO("Amarillo"),
    GRIS("Gris"),
    MARRON("Marrón"),
    OTROS("Otros");

    private final String textoVisible;

    Color(String textoVisible) {
        this.textoVisible = textoVisible;
    }

    @Override
    public String toString() {
        return textoVisible;
    }
}
