angular.module('login', [])

    .service('loginService', [
        'requestService',
        function (requestService) {
            return {
                authenticate: function (user) {
                    return requestService.sendRequest('POST', '/api/v1/login/authenticate', user);
                }
            };
        }
    ])

    .controller('LoginCtrl', [
    '$scope',
    '$location',
    'notification',
    'loginService',
    function ($scope, $location, notification, loginService) {

        $scope.authenticate = function (user) {
            loginService.authenticate(user).then(function (response) {
                notification.add([response]);
                $location.path('/accounts');
            });
        };
    }
]);
