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
  moves: Object;
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

const BASE_API_URL = "http://localhost:8080/api/v1/";

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
    characters: getAllCharactersFromJsonFile(g.filepath, g.code),
  }));

  for (let i = 0; i < gamesWithCharacters.length; i++) {
    await migrateGameWithCharacters(gamesWithCharacters[i]);
  }
}

async function migrateGameWithCharacters(
  gameWithCharacters: GameWithCharacters,
) {
  // TODO: Remember alternateInputs should be "identifiers" field
  // TODO: Remember "categories" is its own separate field, not part of the attributesTemplate
}

function getAllCharactersFromJsonFile(
  jsonFile,
  gameCode: string,
): GameCharacter[] {
  return jsonFile.map((data) => ({
    game: games.filter((g) => g.code == gameCode),
    code: data.characterCode,
    aliases: getAllAliasesFromCharacterCode(data.characterCode, gameCode),
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
