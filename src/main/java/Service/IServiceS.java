package Service;

import java.util.List;

public interface IServiceS<T> {

    void add(T t);

    void delete(int i);

    void update(int i,T t);

    List<T> readAll();

    T readById(int id);
}
