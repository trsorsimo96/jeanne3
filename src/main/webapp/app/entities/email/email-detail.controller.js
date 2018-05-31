(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('EmailDetailController', EmailDetailController);

    EmailDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Email', 'Booking'];

    function EmailDetailController($scope, $rootScope, $stateParams, previousState, entity, Email, Booking) {
        var vm = this;

        vm.email = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jeanne3App:emailUpdate', function(event, result) {
            vm.email = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
