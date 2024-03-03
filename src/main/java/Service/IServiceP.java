package Service;
import Entities.Participation;

import java.util.List;




public interface IServiceP<P> {
    void add(Participation p);

    void delete(int idp);

    void update(int idp, Participation participation);

    List<Participation> readAll();

    Participation readById(int idp);

    void addParticipation(int idp);
}