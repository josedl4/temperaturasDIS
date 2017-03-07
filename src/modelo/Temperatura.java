/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author ycgc
 */
public class Temperatura {
    
    private float valor;

    public Temperatura(float valor) {
        this.valor = valor;
    }

    public float getValor() {
        return valor;
    }
    
    @Override
    public String toString() {
        return getValor() + "ºC";
    }
    
    public static float convertFahrenheitToCelsius(float f) {
        return ((f-32) * 5 / 9);
    }
    
}
