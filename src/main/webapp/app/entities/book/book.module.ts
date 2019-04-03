import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GidiLibrarySharedModule } from 'app/shared';
import {
    BookComponent,
    BookDetailComponent,
    BookUpdateComponent,
    BookDeletePopupComponent,
    BookDeleteDialogComponent,
    BookDetailInfoComponent,
    bookRoute,
    bookPopupRoute
} from './';

const ENTITY_STATES = [...bookRoute, ...bookPopupRoute];

@NgModule({
    imports: [GidiLibrarySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BookComponent,
        BookDetailComponent,
        BookUpdateComponent,
        BookDetailInfoComponent,
        BookDeleteDialogComponent,
        BookDeletePopupComponent
    ],
    entryComponents: [BookComponent, BookUpdateComponent, BookDeleteDialogComponent, BookDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GidiLibraryBookModule {}
