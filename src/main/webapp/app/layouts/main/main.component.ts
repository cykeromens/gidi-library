import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';

import { Title } from '@angular/platform-browser';
import { Principal } from 'app/core';

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html'
})
export class JhiMainComponent implements OnInit {
    account: any;
    mainTitle: any;
    firstChild: ActivatedRouteSnapshot;
    secondChild: ActivatedRouteSnapshot;

    constructor(private titleService: Title, private router: Router, private principal: Principal) {}

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        const title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'Gidi Library';
        if (routeSnapshot.firstChild) {
            this.firstChild = routeSnapshot.firstChild;
            this.secondChild = routeSnapshot.firstChild;
        }
        this.mainTitle = title;
        return title;
    }

    ngOnInit() {
        this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.titleService.setTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
        });

        this.principal.identity().then(account => {
            this.account = account;
        });
    }
}
