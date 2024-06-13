package ru.edel.java.hahatushkabot.server;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.edel.java.hahatushkabot.model.JokesModel;
import ru.edel.java.hahatushkabot.repository.JokesRepository;
import ru.edel.java.hahatushkabot.server.inter.JokesInter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JokesService implements JokesInter {

    private final JokesRepository repository;

    @Override
    public void addJok(JokesModel jok) {
        repository.save(jok);
    }

    @Override
    public List<JokesModel> getJokes() {
        Pageable pageable = PageRequest.of(0, 15);
        Page<JokesModel> page = repository.findAll(pageable);
        return page.getContent();
    }
    @Override
    public Optional<JokesModel> getJokesId(Long id) {
        return repository.findById(id);
    }
    @Override
    public boolean updateJoke(Long id, JokesModel updatedJoke) {
        Optional<JokesModel> existingJoke = repository.findById(id);
        if (existingJoke.isPresent()) {
            JokesModel jokeToUpdate = existingJoke.get();
            jokeToUpdate.setJok(updatedJoke.getJok());
            jokeToUpdate.setUpdateDate(LocalDateTime.now());
            repository.save(jokeToUpdate);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteJoke(Long id) {
        Optional<JokesModel> existingJoke = repository.findById(id);
        if (existingJoke.isPresent()) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public JokesModel getRandomJoke() {
        return repository.findRandomJoke();
    }

    public List<String> getTopJokes() {
        return repository.findTopJokes();
    }
}
