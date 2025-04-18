import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Suggestion } from './entities/suggestion.entity';

@Injectable()
export class SuggestionsRepository {
  constructor(
    @InjectModel(Suggestion.name)
    private readonly suggestionModel: Model<Suggestion>,
  ) {}

  async findAll() {
    return this.suggestionModel.find().exec();
  }

  async findById(id: string) {
    return this.suggestionModel.findById(id).exec();
  }

  async create(suggestion: Suggestion) {
    const created = new this.suggestionModel(suggestion);
    return created.save();
  }
}
