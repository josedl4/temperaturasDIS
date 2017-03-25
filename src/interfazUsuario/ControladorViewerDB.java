/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazUsuario;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import modelo.Momento;
import modelo.Registro;
import modelo.Temperatura;

/**
 *
 * @author joselm
 */
public class ControladorViewerDB implements Observer{
    
    private Observable subject;
    private VistaViewerDB view;
    
    public ControladorViewerDB(VistaViewerDB view){
        this.view = view;
        subject = Registro.getRegistro();
        Registro.getRegistro().addObserver(this);
        updateTableData(((Registro)subject).getRegisterData());
    }
    
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("I have been notified!!");
        updateTableData(((Registro)subject).getRegisterData());
    }
    
    private void updateTableData(HashMap<Momento,Temperatura> hm){
        Object[][] result = new Object[hm.size()][3];
        HashMap<Momento,Temperatura> copy = (HashMap<Momento,Temperatura>) hm.clone();
        Iterator it = copy.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Object[] tmp = {i, pair.getValue(), pair.getKey()};
            result [i] = tmp;
            it.remove(); // avoids a ConcurrentModificationException
            i ++;
        }
        view.changeTableValues(result);
    }
    
}
