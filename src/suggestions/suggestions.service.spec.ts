import { Test, TestingModule } from '@nestjs/testing';
import { SuggestionsService } from './suggestions.service';
import { SuggestionsRepository } from './suggestions.repository';

describe('SuggestionsService', () => {
  let service: SuggestionsService;

  const mockRepository = {
    findByCharacter: jest.fn(),
    saveCharacter: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        SuggestionsService,
        {
          provide: SuggestionsRepository,
          useValue: mockRepository,
        },
      ],
    }).compile();

    service = module.get<SuggestionsService>(SuggestionsService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });
});
