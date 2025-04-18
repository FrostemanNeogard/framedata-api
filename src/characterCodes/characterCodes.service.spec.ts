import { Test, TestingModule } from '@nestjs/testing';
import { CharacterCodesService } from './characterCodes.service';
import { CharacterCodesRepository } from './characterCodes.repository';
import { GameCode } from '../__types/gameCode';
import { BadRequestException } from '@nestjs/common';

describe('CharacterCodesService', () => {
  let service: CharacterCodesService;
  let repository: CharacterCodesRepository;

  const mockCharacterCodes = {
    characters: new Map([
      ['kaz', ['kaz', 'kazuya', 'kazuyamishima']],
      ['jin', ['jin', 'jinkazama']],
    ]),
  };

  const mockRepository = {
    findByGame: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        CharacterCodesService,
        {
          provide: CharacterCodesRepository,
          useValue: mockRepository,
        },
      ],
    }).compile();

    service = module.get<CharacterCodesService>(CharacterCodesService);
    repository = module.get<CharacterCodesRepository>(CharacterCodesRepository);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('getCharacterCode', () => {
    it('should return character code for exact match', async () => {
      const alias = 'kaz';
      const game: GameCode = 'tekken8';
      mockRepository.findByGame.mockResolvedValue(mockCharacterCodes);

      const result = await service.getCharacterCode(alias, game);

      expect(result).toBe('kaz');
      expect(repository.findByGame).toHaveBeenCalledWith(game);
    });

    it('should return character code for case-insensitive match', async () => {
      const alias = 'KAZUYA';
      const game: GameCode = 'tekken8';
      mockRepository.findByGame.mockResolvedValue(mockCharacterCodes);

      const result = await service.getCharacterCode(alias, game);

      expect(result).toBe('kaz');
      expect(repository.findByGame).toHaveBeenCalledWith(game);
    });

    it('should return null when no character codes found for game', async () => {
      const alias = 'kaz';
      const game: GameCode = 'tekken8';
      mockRepository.findByGame.mockResolvedValue(null);

      const result = await service.getCharacterCode(alias, game);

      expect(result).toBeNull();
    });

    it('should throw BadRequestException when alias is not found', async () => {
      const alias = 'nonexistent';
      const game: GameCode = 'tekken8';
      mockRepository.findByGame.mockResolvedValue(mockCharacterCodes);

      await expect(service.getCharacterCode(alias, game)).rejects.toThrow(
        BadRequestException,
      );
    });
  });
});
