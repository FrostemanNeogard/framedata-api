import {
  BadRequestException,
  Injectable,
  InternalServerErrorException,
} from '@nestjs/common';
import { CharacterCodesRepository } from './characterCodes.repository';
import { GameCode } from 'src/__types/gameCode';

@Injectable()
export class CharacterCodesService {
  constructor(private readonly repository: CharacterCodesRepository) {}

  async getCharacterCode(
    alias: string,
    game: GameCode,
  ): Promise<string | null> {
    const record = await this.repository.findByGame(game);
    if (!record) {
      throw new InternalServerErrorException("Couldn't fetch game data.");
    }

    for (const [code, aliases] of record.characters.entries()) {
      if (
        aliases
          .map((a: string) => a.toLowerCase())
          .includes(alias.toLowerCase())
      ) {
        return code;
      }
    }

    throw new BadRequestException(`Invalid character code: ${alias}`);
  }
}
