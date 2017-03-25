/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author joselm
 */
public class Momento {
    private final Date moment;
    
    public Momento() {
        moment = new Date();
    }
    
    public Momento(Date d){
        moment = d;
    }
    
    public Momento(GregorianCalendar gc){
        moment = gc.getGregorianChange();
    }
    
    @Override
    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(moment);
    }
    
    public Date toDate() {
        return (Date)moment.clone();
    }
}
