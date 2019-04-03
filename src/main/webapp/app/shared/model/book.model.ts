import { Moment } from 'moment';

export const enum BookStatus {
    AVAILABLE = 'AVAILABLE',
    IN_USE = 'IN_USE',
    NOT_AVAILABLE = 'NOT_AVAILABLE'
}

export interface IBook {
    id?: number;
    title?: string;
    description?: string;
    isbn?: string;
    author?: string;
    summary?: string;
    datePublished?: Moment;
    status?: BookStatus;
    imageContentType?: string;
    image?: any;
}

export class Book implements IBook {
    constructor(
        public id?: number,
        public title?: string,
        public description?: string,
        public isbn?: string,
        public author?: string,
        public summary?: string,
        public datePublished?: Moment,
        public status?: BookStatus,
        public imageContentType?: string,
        public image?: any
    ) {}
}
