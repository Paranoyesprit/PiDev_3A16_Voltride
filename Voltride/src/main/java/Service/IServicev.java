package Service;

import Entities.Reservation_v;
import Entities.Voiture;

import java.util.List;

public interface IServicev <T>{
    void addV(T t);

    void delete(int id_v);

    //  void delete(T t);
        void update(int id_v,T t);
    List<T> readAll();

    List<Voiture> readAvailableVoitures();

    T readbyId(int id_v);
    public List<T> readAvailableReservations();
}
