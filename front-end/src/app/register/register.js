angular.module('register', [])

    .config(['pagesProvider', function (pages) {
        var register = {name: "Register", url: "/register", templateUrl: "register.tpl.html", controller: 'RegisterCtrl'};

        pages.addPage(register);
    }])

    .service('registerService', [
        'requestService',
        function (requestService) {
            return {
                register: function (user) {
                    return requestService.sendRequest('POST', '/api/v1/register', user);
                }
            };
        }
    ])

    .controller('RegisterCtrl', [
        '$scope',
        'notification',
        'registerService',
        function ($scope, notification, registerService) {
console.log("asdadasdasd");
            $scope.register = function (user) {
                registerService.register(user).then(function (response) {
                    notification.add([response]);
                });
            };
        }
    ]);
