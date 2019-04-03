import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { BookRecord } from 'app/shared/model/book-record.model';
import { BookRecordService } from './book-record.service';
import { BookRecordComponent } from './book-record.component';
import { BookRecordDetailComponent } from './book-record-detail.component';
import { BookRecordUpdateComponent } from './book-record-update.component';
import { BookRecordDeletePopupComponent } from './book-record-delete-dialog.component';
import { IBookRecord } from 'app/shared/model/book-record.model';
import { Book, IBook } from 'app/shared/model/book.model';
import { BookResolve, BookService } from 'app/entities/book';

@Injectable({ providedIn: 'root' })
export class BookRecordResolve implements Resolve<IBookRecord> {
    constructor(private service: BookRecordService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const record_id = route.params['record_id'] ? route.params['record_id'] : null;
        if (record_id) {
            return this.service.find(record_id).pipe(map((bookRecord: HttpResponse<BookRecord>) => bookRecord.body));
        }
        return of(new BookRecord());
    }
}

export const bookRecordRoute: Routes = [
    {
        path: 'book-record',
        component: BookRecordComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'BookRecords',
            pageLabel: 'List'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'book-record/:record_id/view',
        component: BookRecordDetailComponent,
        resolve: {
            bookRecord: BookRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookRecords',
            pageLabel: 'Detail'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'book-record/:id/new',
        component: BookRecordUpdateComponent,
        resolve: {
            book: BookResolve,
            bookRecord: BookRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookRecords',
            pageLabel: 'New'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'book-record/:record_id/edit',
        component: BookRecordUpdateComponent,
        resolve: {
            book: BookResolve,
            bookRecord: BookRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookRecords',
            pageLabel: 'Edit'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bookRecordPopupRoute: Routes = [
    {
        path: 'book-record/:record_id/delete',
        component: BookRecordDeletePopupComponent,
        resolve: {
            bookRecord: BookRecordResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'BookRecords',
            pageLabel: 'Delete'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
