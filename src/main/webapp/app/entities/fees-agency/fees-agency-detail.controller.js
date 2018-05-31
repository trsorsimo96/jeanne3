(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('FeesAgencyDetailController', FeesAgencyDetailController);

    FeesAgencyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FeesAgency', 'Company', 'Agency'];

    function FeesAgencyDetailController($scope, $rootScope, $stateParams, previousState, entity, FeesAgency, Company, Agency) {
        var vm = this;

        vm.feesAgency = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jeanne3App:feesAgencyUpdate', function(event, result) {
            vm.feesAgency = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
