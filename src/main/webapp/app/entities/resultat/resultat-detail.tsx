import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './resultat.reducer';
import { IResultat } from 'app/shared/model/resultat.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IResultatDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ResultatDetail extends React.Component<IResultatDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { resultatEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Resultat [<b>{resultatEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="chrono">Chrono</span>
            </dt>
            <dd>{resultatEntity.chrono}</dd>
            <dt>User</dt>
            <dd>{resultatEntity.user ? resultatEntity.user.login : ''}</dd>
            <dt>Race</dt>
            <dd>{resultatEntity.race ? resultatEntity.race.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/resultat" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/resultat/${resultatEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ resultat }: IRootState) => ({
  resultatEntity: resultat.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ResultatDetail);
