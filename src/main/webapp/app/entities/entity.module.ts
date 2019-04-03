import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GidiLibraryBookModule } from './book/book.module';
import { GidiLibraryBookRecordModule } from './book-record/book-record.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        GidiLibraryBookModule,
        GidiLibraryBookRecordModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GidiLibraryEntityModule {}
