/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.Order;
import com.mycompany.myapp.entities.Velovendre;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author helmi
 */
public class ServiceOrder {
    
    
       public void ajoutTask(Order o) {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        //String Url = "http://localhost/bascla/web/app_dev.php/cart/new1?nom="+o.getLast_Name()+"?pnom="+o.getName()+"?mail="+o.getEmail()+"?num="+o.getNumber();
          String Url = "http://localhost/bascla/web/app_dev.php/cart/new1?nom="+o.getLast_Name()+"&pnom="+o.getName()+"&mail="+o.getEmail()+"&num="+o.getNumber();

        con.setUrl(Url);// Insertion de l'URL de notre demande de connexionin

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur
            System.out.println(str);//Affichage de la réponse serveur sur la console

        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }
       
    
       
          public ArrayList<Order> parseListTaskJson(String json) {

        ArrayList<Order> listTasks = new ArrayList<>();

        try {
            JSONParser j = new JSONParser();// Instanciation d'un objet JSONParser permettant le parsing du résultat json

            /*
                On doit convertir notre réponse texte en CharArray à fin de
            permettre au JSONParser de la lire et la manipuler d'ou vient 
            l'utilité de new CharArrayReader(json.toCharArray())
            
            La méthode parse json retourne une MAP<String,Object> ou String est 
            la clé principale de notre résultat.
            Dans notre cas la clé principale n'est pas définie cela ne veux pas
            dire qu'elle est manquante mais plutôt gardée à la valeur par defaut
            qui est root.
            En fait c'est la clé de l'objet qui englobe la totalité des objets 
                    c'est la clé définissant le tableau de tâches.
            */
            Map<String, Object> tasks = j.parseJSON(new CharArrayReader(json.toCharArray()));
                       
            
            /* Ici on récupère l'objet contenant notre liste dans une liste 
            d'objets json List<MAP<String,Object>> ou chaque Map est une tâche                
            */
            List<Map<String, Object>> list = (List<Map<String, Object>>) tasks.get("root");

            //Parcourir la liste des tâches Json
            for (Map<String, Object> obj : list) {
                //Création des tâches et récupération de leurs données
                Order o = new Order();
                 float Id_Order = Float.parseFloat(obj.get("Id_Order").toString());

                o.setNumber((int) Id_Order);

                float Number = Float.parseFloat(obj.get("Number").toString());

                o.setNumber((int) Number);
                o.setName(obj.get("Name").toString());
                 o.setLast_Name(obj.get("Last_Name").toString());
                o.setEmail(obj.get("Email").toString());
               
                
               

                System.out.println(o);
                
                listTasks.add(o);

            }

        } catch (IOException ex) {
        }
        
        /*
            A ce niveau on a pu récupérer une liste des tâches à partir
        de la base de données à travers un service web
        
        */
        System.out.println(listTasks);
        return listTasks;

    }
         
          ArrayList<Order> listTasks = new ArrayList<>();
    
    public ArrayList<Order> getList2(){       
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost/bascla/web/app_dev.php/cart/all1");  
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                ServiceOrder ser = new ServiceOrder();
                listTasks = ser.parseListTaskJson(new String(con.getResponseData()));
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listTasks;
    }
}
