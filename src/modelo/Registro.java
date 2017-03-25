/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ycgc
 */
public class Registro {
    
    private String nombre;
    private LocalDate fechaInicio;
    private ArrayList<Temperatura> listaTemperaturas;
    private HashMap<LocalDate,Temperatura> mapa;
    private Temperatura temperaturaMinima, temperaturaMaxima;
    private DataBaseInterface db;
    
    private static Registro registro;

    private Registro(String nombre, LocalDate fechaInicio) {
        db = new DataBaseInterface();
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        listaTemperaturas = new ArrayList<>();
        mapa = new HashMap<>();
        
        loadTemperatures();
    }
    
    public static Registro getRegistro(){
        if(registro == null){
            registro = new Registro("El único", LocalDate.now());
        }
        
        return registro;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
     
    protected void setNombre(String nombre) {
        this.nombre = nombre;
    }

    protected void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public ArrayList<Temperatura> getRegistroTemperaturasAsList() {
        return  (ArrayList<Temperatura>) listaTemperaturas.clone();
    }

    public HashMap<LocalDate, Temperatura> getRegistroTemperaturaAsMap() {
        return (HashMap<LocalDate, Temperatura>) mapa.clone();
    }

    public Temperatura getTemperaturaMedia() {
        Temperatura temperaturaMedia = null;
        
        float sumaValores = 0;
         
        for (Temperatura t: listaTemperaturas) {
            sumaValores += t.getValor();
        }
        
        if (!listaTemperaturas.isEmpty()) {
            temperaturaMedia = new Temperatura(sumaValores/listaTemperaturas.size());
        }
        
        return temperaturaMedia;
    }

    public Temperatura getTemperaturaMinima() {
        return temperaturaMinima;
    }

    public Temperatura getTemperaturaMaxima() {
        return temperaturaMaxima;
    }
    
    public void addTemperatura(Temperatura nuevaTemperatura, LocalDate momento){
        if (listaTemperaturas.isEmpty()) {
            temperaturaMinima = nuevaTemperatura;
            temperaturaMaxima = nuevaTemperatura;
        }
        
        listaTemperaturas.add(nuevaTemperatura);
        updateTemperature(momento, nuevaTemperatura.getValor());
        
        mapa.put(momento, nuevaTemperatura);
        
        if (nuevaTemperatura.getValor() < temperaturaMinima.getValor()) {
            temperaturaMinima = nuevaTemperatura;
        }
        
        if (nuevaTemperatura.getValor() > temperaturaMaxima.getValor()) {
            temperaturaMaxima = nuevaTemperatura;
        }
    }
    
    private void loadTemperatures(){
        try {
            mapa = db.getData();
            listaTemperaturas = new ArrayList<Temperatura>(mapa.values());
            
            if(!listaTemperaturas.isEmpty()){
                temperaturaMaxima = listaTemperaturas.get(0);
                temperaturaMinima = listaTemperaturas.get(0);
                for (Temperatura t : listaTemperaturas){
                    if(t.getValor() < temperaturaMinima.getValor())
                        temperaturaMinima = t;
                    if(t.getValor() > temperaturaMaxima.getValor())
                        temperaturaMaxima = t;
                }
            }
                  
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    private void updateTemperature(LocalDate ld,float temp){
        try {
            db.addTemperature(ld, temp);
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
}
