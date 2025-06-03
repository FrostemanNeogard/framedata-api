import { Test, TestingModule } from '@nestjs/testing';
import { CharacterCodesController } from './characterCodes.controller';
import { CharacterCodesService } from './characterCodes.service';
import { GameCodesService } from '../gameCodes/gameCodes.service';
import { GameCode } from '../__types/gameCode';
import { CharacterCodeDto } from './dtos/characterCodeDto';

describe('CharacterCodesController', () => {
  let controller: CharacterCodesController;
  let characterCodesService: CharacterCodesService;
  let gameCodesService: GameCodesService;

  const mockCharacterCodesService = {
    getCharacterCode: jest.fn(),
  };

  const mockGameCodesService = {
    getGameCode: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [CharacterCodesController],
      providers: [
        {
          provide: CharacterCodesService,
          useValue: mockCharacterCodesService,
        },
        {
          provide: GameCodesService,
          useValue: mockGameCodesService,
        },
      ],
    }).compile();

    controller = module.get<CharacterCodesController>(CharacterCodesController);
    characterCodesService = module.get<CharacterCodesService>(
      CharacterCodesService,
    );
    gameCodesService = module.get<GameCodesService>(GameCodesService);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('formatCharacterName', () => {
    it('should return character code for valid input', async () => {
      const gameName = 'tekken8';
      const characterName = 'kaz';
      const gameCode: GameCode = 'tekken8';
      const characterCode = 'kaz';
      mockGameCodesService.getGameCode.mockReturnValue(gameCode);
      mockCharacterCodesService.getCharacterCode.mockResolvedValue(
        characterCode,
      );

      const result = await controller.formatCharacterName(
        gameName,
        characterName,
      );

      expect(result).toBeInstanceOf(CharacterCodeDto);
      expect(result.characterCode).toBe(characterCode);
      expect(gameCodesService.getGameCode).toHaveBeenCalledWith(gameName);
      expect(characterCodesService.getCharacterCode).toHaveBeenCalledWith(
        characterName,
        gameCode,
      );
    });

    it('should throw BadRequestException for invalid game code', async () => {
      const gameName = 'invalid_game';
      const characterName = 'kaz';
      mockGameCodesService.getGameCode.mockImplementation(() => {
        throw new Error('Invalid game code');
      });

      await expect(
        controller.formatCharacterName(gameName, characterName),
      ).rejects.toThrow();
    });

    it('should throw BadRequestException for invalid character code', async () => {
      const gameName = 'tekken8';
      const characterName = 'nonexistent';
      const gameCode: GameCode = 'tekken8';
      mockGameCodesService.getGameCode.mockReturnValue(gameCode);
      mockCharacterCodesService.getCharacterCode.mockImplementation(() => {
        throw new Error('Invalid character code');
      });

      await expect(
        controller.formatCharacterName(gameName, characterName),
      ).rejects.toThrow();
    });
  });
});
