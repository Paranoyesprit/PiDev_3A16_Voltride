package Main;

import Entities.Reservation_v;
import Entities.Voiture;
import Service.ReservationVService;
import Service.VoitureService;
import util.DataSource;

import java.time.LocalDate;


public class Main {


    public static void main(String[] args)
    {
       // Voiture v1=new Voiture("BMW","M5","active",6,55000);
        VoitureService vs=new VoitureService();
      // vs.add(v1);
       // Voiture v2=new Voiture("PORSCHE","911","active",10);
      // vs.addV(v2);
       // vs.readAll().forEach(System.out::println);
        //vs.delete(v1);
       // Voiture v3=new Voiture("RENAULT","CLIO","active",2,10000);
       // vs.add(v3);
        //vs.readAll().forEach(System.out::println);
       // vs.delete(5);
        //vs.update(v3);
        //vs.readAll().forEach(System.out::println);
       // Voiture v=new Voiture("NISSAN","SKYLINE","active",8,1000);
        //vs.update(1,v);
      // Voiture x= vs.readbyId(4);
        //System.out.println(x);
       // Voiture v6=new Voiture("MAZDA","3","active",6,150000);
     //   vs.addV(v6);
        //vs.readAll().forEach(System.out::println);
        //vs.delete(9);
       // vs.update(7,v);
        ReservationVService ReservationVService = new ReservationVService();

        // Creating a reservation_b object (replace with actual values)
        Reservation_v reservation = new Reservation_v();
        Voiture myVoiture = new Voiture();
        myVoiture.setMarque("NISSAN");
        reservation.setVoiture(myVoiture);
        LocalDate date_debut=LocalDate.of(2024,5,5);
        reservation.setDate_debut(date_debut);
        LocalDate date_fin=LocalDate.of(2024,5,10);
        reservation.setDate_fin(date_fin);
       //ReservationVService.addV(reservation);
      //  ReservationVService.delete(2);
       // vs.delete(1);
// Update the reservation with id_r = 1 (example)

        Reservation_v updatedReservation = new Reservation_v();
        updatedReservation.setDate_debut(LocalDate.of(2024, 2, 18));
        updatedReservation.setDate_fin(LocalDate.of(2024, 2, 21));
        //ReservationVService.update(1, updatedReservation);
        ReservationVService.readAll().forEach(System.out::println);
    }
}
