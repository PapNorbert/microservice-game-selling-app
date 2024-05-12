export interface GameDisc {
  name: string;
  type: string;
  gameYear: number | undefined;
}

export interface GameDiscWithId {
  entityId: number;
  name: string;
  type: string;
  gameYear: number;
}