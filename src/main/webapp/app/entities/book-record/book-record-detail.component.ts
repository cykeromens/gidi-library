import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBookRecord } from 'app/shared/model/book-record.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Account, IUser, Principal } from 'app/core';

@Component({
    selector: 'jhi-book-record-detail',
    templateUrl: './book-record-detail.component.html'
})
export class BookRecordDetailComponent implements OnInit {
    bookRecord: IBookRecord;
    currentAccount: Account;

    constructor(private activatedRoute: ActivatedRoute, private principal: Principal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bookRecord }) => {
            this.bookRecord = bookRecord;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    previousState() {
        window.history.back();
    }
}
