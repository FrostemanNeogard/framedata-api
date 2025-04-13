export const validGameCodes = [
  'tekken6',
  'tekkentag2',
  'tekken7',
  'tekken8',
] as const;
export type GameCode = (typeof validGameCodes)[number];
