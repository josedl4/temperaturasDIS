/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazUsuario;

import java.time.LocalDate;
import modelo.Registro;
import modelo.Temperatura;

/**
 *
 * @author ycgc
 */
public class ControladorVistaRegistro {

    private VistaRegistro vista;
    private float t;
    

    public ControladorVistaRegistro(VistaRegistro vista) {
        this.vista = vista;
    }

    public void comprobarTemperatura(String temp, boolean isCelsius) {
     
        try {
            t = Float.parseFloat(temp);
            if(!isCelsius){
                t = Temperatura.convertFahrenheitToCelsius(t);
            }
            vista.muestraConfirmacion();

        } catch (NumberFormatException e) {
            System.err.println("Error: " + e.getMessage());
            vista.muestraError();
        }
    }

    public void guardarTemperatura() { 
        Temperatura temp = new Temperatura(t);
        
        Registro.getRegistro().addTemperatura(temp, LocalDate.now());
        vista.cerrarConfirmacion();
        vista.muestraTemperaturas();
    }

    public void noGuardarTemperatura() {
        vista.cerrarConfirmacion();
    }

}
