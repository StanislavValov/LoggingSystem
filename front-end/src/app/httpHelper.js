angular.module('httpHelper', [
])

    .service('errorCodes', function () {
        var errorCodesMap = {};
        errorCodesMap[0] = 'Client cannot connect to the server';
        errorCodesMap[400] = 'Wrong data was sent to the server.';
        errorCodesMap[404] = 'There was a problem with accessing application resources.';
        errorCodesMap[500] = 'There was an internal server error. Please wait few minutes and try again later. ';
        errorCodesMap[502] = 'Cannot access the remote resources. Please wait few minutes and try again later. ';
        return errorCodesMap;
    }).service('errorHandler', [
    'notification',
    'errorCodes',
    function (notification, errorCodes) {
        return {
            handle: function (response) {
                var messages = [];

                if (response && response.data && Array.isArray(response.data)) {
                    response.data.forEach(function (errorMessage) {
                        messages.push(errorMessage.message);
                    });
                    notification.add(messages);
                    return;
                }
                if (response && response.data) {
                    messages.push(response.data);

                    notification.add(messages);
                    return;
                }
                if (response.data && response.data.indexOf('<html>') === -1) {
                    messages.push(response.data);
                    notification.add(messages);
                    return;
                }
                if (response.status in errorCodes && !response.data) {
                    messages.push(errorCodes[response.status]);
                    notification.add(messages);
                    return;
                }
                if (response.data) {
                    notification.add([errorCodes[response.status]]);
                }
            }
        };
    }
]).factory('errorInterceptor', [
    '$rootScope',
    '$q',
    'errorHandler',
    function ($rootScope, $q, errorHandler) {
        return {
            'responseError': function (response) {
                errorHandler.handle(response);
                return $q.reject(response);
            }
        };
    }
]).service('requestService', [
    '$http',
    '$q',
    function ($http, $q) {
        return {
            sendRequest: function (method, url, config) {
                var defer = $q.defer();
                $http({
                    method: method,
                    url: url,
                    data: config
                }).success(function (data) {
                    defer.resolve(data);
                }).error(function (data) {
                    defer.reject(data);
                });
                return defer.promise;
            }
        };
    }
]).config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('errorInterceptor');
}]);