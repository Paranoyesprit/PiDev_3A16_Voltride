package Service;

import Entities.borne;

import java.util.List;

    public interface IServiceb<T>{

    void addpst(T t);
    void delete(int id);
    void update(int id,T t);
    List<T> readAll();
    List<T> readAllForUser(int id_u);

    List<borne> readAvailableBornes();

    T readById(int id);


}
