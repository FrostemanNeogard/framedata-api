import {
  BadRequestException,
  ConflictException,
  Injectable,
  Logger,
  NotFoundException,
} from '@nestjs/common';
import { SuggestionsRepository } from './suggestions.repository';
import { Suggestion } from './entities/suggestion.entity';
import { FramedataService } from 'src/framedata/framedata.service';
import { FrameData } from 'src/__types/frameData';

@Injectable()
export class SuggestionsService {
  private readonly logger = new Logger(SuggestionsService.name);

  constructor(
    private readonly repo: SuggestionsRepository,
    private readonly framedataService: FramedataService,
  ) {}

  async create(suggestion: Suggestion) {
    if (suggestion.action == 'modify' || suggestion.action == 'create') {
      if (
        (!suggestion.payload.data ||
          Object.keys(suggestion.payload.data).length == 0) &&
        !suggestion.payload.index
      ) {
        throw new BadRequestException(
          `Cannot ${suggestion.action} entry without data.`,
        );
      }
    }

    const foundTarget =
      await this.framedataService.getSingleMoveFrameDataOrSimilarMoves(
        suggestion.target.character.toLowerCase(),
        suggestion.target.game,
        suggestion.action == 'create'
          ? suggestion.payload.data.input
          : suggestion.target.input,
      );

    if (suggestion.action == 'create') {
      this.logger.log(`Creating with length: ${foundTarget.length}`);
      if (foundTarget.length == 1) {
        throw new BadRequestException('Attack already exists.');
      }
    } else {
      this.logger.log(
        `Modifying or deleting with length: ${foundTarget.length}`,
      );
      if (foundTarget.length != 1) {
        throw new BadRequestException("Attack doesn't exist.");
      }
    }

    if (
      foundTarget.length == 1 &&
      suggestion.action == 'modify' &&
      this.isSubset(foundTarget[0], suggestion.payload.data)
    ) {
      this.logger.error(`Rejected modification due to identical data.`);
      throw new BadRequestException('Cannot create duplicate entry.');
    }

    return this.repo.create(suggestion);
  }

  isSubset(superObj: Record<any, any>, subObj: Record<any, any>) {
    return Object.entries(subObj).every(([key, value]) => {
      if (key == 'notes') {
        if (JSON.stringify(superObj[key]) == JSON.stringify(value)) {
          return true;
        }
      } else if (superObj[key] == value) {
        return true;
      }

      return false;
    });
  }

  async findAll() {
    return this.repo.findAll();
  }

  async findOne(id: string) {
    const suggestion = this.repo.findById(id);

    if (!suggestion) {
      this.logger.error(`Couldn't find suggestion with id: ${id}`);
      throw new NotFoundException('Suggestion not found.');
    }

    return suggestion;
  }

  async approveSuggestion(suggestion: Suggestion) {
    const { character, game, input } = suggestion.target;

    const resolvedCharacterCode = character.toLowerCase();

    const existingFramedata =
      await this.framedataService.getSingleMoveFrameDataOrSimilarMoves(
        resolvedCharacterCode,
        game,
        input,
      );

    switch (suggestion.action) {
      case 'delete':
        if (existingFramedata.length != 1) {
          throw new NotFoundException(
            `Couldn't find attack "${input}" for character "${character}" in game: "${game}".`,
          );
        }

        return await this.framedataService.deleteCharacterFramedata(
          resolvedCharacterCode,
          game,
          input,
        );

      case 'create':
        if (existingFramedata.length != 1) {
          throw new ConflictException(
            `Attack already exists: "${input}" for character "${character}" in game: "${game}".`,
          );
        }

        const newCreateFramedata: FrameData = {
          input: suggestion.payload.data.input ?? suggestion.target.input ?? '',
          hitLevel: suggestion.payload.data.hitLevel ?? '',
          damage: suggestion.payload.data.damage ?? '',
          startup: suggestion.payload.data.startup ?? '',
          block: suggestion.payload.data.block ?? '',
          hit: suggestion.payload.data.hit ?? '',
          counter: suggestion.payload.data.counter ?? '',
          notes: suggestion.payload.data.notes ?? [],
          name: suggestion.payload.data.name ?? '',
          alternateInputs: suggestion.payload.data.alternateInputs ?? [],
          categories: suggestion.payload.data.categories ?? [],
        };

        return await this.framedataService.addCharacterFramedata(
          resolvedCharacterCode,
          game,
          newCreateFramedata,
        );

      case 'modify':
        if (existingFramedata.length != 1) {
          throw new NotFoundException(
            `Couldn't find attack "${input}" for character "${character}" in game: "${game}".`,
          );
        }

        const newUpdateFramedata: FrameData = {
          input: suggestion.payload.data.input ?? existingFramedata[0].input,
          hitLevel:
            suggestion.payload.data.hitLevel ?? existingFramedata[0].hitLevel,
          damage: suggestion.payload.data.damage ?? existingFramedata[0].damage,
          startup:
            suggestion.payload.data.startup ?? existingFramedata[0].startup,
          block: suggestion.payload.data.block ?? existingFramedata[0].block,
          hit: suggestion.payload.data.hit ?? existingFramedata[0].hit,
          counter:
            suggestion.payload.data.counter ?? existingFramedata[0].counter,
          notes: suggestion.payload.data.notes ?? existingFramedata[0].notes,
          name: suggestion.payload.data.name ?? existingFramedata[0].name,
          alternateInputs:
            suggestion.payload.data.alternateInputs ??
            existingFramedata[0].alternateInputs,
          categories:
            suggestion.payload.data.categories ??
            existingFramedata[0].categories,
        };

        const allMovesData = await this.framedataService.getCharacterFrameData(
          resolvedCharacterCode,
          game,
        );

        const newFramedataArray: FrameData[] = allMovesData.map((data) =>
          data.input == newUpdateFramedata.input ? newUpdateFramedata : data,
        );

        return await this.framedataService.saveCharacterFrameData(
          resolvedCharacterCode,
          game,
          newFramedataArray,
        );
    }
  }

  async rejectSuggestion(id: string) {
    return this.repo.deleteById(id);
  }
}
