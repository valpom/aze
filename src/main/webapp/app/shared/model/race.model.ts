import { IResultat } from 'app/shared/model//resultat.model';

export interface IRace {
  id?: number;
  title?: string;
  blogs?: IResultat[];
}

export const defaultValue: Readonly<IRace> = {};
