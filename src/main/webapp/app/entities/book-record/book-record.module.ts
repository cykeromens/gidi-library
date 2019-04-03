import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GidiLibrarySharedModule } from 'app/shared';
import { GidiLibraryAdminModule } from 'app/admin/admin.module';
import {
    BookRecordComponent,
    BookRecordDetailComponent,
    BookRecordUpdateComponent,
    BookRecordDeletePopupComponent,
    BookRecordDeleteDialogComponent,
    bookRecordRoute,
    bookRecordPopupRoute
} from './';

const ENTITY_STATES = [...bookRecordRoute, ...bookRecordPopupRoute];

@NgModule({
    imports: [GidiLibrarySharedModule, GidiLibraryAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BookRecordComponent,
        BookRecordDetailComponent,
        BookRecordUpdateComponent,
        BookRecordDeleteDialogComponent,
        BookRecordDeletePopupComponent
    ],
    entryComponents: [BookRecordComponent, BookRecordUpdateComponent, BookRecordDeleteDialogComponent, BookRecordDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GidiLibraryBookRecordModule {}
