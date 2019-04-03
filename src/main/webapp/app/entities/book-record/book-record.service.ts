import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBookRecord } from 'app/shared/model/book-record.model';

type EntityResponseType = HttpResponse<IBookRecord>;
type EntityArrayResponseType = HttpResponse<IBookRecord[]>;

@Injectable({ providedIn: 'root' })
export class BookRecordService {
    private resourceUrl = SERVER_API_URL + 'api/book-records';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/book-records';

    constructor(private http: HttpClient) {}

    create(bookRecord: IBookRecord): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(bookRecord);
        return this.http
            .post<IBookRecord>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(bookRecord: IBookRecord): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(bookRecord);
        return this.http
            .put<IBookRecord>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IBookRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBookRecord[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBookRecord[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(bookRecord: IBookRecord): IBookRecord {
        const copy: IBookRecord = Object.assign({}, bookRecord, {
            borrowedAt: bookRecord.borrowedAt != null && bookRecord.borrowedAt.isValid() ? bookRecord.borrowedAt.format(DATE_FORMAT) : null,
            returnDate: bookRecord.returnDate != null && bookRecord.returnDate.isValid() ? bookRecord.returnDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.borrowedAt = res.body.borrowedAt != null ? moment(res.body.borrowedAt) : null;
        res.body.returnDate = res.body.returnDate != null ? moment(res.body.returnDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((bookRecord: IBookRecord) => {
            bookRecord.borrowedAt = bookRecord.borrowedAt != null ? moment(bookRecord.borrowedAt) : null;
            bookRecord.returnDate = bookRecord.returnDate != null ? moment(bookRecord.returnDate) : null;
        });
        return res;
    }
}
