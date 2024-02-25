package org.example;

import gestion_service_apreslocation.entities.ServiceApreslocation;
import gestion_service_apreslocation.entities.Type;
import gestion_service_apreslocation.service.ServiceApreslocationServices;
import utils.DataSource;


public class Main {
    public static void main(String[] args) {
        DataSource ds1 = DataSource.getInstance();
        System.out.println(ds1);

        ServiceApreslocationServices ps= new ServiceApreslocationServices();
        Type type = new Type("aa");
        ServiceApreslocation l1 = new ServiceApreslocation(type,
                "yassine" , "blablabla","on or off",250.5,1,1
        );
        ps.add(l1);
      // ps.readAll().forEach(System.out::println);
      // l1.setCout(300);
       //ps.update(1,l1);
      //  ps.delete(3);

}}