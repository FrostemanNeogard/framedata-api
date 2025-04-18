import { GameCode } from 'src/__types/gameCode';
import { FramedataPostDto } from 'src/framedata/dtos/framedataPostDto';
import { CreateSuggestionDto } from '../dto/create-suggestion.dto';

export class Suggestion {
  action: 'modify' | 'delete' | 'create';

  target: {
    game: GameCode;
    character: string;
    input: string;
  };

  payload: Partial<FramedataPostDto>;

  constructor(dto: CreateSuggestionDto) {
    this.action = dto.action;
    this.target = dto.target;
    this.payload = dto.payload;
  }
}
