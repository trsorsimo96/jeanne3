(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('DepositDeleteController',DepositDeleteController);

    DepositDeleteController.$inject = ['$uibModalInstance', 'entity', 'Deposit'];

    function DepositDeleteController($uibModalInstance, entity, Deposit) {
        var vm = this;

        vm.deposit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Deposit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
