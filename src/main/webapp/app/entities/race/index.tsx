import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Race from './race';
import RaceDetail from './race-detail';
import RaceUpdate from './race-update';
import RaceDeleteDialog from './race-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RaceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RaceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RaceDetail} />
      <ErrorBoundaryRoute path={match.url} component={Race} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RaceDeleteDialog} />
  </>
);

export default Routes;
