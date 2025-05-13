import { Controller, Get, Post, Body, Param, Logger } from '@nestjs/common';
import { SuggestionsService } from './suggestions.service';
import { CreateSuggestionDto } from './dto/create-suggestion.dto';
import { Suggestion } from './entities/suggestion.entity';

@Controller('suggestions')
export class SuggestionsController {
  private readonly logger = new Logger(SuggestionsController.name);

  constructor(private readonly suggestionsService: SuggestionsService) {}

  @Post()
  async create(@Body() createSuggestionDto: CreateSuggestionDto) {
    const suggestion = new Suggestion(createSuggestionDto);
    return this.suggestionsService.create(suggestion);
  }

  @Get()
  async findAll() {
    return this.suggestionsService.findAll();
  }

  @Get(':id')
  async findOne(@Param('id') id: string) {
    return this.suggestionsService.findOne(id);
  }
}
