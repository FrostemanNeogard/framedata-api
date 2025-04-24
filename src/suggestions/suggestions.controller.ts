import {
  Controller,
  Get,
  Post,
  Body,
  Param,
  NotFoundException,
  Logger,
} from '@nestjs/common';
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
    const suggestion = await this.suggestionsService.findOne(id);

    if (!suggestion) {
      this.logger.error(`Couldn't find suggestion with id: ${id}`);
      throw new NotFoundException('Suggestion not found.');
    }

    return suggestion;
  }
}
