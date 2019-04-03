/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { BookRecordService } from 'app/entities/book-record/book-record.service';
import { IBookRecord, BookRecord, BookRecordStatus } from 'app/shared/model/book-record.model';

describe('Service Tests', () => {
    describe('BookRecord Service', () => {
        let injector: TestBed;
        let service: BookRecordService;
        let httpMock: HttpTestingController;
        let elemDefault: IBookRecord;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(BookRecordService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new BookRecord(0, 'AAAAAAA', currentDate, currentDate, BookRecordStatus.RETURNED);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        borrowedAt: currentDate.format(DATE_FORMAT),
                        returnDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a BookRecord', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        borrowedAt: currentDate.format(DATE_FORMAT),
                        returnDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        borrowedAt: currentDate,
                        returnDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new BookRecord(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a BookRecord', async () => {
                const returnedFromService = Object.assign(
                    {
                        intention: 'BBBBBB',
                        borrowedAt: currentDate.format(DATE_FORMAT),
                        returnDate: currentDate.format(DATE_FORMAT),
                        status: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        borrowedAt: currentDate,
                        returnDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of BookRecord', async () => {
                const returnedFromService = Object.assign(
                    {
                        intention: 'BBBBBB',
                        borrowedAt: currentDate.format(DATE_FORMAT),
                        returnDate: currentDate.format(DATE_FORMAT),
                        status: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        borrowedAt: currentDate,
                        returnDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(take(1), map(resp => resp.body))
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a BookRecord', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
