import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Resultat from './resultat';
import ResultatDetail from './resultat-detail';
import ResultatUpdate from './resultat-update';
import ResultatDeleteDialog from './resultat-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ResultatUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ResultatUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ResultatDetail} />
      <ErrorBoundaryRoute path={match.url} component={Resultat} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ResultatDeleteDialog} />
  </>
);

export default Routes;
