package org.example;

import gestion_service_apreslocation.entities.Participation;
import gestion_service_apreslocation.entities.ServiceApreslocation;
import gestion_service_apreslocation.service.ParticipationServices;
import gestion_service_apreslocation.service.ServiceApreslocationServices;

public class Main {

    public static void main(String[] args) {
        ServiceApreslocationServices E = new ServiceApreslocationServices();
        int typeId = 24; // Replace your_type_id_here with the actual type ID
        ServiceApreslocation service = E.readById(typeId);

        // Check if the ServiceApreslocation object is not null before creating the Participation instance
        if (service != null) {
            ParticipationServices P = new ParticipationServices();
            Participation p = new Participation(service, 3);
            P.add(p);
            //P.readAll().forEach(System.out::println);
        } else {
            System.out.println("Failed to retrieve ServiceApreslocation for type ID: " + typeId);
        }
    }
}
