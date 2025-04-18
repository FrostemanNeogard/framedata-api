import { Test, TestingModule } from '@nestjs/testing';
import { GameCodesService } from './gameCodes.service';
import { GameCodesRepository } from './gameCodes.repository';
import { GameCode } from 'src/__types/gameCode';
import { BadRequestException } from '@nestjs/common';

describe('GameCodesService', () => {
  let service: GameCodesService;
  let repository: GameCodesRepository;

  const mockGameCodes = [
    'tekken6',
    'tekkentag2',
    'tekken7',
    'tekken8',
  ] as GameCode[];

  const mockRepository = {
    findAllGameCodes: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        GameCodesService,
        {
          provide: GameCodesRepository,
          useValue: mockRepository,
        },
      ],
    }).compile();

    service = module.get<GameCodesService>(GameCodesService);
    repository = module.get<GameCodesRepository>(GameCodesRepository);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('getAllGameCodes', () => {
    it('should return all game codes', () => {
      mockRepository.findAllGameCodes.mockReturnValue(mockGameCodes);

      const result = service.getAllGameCodes();

      expect(result).toEqual(mockGameCodes);
      expect(repository.findAllGameCodes).toHaveBeenCalled();
    });
  });

  describe('getGameCode', () => {
    it('should return valid game code', () => {
      const gameName = 'tekken8';
      mockRepository.findAllGameCodes.mockReturnValue(mockGameCodes);

      const result = service.getGameCode(gameName);

      expect(result).toBe(gameName);
      expect(repository.findAllGameCodes).toHaveBeenCalled();
    });

    it('should throw BadRequestException for invalid game code', () => {
      const gameName = 'invalid_game';
      mockRepository.findAllGameCodes.mockReturnValue(mockGameCodes);

      expect(() => service.getGameCode(gameName)).toThrow(BadRequestException);
    });
  });

  describe('isValidGameCode', () => {
    it('should return true for valid game code', () => {
      const gameCode = 'tekken8';
      mockRepository.findAllGameCodes.mockReturnValue(mockGameCodes);

      const result = service.isValidGameCode(gameCode);

      expect(result).toBe(true);
      expect(repository.findAllGameCodes).toHaveBeenCalled();
    });

    it('should return false for invalid game code', () => {
      const gameCode = 'invalid_game';
      mockRepository.findAllGameCodes.mockReturnValue(mockGameCodes);

      const result = service.isValidGameCode(gameCode);

      expect(result).toBe(false);
      expect(repository.findAllGameCodes).toHaveBeenCalled();
    });
  });
});
