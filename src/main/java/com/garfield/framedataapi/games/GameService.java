package com.garfield.framedataapi.games;

import com.garfield.framedataapi.framedata.FramedataService;
import com.garfield.framedataapi.games.exceptions.GameAlreadyExistsException;
import com.garfield.framedataapi.games.exceptions.GameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final FramedataService framedataService;

    public Game getGameByIdentifier(String identifier) throws GameNotFoundException {
        try {
            return getGameById(UUID.fromString(identifier));
        } catch (IllegalArgumentException e) {
            return getGameByName(identifier);
        }
    }

    public Game getGameById(UUID id) throws GameNotFoundException {
        Game game = this.gameRepository.getById(id);

        if (game == null) {
            throw new GameNotFoundException(id);
        }

        return game;
    }

    private Game getGameByName(String name) throws GameNotFoundException {
        Game game = this.gameRepository.getByNameIgnoreCase(name);

        if (game == null) {
            throw new GameNotFoundException(name);
        }

        return game;
    }

    public List<Game> getAllGames() {
        return this.gameRepository.findAll();
    }

    public void createGame(Game game) {
        try {
            this.getGameByIdentifier(game.getName());
            throw new GameAlreadyExistsException(game);
        } catch (GameNotFoundException e) {
            this.framedataService.validateOnlyEmptyStrings(game.getAttributesTemplate().getAttributes());
            this.gameRepository.save(game);
        }
    }

    public void deleteGameById(UUID id) {
        Game game = this.gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));

        this.gameRepository.delete(game);
    }
}
