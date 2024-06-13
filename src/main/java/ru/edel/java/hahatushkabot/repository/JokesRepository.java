package ru.edel.java.hahatushkabot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.edel.java.hahatushkabot.model.JokesModel;

import java.util.List;

public interface JokesRepository extends JpaRepository<JokesModel, Long> {

    @Query(value = "SELECT * FROM jokes ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    JokesModel findRandomJoke();

    @Query(value = "SELECT jok \n" +
            "FROM visitors as v INNER JOIN jokes as j on v.joke_id = j.id \n" +
            "GROUP BY jok \n" +
            "ORDER BY COUNT(*) \n" +
            "DESC LIMIT 5", nativeQuery = true)
    List<String> findTopJokes();
}
