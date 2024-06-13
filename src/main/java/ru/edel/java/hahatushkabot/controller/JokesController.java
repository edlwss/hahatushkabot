package ru.edel.java.hahatushkabot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edel.java.hahatushkabot.model.JokesModel;
import ru.edel.java.hahatushkabot.server.JokesService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/jokes")
@RequiredArgsConstructor
public class JokesController {

    private final JokesService jokesService;

    @PostMapping
    public ResponseEntity<String> addJokes(@RequestBody JokesModel jok){
        if(jok.getJok() == null){
            return ResponseEntity.badRequest().body("Шутка в том, что шутки нет :(");
        }
        jokesService.addJok(jok);
        return ResponseEntity.ok("Уважаемый, хахатушка добавлена!");
    }

    @GetMapping
    public ResponseEntity<List<JokesModel>> getJokes(){
        return ResponseEntity.ok(jokesService.getJokes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JokesModel> getJokesId(@PathVariable Long id) {
        return jokesService.getJokesId(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/random")
    public ResponseEntity<JokesModel> getRandomJoke(){
        return ResponseEntity.ok(jokesService.getRandomJoke());
    }

    @GetMapping("/top")
    public ResponseEntity<List<String>> getTopJokes(){
        return ResponseEntity.ok(jokesService.getTopJokes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJoke(@PathVariable Long id, @RequestBody JokesModel updatedJoke) {
        if (jokesService.updateJoke(id, updatedJoke)) {
            return ResponseEntity.ok("Уважаемые, хахатушка обновилась!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteJoke(@PathVariable Long id) {
        if (jokesService.deleteJoke(id)) {
            return ResponseEntity.ok("Мы не хотели этого, но хахатушку удалили :(");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
