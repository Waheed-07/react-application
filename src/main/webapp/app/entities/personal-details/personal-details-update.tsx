import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './personal-details.reducer';

export const PersonalDetailsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const personalDetailsEntity = useAppSelector(state => state.personalDetails.entity);
  const loading = useAppSelector(state => state.personalDetails.loading);
  const updating = useAppSelector(state => state.personalDetails.updating);
  const updateSuccess = useAppSelector(state => state.personalDetails.updateSuccess);

  const handleClose = () => {
    navigate('/personal-details');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.dateOfBirth = convertDateTimeToServer(values.dateOfBirth);
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...personalDetailsEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateOfBirth: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...personalDetailsEntity,
          dateOfBirth: convertDateTimeFromServer(personalDetailsEntity.dateOfBirth),
          createdAt: convertDateTimeFromServer(personalDetailsEntity.createdAt),
          updatedAt: convertDateTimeFromServer(personalDetailsEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="investorRiskReturnProfilingApp.personalDetails.home.createOrEditLabel" data-cy="PersonalDetailsCreateUpdateHeading">
            <Translate contentKey="investorRiskReturnProfilingApp.personalDetails.home.createOrEditLabel">
              Create or edit a PersonalDetails
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="personal-details-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('investorRiskReturnProfilingApp.personalDetails.fullName')}
                id="personal-details-fullName"
                name="fullName"
                data-cy="fullName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('investorRiskReturnProfilingApp.personalDetails.dateOfBirth')}
                id="personal-details-dateOfBirth"
                name="dateOfBirth"
                data-cy="dateOfBirth"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('investorRiskReturnProfilingApp.personalDetails.comments')}
                id="personal-details-comments"
                name="comments"
                data-cy="comments"
                type="textarea"
              />
              <ValidatedField
                label={translate('investorRiskReturnProfilingApp.personalDetails.createdAt')}
                id="personal-details-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('investorRiskReturnProfilingApp.personalDetails.updatedAt')}
                id="personal-details-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/personal-details" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PersonalDetailsUpdate;
