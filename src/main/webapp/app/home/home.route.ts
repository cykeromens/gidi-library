import { Route, Routes } from '@angular/router';

import { HomeComponent } from './';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';

export const HOME_ROUTE: Routes = [
    {
        path: '',
        component: HomeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [],
            pageTitle: 'Home',
            defaultSort: 'id,asc'
        }
    },
    {
        path: 'home',
        component: HomeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [],
            defaultSort: 'id,asc',
            pageTitle: 'Books'
        },
        canActivate: [UserRouteAccessService]
    }
];
