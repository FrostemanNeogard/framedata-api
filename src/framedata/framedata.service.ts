import {
  Injectable,
  Logger,
  BadRequestException,
  NotFoundException,
  ConflictException,
} from '@nestjs/common';
import { FrameData } from 'src/__types/frameData';
import { GameCode } from 'src/__types/gameCode';
import { FramedataRepository } from './framedata.repository';

@Injectable()
export class FramedataService {
  private readonly logger = new Logger(FramedataService.name);

  constructor(private readonly repo: FramedataRepository) {}

  async getCharacterFrameData(
    characterCode: string,
    game: GameCode,
  ): Promise<FrameData[]> {
    try {
      const doc = await this.repo.findByCharacter(game, characterCode);

      if (!doc?.moves) {
        throw new BadRequestException(
          `No framedata was found for the given character and game combo.`,
        );
      }

      return doc.moves;
    } catch (error) {
      this.logger.error(
        `Failed to fetch frame data for ${characterCode} in ${game}. ${error.message}`,
      );
      throw new BadRequestException(
        `No framedata was found for the given character and game combo.`,
      );
    }
  }

  async getSingleMoveFrameDataOrSimilarMoves(
    character: string,
    game: GameCode,
    notation: string,
  ) {
    this.logger.log(
      `Attempting to find attack: ${notation} for character: ${character} in ${game}.`,
    );

    const frameData = await this.getCharacterFrameData(character, game);
    const attackInfo: FrameData[] = frameData.filter((item) =>
      item.alternateInputs.includes(
        notation.replaceAll(/[\u200B-\u200D\uFEFF]/g, ''),
      ),
    );

    const similarityMap: { move: FrameData; similarity: number }[] = [];
    for (let i = 0; i < 2; i++) {
      if (!!attackInfo[0]) {
        break;
      }

      this.logger.log(`Formatting notation, iteration: ${i}`);
      const removePlus = i > 0;
      const formattedNotation = this.formatNotation(notation, removePlus);

      for (let y = 0; y < frameData.length; y++) {
        const moveData = frameData[y];
        moveData.alternateInputs.forEach((input) => {
          const formattedInput = this.formatNotation(input, removePlus);

          if (
            formattedNotation == formattedInput ||
            formattedNotation == this.formatNotation(moveData.name, removePlus)
          ) {
            attackInfo.push(moveData);
          }

          const similarity = this.calculateSimilarity(input, notation);
          similarityMap.push({ move: moveData, similarity });
        });
      }
    }

    if (!attackInfo[0]) {
      similarityMap.sort((a, b) => b.similarity - a.similarity);

      const top5Moves = similarityMap.slice(0, 5).map((entry) => entry.move);
      const uniqueSimilarMoves = top5Moves.filter(
        (value, index, array) => array.indexOf(value) == index,
      );

      if (uniqueSimilarMoves.length == 0) {
        throw new NotFoundException(`No data found for the given attack.`);
      }

      this.logger.error(
        `Couldn't find attack: ${notation}. Returning 5 most similar ones.`,
      );
      return uniqueSimilarMoves;
    }

    this.logger.log(`Found attack: ${attackInfo[0].input}`);
    return [attackInfo[0]];
  }

  async saveCharacterFrameData(
    characterCode: string,
    game: GameCode,
    frameData: FrameData[],
  ): Promise<void> {
    try {
      await this.repo.saveCharacter(game, characterCode, frameData);
      this.logger.log(`Frame data saved for ${characterCode} in ${game}.`);
    } catch (error) {
      this.logger.error(
        `Failed to save frame data for ${characterCode} in ${game}. ${error.message}`,
      );

      throw new BadRequestException(
        `Failed to save frame data for character: ${characterCode}`,
      );
    }
  }

