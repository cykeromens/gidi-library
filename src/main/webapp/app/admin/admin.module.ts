import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GidiLibrarySharedModule } from 'app/shared';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

import { adminState, UserMgmtComponent, UserMgmtDetailComponent, UserMgmtUpdateComponent, UserMgmtDeleteDialogComponent } from './';

@NgModule({
    imports: [
        GidiLibrarySharedModule,
        RouterModule.forChild(adminState)
        /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    ],
    declarations: [UserMgmtComponent, UserMgmtDetailComponent, UserMgmtUpdateComponent, UserMgmtDeleteDialogComponent],
    entryComponents: [UserMgmtDeleteDialogComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GidiLibraryAdminModule {}
