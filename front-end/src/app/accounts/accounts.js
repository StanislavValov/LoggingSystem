angular.module('accounts', [

])
    .config(['pagesProvider', function (pages) {
        var accounts = {name: "Accounts", url: "/accounts", templateUrl: "accounts/accounts.tpl.html"};

        pages.addPage(accounts);
    }])

    .service('accountService', [
        'requestService',
        function (requestService) {
            return {
                create: function (account) {
                    return requestService.sendRequest('POST', '/api/v1/accounts', account);
                },
                findAll: function () {
                    return requestService.sendRequest('GET', '/api/v1/accounts/findAll');
                },
                remove: function (nickname) {
                    return requestService.sendRequest('DELETE', '/api/v1/accounts/delete/' + nickname);
                },
                update: function (account) {
                    return requestService.sendRequest('PUT', '/api/v1/accounts/', account);
                },
                importImage: function (image, nickname) {
                    return requestService.sendRequest('POST', '/api/v1/accounts/'+ nickname +"/image/", image);
                }
            };
        }

    ])

    .controller('AccountsCtrl', [
        '$scope',
        'notification',
        'accountService',
        function ($scope, notification, accountService) {
            accountService.findAll().then(function (result) {
                $scope.accounts = result;
            });

            $scope.create = function (account) {
                accountService.create(account).then(function (response) {
                    notification.add([response]);

                    $scope.findAll();
                });
            };

            $scope.remove = function (id, nickname) {
                $scope.accounts.splice(id, 1);
                accountService.remove(nickname).then(function (result) {
                    notification.add([result]);
                });
            };

            $scope.update = function (account) {
                accountService.update(account).then(function (result) {
                    notification.add([result]);

                    $scope.findAll();
                });
            };

            $scope.findAll = function () {
                accountService.findAll().then(function (result) {
                    $scope.accounts = result;
                });
            };

            $scope.importImage = function (nickname) {
                var file = document.createElement("input");
                file.setAttribute("type", "file");
                file.click();
                angular.element(file).bind('change', function (event) {
                    accountService.importImage(event.path[0].files[0], nickname).then(function (data) {
                        notification.add([data]);

                        $scope.findAll();
                    });
                });
            };

            $scope.getImage = function(account){
                return 'data:image/png;base64,' + account.imageData;
            };
        }
    ]);
