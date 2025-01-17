import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './personal-details.reducer';

export const PersonalDetailsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const personalDetailsEntity = useAppSelector(state => state.personalDetails.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="personalDetailsDetailsHeading">
          <Translate contentKey="investorRiskReturnProfilingApp.personalDetails.detail.title">PersonalDetails</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{personalDetailsEntity.id}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="investorRiskReturnProfilingApp.personalDetails.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{personalDetailsEntity.fullName}</dd>
          <dt>
            <span id="dateOfBirth">
              <Translate contentKey="investorRiskReturnProfilingApp.personalDetails.dateOfBirth">Date Of Birth</Translate>
            </span>
          </dt>
          <dd>
            {personalDetailsEntity.dateOfBirth ? (
              <TextFormat value={personalDetailsEntity.dateOfBirth} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="comments">
              <Translate contentKey="investorRiskReturnProfilingApp.personalDetails.comments">Comments</Translate>
            </span>
          </dt>
          <dd>{personalDetailsEntity.comments}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="investorRiskReturnProfilingApp.personalDetails.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {personalDetailsEntity.createdAt ? (
              <TextFormat value={personalDetailsEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="investorRiskReturnProfilingApp.personalDetails.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {personalDetailsEntity.updatedAt ? (
              <TextFormat value={personalDetailsEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/personal-details" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/personal-details/${personalDetailsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PersonalDetailsDetail;
