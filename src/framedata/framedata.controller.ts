import {
  Controller,
  Body,
  Logger,
  BadRequestException,
  Get,
  Param,
  NotFoundException,
  Patch,
} from '@nestjs/common';
import { FramedataService } from './framedata.service';
import { GameCode } from 'src/__types/gameCode';
import { CharacterCodesService } from 'src/characterCodes/characterCodes.service';
import {
  TekkenMoveCategory,
  tekkenMoveCategories,
} from 'src/__types/moveCategories';
import { FrameData } from 'src/__types/frameData';
import { GameCodeValidationPipe } from 'src/__pipes/gameCodeValidation.pipe';

@Controller('framedata')
export class FramedataController {
  private readonly logger = new Logger(FramedataController.name);

  constructor(
    private readonly framedataService: FramedataService,
    private readonly characterCodesService: CharacterCodesService,
  ) {}

  @Get(':gameCode/:characterCode')
  public async getFramedataForCharacter(
    @Param('gameCode', GameCodeValidationPipe) gameCode: GameCode,
    @Param('characterCode') characterName: string,
  ) {
    const characterCode = await this.characterCodesService.getCharacterCode(
      characterName,
      gameCode,
    );

    if (characterCode == null) {
      this.logger.log(
        `Couldn't find character code for: ${characterName} in game: ${gameCode}`,
      );
      throw new NotFoundException(
        'The given character was not found for the given game.',
      );
    }

    this.logger.log(
      `Returning all character data for character: ${characterName} in game: ${gameCode}`,
    );

    return await this.framedataService.getCharacterFrameData(
      characterCode,
      gameCode,
    );
  }

  @Get(':gameCode/:characterCode/categories/:category')
  public async getMoveCategoryForCharacter(
    @Param('gameCode', GameCodeValidationPipe) gameCode: GameCode,
    @Param('characterCode') characterCode: string,
    @Param('category') category: TekkenMoveCategory,
  ) {
    const characterCodeResolved =
      await this.characterCodesService.getCharacterCode(
        characterCode,
        gameCode,
      );

    if (!characterCodeResolved) {
      this.logger.log(
        `Couldn't find character code for: ${characterCode} in game: ${gameCode}`,
      );
      throw new NotFoundException(
        'The given character was not found for the specified game.',
      );
    }

    if (!tekkenMoveCategories.includes(category)) {
      throw new BadRequestException('Invalid category.');
    }

    const allFramedata = await this.framedataService.getCharacterFrameData(
      characterCodeResolved,
      gameCode,
    );

    const filteredMoves = allFramedata.filter((attack) =>
      attack.categories.includes(category),
    );

    const moveInputs = filteredMoves.map((attack) => attack.input);

    if (moveInputs.length == 0) {
      throw new NotFoundException();
    }

    return moveInputs;
  }

  @Get(':gameCode/:characterCode/moves/:input')
  public async getFrameDataSingle(
    @Param('gameCode', GameCodeValidationPipe) gameCode: GameCode,
    @Param('characterCode') characterCode: string,
    @Param('input') input: string,
  ) {
    const characterCodeResolved =
      await this.characterCodesService.getCharacterCode(
        characterCode,
        gameCode,
      );

    if (!characterCodeResolved) {
      this.logger.log(
        `Couldn't find character code for: ${characterCode} in game: ${gameCode}`,
      );
      throw new NotFoundException(
        'The given character was not found for the specified game.',
      );
    }

    const frameData =
      await this.framedataService.getSingleMoveFrameDataOrSimilarMoves(
        characterCodeResolved,
        gameCode,
        input,
      );

    if (frameData.length > 1) {
      throw new NotFoundException({ similar_moves: frameData });
    }

    return frameData;
  }

  @Patch(':gameCode/:characterCode/moves/:input')
  public async updateMoveData(
    @Param('gameCode', GameCodeValidationPipe) gameCode: GameCode,
    @Param('characterCode') characterCode: string,
    @Param('input') input: string,
    @Body() updates: Partial<FrameData>,
  ) {
    const characterCodeResolved =
      await this.characterCodesService.getCharacterCode(
        characterCode,
        gameCode,
      );

    if (!characterCodeResolved) {
      this.logger.log(
        `Couldn't find character code for: ${characterCode} in game: ${gameCode}`,
      );
      throw new NotFoundException(
        'The given character was not found for the specified game.',
      );
    }

    const frameData = await this.framedataService.getCharacterFrameData(
      characterCodeResolved,
      gameCode,
    );

    const moveIndex = frameData.findIndex(
      (move) => move.input === input || move.alternateInputs.includes(input),
    );

    if (moveIndex === -1) {
      throw new NotFoundException(
        `Move with input "${input}" not found for character "${characterCode}" in game "${gameCode}".`,
      );
    }

    const moveToUpdate = frameData[moveIndex];
    frameData[moveIndex] = { ...moveToUpdate, ...updates };

    try {
      await this.framedataService.saveCharacterFrameData(
        characterCodeResolved,
        gameCode,
        frameData,
      );
      this.logger.log(
        `Successfully updated move "${input}" for character "${characterCode}" in game "${gameCode}".`,
      );
      return frameData[moveIndex];
    } catch (error) {
      this.logger.error(
        `Failed to update move "${input}" for character "${characterCode}" in game "${gameCode}". ${error.message}`,
      );
      throw new BadRequestException('Failed to update move data.');
    }
  }
}
