(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('MonthDeleteController',MonthDeleteController);

    MonthDeleteController.$inject = ['$uibModalInstance', 'entity', 'Month'];

    function MonthDeleteController($uibModalInstance, entity, Month) {
        var vm = this;

        vm.month = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Month.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
