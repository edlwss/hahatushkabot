package ru.edel.java.hahatushkabot.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edel.java.hahatushkabot.model.JokesModel;
import ru.edel.java.hahatushkabot.model.Visitors;
import ru.edel.java.hahatushkabot.repository.VisitorsRepository;
import ru.edel.java.hahatushkabot.server.inter.VisitorsInter;


@RequiredArgsConstructor
@Service
public class VisitorsService implements VisitorsInter {

    private final VisitorsRepository repository;

    @Override
    public void addVisitors(Long visitors_id, String action, JokesModel joke_id){
        Visitors visitors = new Visitors();
        visitors.setVisitorId(visitors_id);
        visitors.setAction(action);
        visitors.setJoke(joke_id);
        repository.save(visitors);
    }

}
