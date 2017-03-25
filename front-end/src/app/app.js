angular.module('app', [
    'templates-app',
    'templates-common',
    'xeditable',
    'notification',
    'httpHelper',
    'router',
    'accounts'
])
    .run(function (editableOptions, editableThemes) {
        editableOptions.theme = 'bs3'; // set xeditable to use bootstrap3 theme

        editableThemes.bs3.inputClass = 'input-sm';
        editableThemes.bs3.buttonsClass = 'btn-sm';

        editableThemes['bs3'].submitTpl = '<button type="submit" style="margin-top: -1px" class="btn btn-info"><span class="icon-check"></span></button>';
        editableThemes['bs3'].cancelTpl = '<button type="button" style="margin-top: -1px" class="btn btn-default" ng-click="$form.$cancel()"><span class="icon-remove-circle"></span></button>';
    })
    .controller('NavbarCtrl', ["$rootScope", '$scope', 'pages', function ($rootScope, $scope, pages) {
        $scope.contexts = pages;
    }])
    .directive('navBarItem', function ($compile) {
        return {
            restrict: "EA",
            link: function (scope, element) {
                var el = angular.element('<a ui-sref="{{context.name}}" class="nav-bar-item-style">{{context.name}}</a>');
                $compile(el)(scope);
                element.append(el);
            }
        };
    });