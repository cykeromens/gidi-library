import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { BookRecordStatus, IBookRecord } from 'app/shared/model/book-record.model';
import { BookRecordService } from './book-record.service';
import { Account, IUser, Principal, UserService } from 'app/core';
import { BookStatus, IBook } from 'app/shared/model/book.model';
import { BookService } from 'app/entities/book';

@Component({
    selector: 'jhi-book-record-update',
    templateUrl: './book-record-update.component.html'
})
export class BookRecordUpdateComponent implements OnInit {
    bookRecord: IBookRecord;
    isSaving: boolean;
    account: Account;
    users: IUser[];
    book: IBook;
    borrowedAtDp: any;
    returnDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private bookRecordService: BookRecordService,
        private userService: UserService,
        private bookService: BookService,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ book }) => {
            this.book = book;
            this.bookRecord.book = book;
        });
        this.activatedRoute.data.subscribe(({ bookRecord }) => {
            this.bookRecord = bookRecord;
        });
        this.principal.identity().then(account => {
            this.account = account;
            this.userService.find(this.account.login).subscribe(
                (res: HttpResponse<IUser>) => {
                    this.bookRecord.user = res.body;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        });
        this.bookRecord.book = this.book;
        this.bookRecord.borrowedAt = moment();
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.bookRecord.status = BookRecordStatus.READING;
        if (this.bookRecord.id !== undefined) {
            this.subscribeToSaveResponse(this.bookRecordService.update(this.bookRecord));
        } else {
            this.subscribeToSaveResponse(this.bookRecordService.create(this.bookRecord));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBookRecord>>) {
        result.subscribe((res: HttpResponse<IBookRecord>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        if (this.book !== undefined) {
            this.book.status = BookStatus.IN_USE;
            this.bookService.update(this.book).subscribe(res => {
                this.book = res.body;
            });
        }
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackBookById(index: number, item: IBook) {
        return item.id;
    }
}
