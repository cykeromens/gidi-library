import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BookRecordStatus, IBookRecord } from 'app/shared/model/book-record.model';
import { BookRecordService } from './book-record.service';
import { Observable } from 'rxjs';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { BookService } from 'app/entities/book';
import { BookStatus, IBook } from 'app/shared/model/book.model';

@Component({
    selector: 'jhi-book-record-delete-dialog',
    templateUrl: './book-record-delete-dialog.component.html'
})
export class BookRecordDeleteDialogComponent {
    bookRecord: IBookRecord;
    book: IBook;

    constructor(
        private bookRecordService: BookRecordService,
        private bookService: BookService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.bookRecordService.find(id).subscribe(res => {
            this.bookRecord = res.body;
            this.bookRecord.status = BookRecordStatus.CANCELLED;
            this.subscribeToSaveBookResponse(this.bookRecordService.update(this.bookRecord));
        });
    }

    private subscribeToSaveBookResponse(result: Observable<HttpResponse<IBookRecord>>) {
        result.subscribe((res: HttpResponse<IBookRecord>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.bookService.find(this.bookRecord.book.id).subscribe(res => {
            this.book = res.body;
            this.book.status = BookStatus.AVAILABLE;
            this.bookService.update(this.book).subscribe(next => {
                this.book = next.body;
                this.eventManager.broadcast({
                    name: 'bookRecordListModification',
                    content: 'Suspended ' + this.bookRecord.user.login + ' from reading ' + this.book.title
                });
                this.activeModal.dismiss(true);
            });
        });
    }

    private onSaveError() {
        this.activeModal.dismiss(false);
    }
}

@Component({
    selector: 'jhi-book-record-delete-popup',
    template: ''
})
export class BookRecordDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bookRecord }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BookRecordDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.bookRecord = bookRecord;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
