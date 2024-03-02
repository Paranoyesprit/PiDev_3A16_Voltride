package Service;

import Entities.Admin;

import java.util.List;

public interface IService <T>{
    void add(T t);

    void delete(int id_u);

    //  void delete(T t);
        void update(int id_u,T t);


    List<T> readAll();


    T readbyId(int id_u);
}
