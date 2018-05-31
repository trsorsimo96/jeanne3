(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('FeesAgencyDeleteController',FeesAgencyDeleteController);

    FeesAgencyDeleteController.$inject = ['$uibModalInstance', 'entity', 'FeesAgency'];

    function FeesAgencyDeleteController($uibModalInstance, entity, FeesAgency) {
        var vm = this;

        vm.feesAgency = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FeesAgency.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
