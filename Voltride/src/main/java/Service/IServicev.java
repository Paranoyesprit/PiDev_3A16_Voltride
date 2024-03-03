package Service;

import Entities.Reservation_v;
import Entities.Voiture;

import java.util.List;

public interface IServicev <T>{
    void addV(T t);

    boolean delete(int id_v);

    //  void delete(T t);
        void update(int id_v,T t);
    List<T> readAll();

    List<Voiture> readAvailableVoitures();

    T readbyId(int id_v);

    List<Reservation_v> readAllreservation();

    List<Reservation_v> readAllForUser(int id_u);
    public List<T> readAvailableReservations();
}
