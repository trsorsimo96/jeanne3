(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('PassengerDetailController', PassengerDetailController);

    PassengerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Passenger', 'Booking'];

    function PassengerDetailController($scope, $rootScope, $stateParams, previousState, entity, Passenger, Booking) {
        var vm = this;

        vm.passenger = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jeanne3App:passengerUpdate', function(event, result) {
            vm.passenger = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
