import * as dotenv from "dotenv";
dotenv.config();

// ==========================================
// CONFIGURATION
// ==========================================

const NEW_API_BASE_URL = "http://localhost:8080/api/v1";
const OLD_API_BASE_URL = "https://api.framedatabot.com/v1/framedata";
const JWT_TOKEN = process.env.MIGRATION_JWT;

const AUTH_HEADERS = {
  Authorization: `Bearer ${JWT_TOKEN}`,
  "Content-Type": "application/json",
};

interface CharacterConfig {
  oldName: string;
  newName: string;
  aliases: string[];
}

interface GameConfig {
  oldName: string;
  newName: string;
  attributesTemplate: Record<string, any>;
  characters: CharacterConfig[];
}

// --- Migration Config ---

const GAME_CONFIG: GameConfig[] = [
  {
    oldName: "tekken8",
    newName: "Tekken 8",
    attributesTemplate: {
      startup: "",
      hit: "",
      block: "",
      damage: "",
      hitLevel: "",
      counter: "",
      notes: [],
    },
    characters: [
      {
        oldName: "bryan",
        newName: "Bryan Fury",
        aliases: ["brian", "jet upper guy"],
      },
      {
        oldName: "kazuya",
        newName: "Kazuya Mishima",
        aliases: ["kaz", "dorya"],
      },
    ],
  },
  {
    oldName: "tekken7",
    newName: "Tekken 7",
    attributesTemplate: {
      startup: "",
      hit: "",
      block: "",
      damage: "",
      hitLevel: "",
      notes: [],
    },
    characters: [{ oldName: "chunli", newName: "Chun-Li", aliases: ["chun"] }],
  },
];

// ==========================================
// HELPERS
// ==========================================

function getIdFromLocation(locationHeader: string | null): string | null {
  if (!locationHeader) return null;
  const parts = locationHeader.split("/");
  return parts[parts.length - 1];
}

async function handleResponse(
  response: Response,
  resourceName: string,
): Promise<string | null> {
  if (!response.ok) {
    let errorDetail = response.statusText;
    try {
      const errorBody = await response.json();
      errorDetail = errorBody.message || JSON.stringify(errorBody);
    } catch {}
    throw new Error(`[${response.status}] ${response.url}: ${errorDetail}`);
  }

  if (response.headers.has("Location")) {
    return getIdFromLocation(response.headers.get("Location"));
  }

  return null;
}

async function createGame(name: string, template: any): Promise<string | null> {
  console.log(`Creating Game: ${name}...`);
  try {
    const payload = { name, attributesTemplate: template };
    const response = await fetch(`${NEW_API_BASE_URL}/games`, {
      method: "POST",
      headers: AUTH_HEADERS,
      body: JSON.stringify(payload),
    });

    const id = await handleResponse(response, `game ${name}`);
    console.log(` -> Success! Game ID: ${id}`);
    return id;
  } catch (error) {
    console.error(
      ` -> Failed to create game ${name}:`,
      (error as Error).message,
    );
    return null;
  }
}

async function createCharacter(
  name: string,
  gameId: string,
): Promise<string | null> {
  console.log(`  Creating Character: ${name}...`);
  try {
    const payload = { name, gameId };
    const response = await fetch(`${NEW_API_BASE_URL}/characters`, {
      method: "POST",
      headers: AUTH_HEADERS,
      body: JSON.stringify(payload),
    });

    const id = await handleResponse(response, `character ${name}`);
    console.log(`  -> Success! Char ID: ${id}`);
    return id;
  } catch (error) {
    console.error(
      `  -> Failed to create character ${name}:`,
      (error as Error).message,
    );
    return null;
  }
}

async function createAlias(charId: string, aliasName: string) {
  try {
    const payload = { characterId: charId, aliasName };
    await fetch(`${NEW_API_BASE_URL}/aliases`, {
      method: "POST",
      headers: AUTH_HEADERS,
      body: JSON.stringify(payload),
    });
  } catch (error) {
    console.warn(`    [!] Warning: Failed to create alias '${aliasName}'`);
  }
}

async function fetchLegacyData(
  gameSlug: string,
  charSlug: string,
): Promise<any[]> {
  const url = `${OLD_API_BASE_URL}/${gameSlug}/${charSlug}`;
  console.log(`    Fetching legacy data from: ${url}`);
  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`[${response.status}] Failed to fetch old data.`);
    }
    return await response.json();
  } catch (error) {
    console.error(
      `    -> Error fetching legacy data:`,
      (error as Error).message,
    );
    return [];
  }
}

async function transformAndUploadMoves(
  charId: string,
  moves: any[],
  template: Record<string, any>,
) {
  console.log(`    Processing ${moves.length} moves...`);
  let successCount = 0;
  const validAttributeKeys = Object.keys(template);

  for (const move of moves) {
    const identifiers = new Set<string>();

    if (move.input) identifiers.add(move.input);
    if (Array.isArray(move.alternateInputs)) {
      move.alternateInputs.forEach((alt: string) => identifiers.add(alt));
    }

    if (identifiers.size === 0) continue;

    const attributes: Record<string, any> = {};
    for (const key of validAttributeKeys) {
      if (move[key] !== undefined && move[key] !== null) {
        attributes[key] = move[key];
      }
    }

    const payload = {
      identity: {
        identifiers: Array.from(identifiers),
        categories: move.categories || [],
      },
      data: {
        attributes: attributes,
      },
    };

    try {
      const response = await fetch(
        `${NEW_API_BASE_URL}/framedata/character/${charId}`,
        {
          method: "POST",
          headers: AUTH_HEADERS,
          body: JSON.stringify(payload),
        },
      );

      if (response.ok) {
        successCount++;
      } else {
        let errorDetail = response.statusText;
        try {
          const errorBody = await response.json();
          errorDetail = errorBody.message || JSON.stringify(errorBody);
        } catch {}
        console.error(
          `    [!] Failed to upload move '${Array.from(identifiers)[0]}' [${response.status}]: ${errorDetail}`,
        );
      }
    } catch (error) {
      console.error(
        `    [!] Exception uploading move:`,
        (error as Error).message,
      );
    }
  }

  console.log(`    -> Uploaded ${successCount}/${moves.length} moves.`);
}

// ==========================================
// MAIN EXECUTION
// ==========================================

async function main() {
  console.log("Starting Migration...");

  for (const gameConf of GAME_CONFIG) {
    const gameId = await createGame(
      gameConf.newName,
      gameConf.attributesTemplate,
    );
    if (!gameId) continue;

    for (const charConf of gameConf.characters) {
      const charId = await createCharacter(charConf.newName, gameId);
      if (!charId) continue;

      if (charConf.aliases && charConf.aliases.length > 0) {
        console.log(`    Creating aliases: ${charConf.aliases.join(", ")}`);
        for (const alias of charConf.aliases) {
          await createAlias(charId, alias);
        }
      }

      const legacyMoves = await fetchLegacyData(
        gameConf.oldName,
        charConf.oldName,
      );

      if (legacyMoves.length > 0) {
        await transformAndUploadMoves(
          charId,
          legacyMoves,
          gameConf.attributesTemplate,
        );
      } else {
        console.log("    -> No legacy data found.");
      }
    }
  }

  console.log("\nMigration Complete.");
}

main().catch((err) => console.error(err));
