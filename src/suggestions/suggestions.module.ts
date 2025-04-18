import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { Suggestion } from './entities/suggestion.entity';
import { SuggestionSchema } from './schemas/suggestion.schema';
import { SuggestionsRepository } from './suggestions.repository';
import { SuggestionsService } from './suggestions.service';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: Suggestion.name, schema: SuggestionSchema },
    ]),
  ],
  providers: [SuggestionsService, SuggestionsRepository],
  exports: [SuggestionsService],
})
export class SuggestionsModule {}
