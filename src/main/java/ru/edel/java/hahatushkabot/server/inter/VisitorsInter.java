package ru.edel.java.hahatushkabot.server.inter;

import ru.edel.java.hahatushkabot.model.JokesModel;

public interface VisitorsInter {

    void addVisitors(Long visitors_id, String action, JokesModel joke_id);
}
