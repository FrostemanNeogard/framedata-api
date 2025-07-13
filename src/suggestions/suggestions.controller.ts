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
  BadRequestException,
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
    if (createSuggestionDto.target.input == '') {
      throw new BadRequestException('No target input provided');
    }

    if (
      createSuggestionDto.action == 'create' &&
      !createSuggestionDto.payload.data.input
    ) {
      throw new BadRequestException('No input provided');
    }

    if (
      createSuggestionDto.action == 'modify' &&
      createSuggestionDto.payload.data.input == ''
    ) {
      throw new BadRequestException('Cannot modify attack to have empty input');
    }

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
