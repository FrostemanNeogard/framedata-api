package com.garfield.framedataapi.games;

import com.garfield.framedataapi.games.exceptions.GameAlreadyExistsException;
import com.garfield.framedataapi.games.exceptions.GameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GamesService {
    private final GamesRepository gamesRepo;

    public GamesService(GamesRepository gamesRepo) {
        this.gamesRepo = gamesRepo;
    }

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

}
