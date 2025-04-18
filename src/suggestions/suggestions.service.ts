import { Injectable } from '@nestjs/common';
import { SuggestionsRepository } from './suggestions.repository';
import { Suggestion } from './entities/suggestion.entity';

@Injectable()
export class SuggestionsService {
  constructor(private readonly repo: SuggestionsRepository) {}

  create(suggestion: Suggestion) {
    return this.repo.create(suggestion);
  }

  findAll() {
    return this.repo.findAll();
  }

  findOne(id: string) {
    return this.repo.findById(id);
  }
}
