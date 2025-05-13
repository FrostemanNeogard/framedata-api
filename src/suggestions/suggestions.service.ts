import { Injectable, Logger, NotFoundException } from '@nestjs/common';
import { SuggestionsRepository } from './suggestions.repository';
import { Suggestion } from './entities/suggestion.entity';

@Injectable()
export class SuggestionsService {
  private readonly logger = new Logger(SuggestionsService.name);

  constructor(private readonly repo: SuggestionsRepository) {}

  create(suggestion: Suggestion) {
    return this.repo.create(suggestion);
  }

  findAll() {
    return this.repo.findAll();
  }

  findOne(id: string) {
    const suggestion = this.repo.findById(id);

    if (!suggestion) {
      this.logger.error(`Couldn't find suggestion with id: ${id}`);
      throw new NotFoundException('Suggestion not found.');
    }

    return suggestion;
  }
}