  async deleteCharacterFramedata(
    characterCode: string,
    gameCode: GameCode,
    input: string,
  ) {
    const frameData = await this.getCharacterFrameData(characterCode, gameCode);

    const moveIndex = frameData.findIndex(
      (move) => move.input === input || move.alternateInputs.includes(input),
    );

    if (moveIndex === -1) {
      this.logger.error(
        `Couldn't delete framedata due to missing move "${input}" for "${characterCode}" in "${gameCode}".`,
      );
      throw new NotFoundException(
        `Move with input "${input}" not found for character "${characterCode}" in game "${gameCode}".`,
      );
    }

    frameData.splice(moveIndex, 1);

    try {
      this.repo.saveCharacter(gameCode, characterCode, frameData);
    } catch (error) {
      this.logger.error(
        `Failed to save frame data for ${characterCode} in ${gameCode}. ${error.message}`,
      );

      throw new BadRequestException(
        `Failed to save frame data for character: ${characterCode}`,
      );
    }
  }

  async addCharacterFramedata(
    characterCode: string,
    gameCode: GameCode,
    data: FrameData,
  ) {
    let isDuplicate = false;
    try {
      const existingData = await this.getSingleMoveFrameDataOrSimilarMoves(
        characterCode,
        gameCode,
        data.input,
      );

      isDuplicate = existingData.length == 1;
    } catch {}

    if (isDuplicate) {
      throw new ConflictException('Data already exists for the given attack.');
    }

    const frameData = await this.getCharacterFrameData(characterCode, gameCode);
    const newData = [...frameData, data];

    try {
      this.repo.saveCharacter(gameCode, characterCode, newData);
    } catch (error) {
      this.logger.error(
        `Failed to save frame data for ${characterCode} in ${gameCode}. ${error.message}`,
      );

      throw new BadRequestException(
        `Failed to save frame data for character: ${characterCode}`,
      );
    }
  }

  private calculateSimilarity(input1: string, input2: string): number {
    const set1 = new Set(input1.split(''));
    const set2 = new Set(input2.split(''));
    const intersection = new Set([...set1].filter((x) => set2.has(x)));
    const union = new Set([...set1, ...set2]);
    return intersection.size / union.size;
  }

  private formatNotation(inputNotation: string, removePlus: boolean): string {
    let modifiedNotation = inputNotation.toLowerCase();
    if (!modifiedNotation.includes('fc')) {
      modifiedNotation = modifiedNotation.replaceAll('cd', 'f,n,d,df');
    }
    modifiedNotation = modifiedNotation
      // TODO: This is kinda stupid, maybe move the input shortcuts to a different function
      .replaceAll('#', ':')
      .replaceAll('.', '')
      .replaceAll(' ', '')
      .replaceAll(/ *\([^)]*\) */g, '')
      .replaceAll(/[\u200B-\u200D\uFEFF]/g, '')
      .replaceAll('h.', 'in heat')
      .replaceAll('r.', 'in rage')
      .replaceAll('backturned', 'bt')
      .replaceAll('backturn', 'bt')
      .replaceAll('debug', 'b,db,d,df')
      .replaceAll('gs', 'f,n,b,db,d,df,f')
      .replaceAll('wr', 'f,f,f')
      .replaceAll('qcf', 'd,df,f')
      .replaceAll('qcb', 'd,db,b')
      .replaceAll('hcf', 'b,db,f,df,f')
      .replaceAll('hcb', 'f,df,d,db,d')
      .replaceAll('ewgf', 'f,n,d,df:2')
      .replaceAll('electric', 'f,n,d,df:2')
      .replaceAll('ewhf', 'f,n,d,df:2')
      .replaceAll('heatsmash', 'in heat 2+3')
      .replaceAll('heatburst', '2+3')
      .replaceAll('rageart', 'in rage df+1+2')
      .split('or')
      .pop()
      .replace(/[\s,/()]/g, '');

    if (removePlus) {
      modifiedNotation = modifiedNotation.replaceAll(/\+/g, '');
    }

    return modifiedNotation;
  }
}
