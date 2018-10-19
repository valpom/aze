import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IResultat, defaultValue } from 'app/shared/model/resultat.model';

export const ACTION_TYPES = {
  FETCH_RESULTAT_LIST: 'resultat/FETCH_RESULTAT_LIST',
  FETCH_RESULTAT: 'resultat/FETCH_RESULTAT',
  CREATE_RESULTAT: 'resultat/CREATE_RESULTAT',
  UPDATE_RESULTAT: 'resultat/UPDATE_RESULTAT',
  DELETE_RESULTAT: 'resultat/DELETE_RESULTAT',
  RESET: 'resultat/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IResultat>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ResultatState = Readonly<typeof initialState>;

// Reducer

export default (state: ResultatState = initialState, action): ResultatState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RESULTAT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RESULTAT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RESULTAT):
    case REQUEST(ACTION_TYPES.UPDATE_RESULTAT):
    case REQUEST(ACTION_TYPES.DELETE_RESULTAT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_RESULTAT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RESULTAT):
    case FAILURE(ACTION_TYPES.CREATE_RESULTAT):
    case FAILURE(ACTION_TYPES.UPDATE_RESULTAT):
    case FAILURE(ACTION_TYPES.DELETE_RESULTAT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESULTAT_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESULTAT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RESULTAT):
    case SUCCESS(ACTION_TYPES.UPDATE_RESULTAT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RESULTAT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/resultats';

// Actions

export const getEntities: ICrudGetAllAction<IResultat> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RESULTAT_LIST,
    payload: axios.get<IResultat>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IResultat> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RESULTAT,
    payload: axios.get<IResultat>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IResultat> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RESULTAT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IResultat> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RESULTAT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IResultat> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RESULTAT,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
