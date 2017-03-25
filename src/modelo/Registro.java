/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author ycgc
 */
public class Registro extends Observable{
    
    private String nombre;
    private Momento fechaInicio;
    private ArrayList<Temperatura> listaTemperaturas;
    private HashMap<Momento,Temperatura> mapa;
    private Temperatura temperaturaMinima, temperaturaMaxima;
    private final DataBaseInterface db;
    
    private static Registro registro;

    private Registro(String nombre, Momento fechaInicio) {
        db = new DataBaseInterface();
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        listaTemperaturas = new ArrayList<>();
        mapa = new HashMap<Momento, Temperatura>();
        
        loadTemperatures();
    }
    
    public static Registro getRegistro(){
        if(registro == null){
            registro = new Registro("El único", new Momento());
        }
        
        return registro;
    }

    public String getNombre() {
        return nombre;
    }

    public Momento getFechaInicio() {
        return fechaInicio;
    }
     
    protected void setNombre(String nombre) {
        this.nombre = nombre;
    }

    protected void setFechaInicio(Momento fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public ArrayList<Temperatura> getRegistroTemperaturasAsList() {
        return  (ArrayList<Temperatura>) listaTemperaturas.clone();
    }

    public HashMap<Date, Temperatura> getRegistroTemperaturaAsMap() {
        return (HashMap<Date, Temperatura>) mapa.clone();
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
    
    public HashMap<Momento, Temperatura> getRegisterData(){
        return mapa;
    }
    
    public void addTemperatura(Temperatura nuevaTemperatura, Momento moment){
        if (listaTemperaturas.isEmpty()) {
            temperaturaMinima = nuevaTemperatura;
            temperaturaMaxima = nuevaTemperatura;
        }
        
        listaTemperaturas.add(nuevaTemperatura);
        updateTemperature(moment, nuevaTemperatura.getValor());
        
        mapa.put(moment, nuevaTemperatura);
        
        if (nuevaTemperatura.getValor() < temperaturaMinima.getValor()) {
            temperaturaMinima = nuevaTemperatura;
        }
        
        if (nuevaTemperatura.getValor() > temperaturaMaxima.getValor()) {
            temperaturaMaxima = nuevaTemperatura;
        }
        setChanged();
        if(countObservers() != 0)
            synchronized(this){
                notifyObservers();
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
        setChanged();
        if(countObservers() != 0)
            synchronized(this){
                notifyObservers();
            }
    }
    
    private void updateTemperature(Momento moment,float temp){
        try {
            db.addTemperature(moment, temp);
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }    

}
