import { TekkenMoveCategory } from './moveCategories';

export type FrameData = {
  input: string;
  hit_level: string;
  damage: string;
  startup: string;
  block: string;
  hit: string;
  counter: string;
  notes: string[];
  name: string;
  alternateInputs: string[];
  categories: TekkenMoveCategory[];
};
