import {
  Controller,
  Get,
  Post,
  Body,
  Param,
  Logger,
  UseGuards,
  NotFoundException,
  Delete,
} from '@nestjs/common';
import { SuggestionsService } from './suggestions.service';
import { CreateSuggestionDto } from './dto/create-suggestion.dto';
import { Suggestion } from './entities/suggestion.entity';
import { OwnerAuthGuard } from 'src/auth/guards/owner-auth.guard';

@Controller('suggestions')
export class SuggestionsController {
  private readonly logger = new Logger(SuggestionsController.name);

  constructor(private readonly suggestionsService: SuggestionsService) {}

  @Post()
  async create(@Body() createSuggestionDto: CreateSuggestionDto) {
    const suggestion = new Suggestion(createSuggestionDto);
    const createdSuggestion = await this.suggestionsService.create(suggestion);
    return createdSuggestion;
  }

  @Get()
  async findAll() {
    const suggestions = await this.suggestionsService.findAll();
    return suggestions;
  }

  @Get(':id')
  async findOne(@Param('id') id: string) {
    const suggestion = await this.suggestionsService.findOne(id);

    if (!suggestion) {
      throw new NotFoundException(`No suggestion found with id "${id}".`);
    }

    return suggestion;
  }

  @UseGuards(OwnerAuthGuard)
  @Get('approve/:id')
  async approveSuggestion(@Param('id') id: string) {
    const suggestion = await this.suggestionsService.findOne(id);

    if (!suggestion) {
      throw new NotFoundException(`No suggestion found with id "${id}".`);
    }

    await this.suggestionsService.approveSuggestion(suggestion);

    return suggestion;
  }

  @UseGuards(OwnerAuthGuard)
  @Delete('reject/:id')
  async rejectSuggestion(@Param('id') id: string) {
    await this.suggestionsService.rejectSuggestion(id);

    return;
  }
}
