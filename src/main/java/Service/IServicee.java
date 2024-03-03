package Service;

import Entities.Evenement;
import Entities.Reservation_e;

import java.time.LocalDate;
import java.util.List;

public interface IServicee <T>{
    void add(T t);

    void delete(int id_e);

    //  void delete(T t);
    void update(int id_e,T t);
    List<T> readAll();

    List<Evenement> readAvailableEvenement();

    T readbyId(int id_e);
    public List<T> readAvailableReservations();

    List<Reservation_e> readAllForUser(int id_u);
}
