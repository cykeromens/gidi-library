/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { GidiLibraryTestModule } from '../../../test.module';
import { BookRecordUpdateComponent } from 'app/entities/book-record/book-record-update.component';
import { BookRecordService } from 'app/entities/book-record/book-record.service';
import { BookRecord } from 'app/shared/model/book-record.model';

describe('Component Tests', () => {
    describe('BookRecord Management Update Component', () => {
        let comp: BookRecordUpdateComponent;
        let fixture: ComponentFixture<BookRecordUpdateComponent>;
        let service: BookRecordService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GidiLibraryTestModule],
                declarations: [BookRecordUpdateComponent]
            })
                .overrideTemplate(BookRecordUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BookRecordUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookRecordService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new BookRecord(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.bookRecord = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new BookRecord();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.bookRecord = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
