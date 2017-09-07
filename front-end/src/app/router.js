angular.module('router', ['ui.router'])

    .provider('pages', ['$stateProvider', function ($stateProvider) {
        this.contexts = [];

        this.$get = function () {
            return this.contexts;
        };

        this.addPage = function (page) {
            this.contexts.push(page);

            $stateProvider.state(page);
        };
    }]);