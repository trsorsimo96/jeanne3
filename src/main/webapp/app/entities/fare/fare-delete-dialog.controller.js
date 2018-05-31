(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('FareDeleteController',FareDeleteController);

    FareDeleteController.$inject = ['$uibModalInstance', 'entity', 'Fare'];

    function FareDeleteController($uibModalInstance, entity, Fare) {
        var vm = this;

        vm.fare = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Fare.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
