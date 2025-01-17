import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PersonalDetails from './personal-details';
import PersonalDetailsDetail from './personal-details-detail';
import PersonalDetailsUpdate from './personal-details-update';
import PersonalDetailsDeleteDialog from './personal-details-delete-dialog';

const PersonalDetailsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PersonalDetails />} />
    <Route path="new" element={<PersonalDetailsUpdate />} />
    <Route path=":id">
      <Route index element={<PersonalDetailsDetail />} />
      <Route path="edit" element={<PersonalDetailsUpdate />} />
      <Route path="delete" element={<PersonalDetailsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PersonalDetailsRoutes;
