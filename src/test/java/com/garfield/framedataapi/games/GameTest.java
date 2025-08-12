package com.garfield.framedataapi.games;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void allArgsConstructor_shouldSetName() {
        Game game = new Game("Tekken");

        assertEquals("Tekken", game.getName());
        assertNull(game.getId());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyGame() {
        Game game = new Game();

        assertNull(game.getId());
        assertNull(game.getName());
    }

    @Test
    void settersShouldNotExistBecauseLombokGetterOnly() {
        assertThrows(NoSuchMethodException.class, () -> Game.class.getDeclaredMethod("setName", String.class));
    }

    @Test
    void idFieldCanBeSetViaReflectionForTesting() throws Exception {
        Game game = new Game("Test Game");
        UUID id = UUID.randomUUID();
        var field = Game.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(game, id);

        assertEquals(id, game.getId());
    }

    @Nested
    @DataJpaTest
    class GamePersistenceTests {

        @Autowired
        private GamesRepository gamesRepository;

        @Test
        void saveAndRetrieveGame_shouldPersistCorrectly() {
            Game game = new Game("Tekken");
            Game saved = gamesRepository.save(game);

            assertNotNull(saved.getId());
            assertEquals("Tekken", saved.getName());

            Game retrieved = gamesRepository.getById(saved.getId());
            assertEquals(saved.getId(), retrieved.getId());
            assertEquals("Tekken", retrieved.getName());
        }
    }

}