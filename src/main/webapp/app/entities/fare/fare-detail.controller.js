(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('FareDetailController', FareDetailController);

    FareDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Fare', 'Booking'];

    function FareDetailController($scope, $rootScope, $stateParams, previousState, entity, Fare, Booking) {
        var vm = this;

        vm.fare = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jeanne3App:fareUpdate', function(event, result) {
            vm.fare = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
