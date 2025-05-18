import {
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
    return this.repo.create(suggestion);
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

    const existingFramedata =
      await this.framedataService.getSingleMoveFrameDataOrSimilarMoves(
        character,
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
          character,
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
          ...suggestion.payload.data,
        };

        return await this.framedataService.addCharacterFramedata(
          character,
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
          ...suggestion.payload.data,
        };

        return await this.framedataService.saveCharacterFrameData(
          character,
          game,
          [newUpdateFramedata],
        );
    }
  }
}
