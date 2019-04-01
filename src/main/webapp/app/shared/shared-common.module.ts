import { NgModule } from '@angular/core';

import { GidiLibrarySharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [GidiLibrarySharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [GidiLibrarySharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class GidiLibrarySharedCommonModule {}
