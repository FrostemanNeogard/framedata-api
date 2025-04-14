import { Injectable } from '@nestjs/common';
import { CharacterCodesRepository } from './characterCodes.repository';

@Injectable()
export class CharacterCodesService {
  constructor(private readonly repository: CharacterCodesRepository) {}

  async getCharacterCode(alias: string, game: string): Promise<string | null> {
    const record = await this.repository.findByGame(game);
    if (!record) return null;

    for (const [code, aliases] of record.characters.entries()) {
      if (
        aliases
          .map((a: string) => a.toLowerCase())
          .includes(alias.toLowerCase())
      ) {
        return code;
      }
    }

    return null;
  }
}
