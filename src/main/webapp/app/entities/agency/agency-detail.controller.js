(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('AgencyDetailController', AgencyDetailController);

    AgencyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Agency', 'Deposit', 'FeesAgency'];

    function AgencyDetailController($scope, $rootScope, $stateParams, previousState, entity, Agency, Deposit, FeesAgency) {
        var vm = this;

        vm.agency = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jeanne3App:agencyUpdate', function(event, result) {
            vm.agency = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
