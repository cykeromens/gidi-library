import { Routes } from '@angular/router';

import { userMgmtRoute } from './';

import { UserRouteAccessService } from 'app/core';

const ADMIN_ROUTES = [...userMgmtRoute];

export const adminState: Routes = [
    {
        path: '',
        data: {
            authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        children: ADMIN_ROUTES
    }
];
