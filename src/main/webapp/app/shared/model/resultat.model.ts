import { IUser } from 'app/shared/model/user.model';
import { IRace } from 'app/shared/model//race.model';

export interface IResultat {
  id?: number;
  chrono?: number;
  user?: IUser;
  race?: IRace;
}

export const defaultValue: Readonly<IResultat> = {};
