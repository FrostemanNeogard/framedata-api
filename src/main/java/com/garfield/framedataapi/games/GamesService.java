package com.garfield.framedataapi.games;

import com.garfield.framedataapi.games.exceptions.GameAlreadyExistsException;
import com.garfield.framedataapi.games.exceptions.GameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GamesService {

    private final GamesRepository gamesRepo;

    public Game getGameByIdentifier(UUID id) throws GameNotFoundException {
        Game game = this.gamesRepo.getById(id);

        if (game == null) {
            throw new GameNotFoundException(id);
        }

        return game;
    }

    public Game getGameByIdentifier(String name) throws GameNotFoundException {
        Game game = this.gamesRepo.getByName(name);

        if (game == null) {
            throw new GameNotFoundException(name);
        }

        return game;
    }

    public List<Game> getAllGames() {
        return this.gamesRepo.findAll();
    }

    public void createGame(Game game) {
        try {
            this.getGameByIdentifier(game.getName());
            throw new GameAlreadyExistsException(game);
        } catch (GameNotFoundException e) {
            this.gamesRepo.save(game);
        }
    }

    public void deleteGameById(UUID id) {
        Game game = this.gamesRepo.findById(id).orElseThrow(() -> new GameNotFoundException(id));

        this.gamesRepo.delete(game);
    }
}
