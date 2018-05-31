(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('MonthDetailController', MonthDetailController);

    MonthDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Month'];

    function MonthDetailController($scope, $rootScope, $stateParams, previousState, entity, Month) {
        var vm = this;

        vm.month = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jeanne3App:monthUpdate', function(event, result) {
            vm.month = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
