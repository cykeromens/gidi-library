/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GidiLibraryTestModule } from '../../../test.module';
import { BookRecordDetailComponent } from 'app/entities/book-record/book-record-detail.component';
import { BookRecord } from 'app/shared/model/book-record.model';

describe('Component Tests', () => {
    describe('BookRecord Management Detail Component', () => {
        let comp: BookRecordDetailComponent;
        let fixture: ComponentFixture<BookRecordDetailComponent>;
        const route = ({ data: of({ bookRecord: new BookRecord(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GidiLibraryTestModule],
                declarations: [BookRecordDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BookRecordDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BookRecordDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.bookRecord).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
