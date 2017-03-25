angular.module('notification', []).constant('notifyDuration', 10000).factory('notification', [
  '$rootScope',
  '$timeout',
  'notifyDuration',
  function ($rootScope, $timeout, notifyDuration) {
    var reset;
    var emit = function (messages) {
      $rootScope.$emit('notification:message', messages);
    };
    return {
      add: function (messages) {
        $timeout.cancel(reset);
        emit(messages);
        reset = $timeout(function () {
          emit('');
        }, notifyDuration);
      }
    };
  }
]).directive('notificationBox', [function () {
    var directive = {
        restrict: 'EA',
        replace: true,
        template: '<div ng-if="messages.length" class="notification-message">' + '<div class="row" ng-repeat="message in messages"><div class="col-md-12">{{message}}</div></div>' + '</div>'
      };
    directive.controller = [
      '$scope',
      '$rootScope',
      function ($scope, $rootScope) {
        $rootScope.$on('notification:message', function (_, messages) {
          $scope.messages = messages;
        });
      }
    ];
    return directive;
  }]);