import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IBook } from 'app/shared/model//book.model';

export const enum BookRecordStatus {
    RETURNED = 'RETURNED',
    READING = 'READING',
    CANCELLED = 'CANCELLED'
}

export interface IBookRecord {
    id?: number;
    intention?: string;
    borrowedAt?: Moment;
    returnDate?: Moment;
    status?: BookRecordStatus;
    user?: IUser;
    book?: IBook;
}

export class BookRecord implements IBookRecord {
    constructor(
        public id?: number,
        public intention?: string,
        public borrowedAt?: Moment,
        public returnDate?: Moment,
        public status?: BookRecordStatus,
        public user?: IUser,
        public book?: IBook
    ) {}
}
