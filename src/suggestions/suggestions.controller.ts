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
  Query,
} from '@nestjs/common';
import { SuggestionsService } from './suggestions.service';
import { CreateSuggestionDto } from './dto/create-suggestion.dto';
import { Suggestion } from './entities/suggestion.entity';
import { OwnerAuthGuard } from 'src/auth/guards/owner-auth.guard';
import { PaginatedResponse } from 'src/__types/responses';

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
  async findAll(@Query('page') page: number): Promise<PaginatedResponse> {
    if (!page) {
      throw new BadRequestException('The "Page" query parameter must be set.');
    }

    if (page <= 0) {
      throw new BadRequestException('"Page" must be greater than 0.');
    }

    const allSuggestions = await this.suggestionsService.findAll();
    const totalPages = Math.ceil(allSuggestions.length / 10);
    const startIndex = Math.max((page - 1) * 10, 0);
    const paginatedSuggestions = allSuggestions.slice(
      startIndex,
      startIndex + 10,
    );

    const response: PaginatedResponse = {
      data: paginatedSuggestions,
      pagination: {
        currentPage: page,
        nextPage: page >= totalPages ? null : page + 1,
        previousPage: page <= 1 ? null : page - 1,
        totalPages: totalPages,
        totalEntries: allSuggestions.length,
      },
    };

    return response;
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
