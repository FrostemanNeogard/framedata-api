import { Test, TestingModule } from '@nestjs/testing';
import { SuggestionsController } from './suggestions.controller';
import { SuggestionsService } from './suggestions.service';
import { SuggestionsRepository } from './suggestions.repository';

describe('SuggestionsController', () => {
  let controller: SuggestionsController;

  const mockRepository = {
    findByCharacter: jest.fn(),
    saveCharacter: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [SuggestionsController],
      providers: [
        SuggestionsService,
        {
          provide: SuggestionsRepository,
          useValue: mockRepository,
        },
      ],
    }).compile();

    controller = module.get<SuggestionsController>(SuggestionsController);
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });
});
