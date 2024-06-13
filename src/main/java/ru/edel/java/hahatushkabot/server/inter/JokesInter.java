package ru.edel.java.hahatushkabot.server.inter;
import ru.edel.java.hahatushkabot.model.JokesModel;

import java.util.List;
import java.util.Optional;

public interface JokesInter {

    void addJok(JokesModel jok);

    List<JokesModel> getJokes();
    Optional<JokesModel> getJokesId(Long id);

    boolean updateJoke(Long id, JokesModel updatedJoke);
    boolean deleteJoke(Long id);

}
