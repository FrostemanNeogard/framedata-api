import dotenv from "dotenv";
import charactercodes from "./backups/charactercodes.json" with { type: "json" };
import tekken6 from "./backups/tekken6.json" with { type: "json" };
import tekken7 from "./backups/tekken7.json" with { type: "json" };
import tekken8 from "./backups/tekken8.json" with { type: "json" };
import tekkentag2 from "./backups/tekkentag2.json" with { type: "json" };

dotenv.config();

type GameCharacter = {
  game: Game;
  code: string;
  aliases: string[];
  moves: Object[];
};
type Game = {
  filepath: any;
  code: string;
  name: string;
};
type GameWithCharacters = {
  game: Game;
  characters: GameCharacter[];
};

const framedataTemplate = {
  name: "",
  input: "",
  damage: "",
  startup: "",
  block: "",
  hit: "",
  counter: "",
  notes: [],
  hitLevel: "",
};

const BASE_ENDPOINT = "/api/v1/";
const BASE_API_URL = `http://localhost:8080${BASE_ENDPOINT}`;

const AUTH_JWT = process.env.AUTH_JWT;

const games: Game[] = [
  { filepath: tekken6, code: "tekken6", name: "Tekken 6" },
  { filepath: tekken7, code: "tekken7", name: "Tekken 7" },
  { filepath: tekken8, code: "tekken8", name: "Tekken 8" },
  { filepath: tekkentag2, code: "tekkentag2", name: "Tekken Tag Tournament 2" },
];

async function migrate() {
  const gamesWithCharacters: GameWithCharacters[] = games.map((g) => ({
    game: g,
    characters: getAllCharactersFromJsonFile(g),
  }));

  for (let i = 0; i < gamesWithCharacters.length; i++) {
    await migrateGameWithCharacters(gamesWithCharacters[i]);
  }
}

async function migrateGameWithCharacters(
  gameWithCharacters: GameWithCharacters,
) {
  for (const character of gameWithCharacters.characters) {
    const characterId = await getCharacterId(character);

    console.log(`Migrating aliases: ${character.aliases}`);
    if (!character.aliases) {
      console.log(
        `\n\n\nMISSING CHARACTER ALIASES FOR: ${character.code}\n\n\n`,
      );
    }

    if (character.aliases) {
      for (const alias of character.aliases) {
        await createAlias(characterId, alias);
      }
    }

    for (const move of character.moves) {
      const requestBody = {
        identity: {
          identifiers: Array.from(
            new Set(
              [...move.alternateInputs, move.name, move.input].filter(
                (e) => !!e,
              ),
            ).values(),
          ),
          categories: move.categories,
        },
        data: {
          attributes: {
            name: move.name,
            input: move.input,
            damage: move.damage,
            startup: move.startup,
            block: move.block,
            hit: move.hit,
            counter: move.counter,
            notes: move.notes,
            hitLevel: move.hitLevel,
          },
        },
      };

      const createFramedataResponse = await fetch(
        `${BASE_API_URL}framedata/character/${characterId}`,
        {
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
            Authorization: `Bearer ${AUTH_JWT}`,
          },
          body: JSON.stringify(requestBody),
        },
      );

      if (createFramedataResponse.status != 201) {
        const response = await createFramedataResponse.json();
        console.log(
          `An error ocurred when attempting to create framedata: ${characterId}, ${gameWithCharacters.game.name}, ${move.input}. "${createFramedataResponse.status} ${response.error}"`,
        );
        continue;
      }

      console.log(`Created framedata: ${move.input}`);
    }
  }
}

async function createAlias(characterId: string, alias: string) {
  console.log(`Creating alias "${alias}"`);

  const requestBody = {
    characterId: characterId,
    aliasName: alias,
  };

  const createAliasResponse = await fetch(`${BASE_API_URL}aliases`, {
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: `Bearer ${AUTH_JWT}`,
    },
    body: JSON.stringify(requestBody),
  });

  if (createAliasResponse.status != 201) {
    console.log(
      `An error ocurred when attempting to create alias: ${alias}. "${createAliasResponse.status}"`,
    );
    return;
  }

  console.log(`Created alias: ${alias}`);
}

async function createGameIfDoesntExist(game: Game) {
  const existingGameResponse = await fetch(
    `${BASE_API_URL}games/identifier/${game.name}`,
  );

  if (existingGameResponse.status == 200) {
    return (await existingGameResponse.json()).data.id;
  }

  const requestBody = {
    name: game.name,
    attributesTemplate: framedataTemplate,
  };

  const createGameResponse = await fetch(`${BASE_API_URL}games`, {
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: `Bearer ${AUTH_JWT}`,
    },
    body: JSON.stringify(requestBody),
  });

  if (createGameResponse.status != 201) {
    console.log(
      `An error ocurred when attempting to create game: ${game.name}. "${createGameResponse.status} ${createGameResponse.statusText}"`,
    );
    return null;
  }

  const gameId = createGameResponse.headers
    .get("Location")
    ?.substring(`${BASE_ENDPOINT}games`.length);

  console.log(`Created game: ${game.name} with ID ${gameId}`);
  return gameId;
}

async function getCharacterId(character: GameCharacter) {
  const characterResponse = await fetch(
    `${BASE_API_URL}/characters/identifier/${character.code}`,
  );

  if (characterResponse.status != 200) {
    return await createCharacterIfDoesntExist(character);
  }

  const characterId = (await characterResponse.json()).data.id;

  console.log(`Returning character ID: ${characterId}`);
  return characterId;
}

async function getGameId(game: Game) {
  const gameResponse = await fetch(
    `${BASE_API_URL}/games/identifier/${game.name}`,
  );

  if (gameResponse.status != 200) {
    return await createGameIfDoesntExist(game);
  }

  return (await gameResponse.json()).data.id;
}

async function createCharacterIfDoesntExist(character: GameCharacter) {
  const existingCharacterResponse = await fetch(
    `${BASE_API_URL}characters/identifier/${character.code}`,
  );

  if (existingCharacterResponse.status == 200) {
    return (await existingCharacterResponse.json()).data.id;
  }

  const requestBody = {
    name: character.code,
    gameId: await getGameId(character.game),
  };

  const createCharacterResponse = await fetch(`${BASE_API_URL}characters`, {
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: `Bearer ${AUTH_JWT}`,
    },
    body: JSON.stringify(requestBody),
  });

  if (createCharacterResponse.status != 201) {
    console.log(
      `An error ocurred when attempting to create character that didn't exist: ${character.code}. "${createCharacterResponse.status} ${createCharacterResponse.statusText}"`,
    );
    return;
  }

  console.log(`Created character: ${character.code}`);
  return createCharacterResponse.headers
    .get("Location")
    ?.substring(`${BASE_ENDPOINT}characters/identifier/`.length);
}

function getAllCharactersFromJsonFile(game: Game): GameCharacter[] {
  return game.filepath.map((data: any) => ({
    game: game,
    code: data.character,
    aliases: getAllAliasesFromCharacterCode(data.character, game.code),
    moves: data.moves,
  }));
}

function getAllAliasesFromCharacterCode(
  characterCode: string,
  gameCode: string,
): string[] {
  return charactercodes.filter((json) => json.game == gameCode)[0].characters[
    characterCode
  ];
}

migrate();
