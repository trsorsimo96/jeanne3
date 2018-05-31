(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ModelCarDeleteController',ModelCarDeleteController);

    ModelCarDeleteController.$inject = ['$uibModalInstance', 'entity', 'ModelCar'];

    function ModelCarDeleteController($uibModalInstance, entity, ModelCar) {
        var vm = this;

        vm.modelCar = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ModelCar.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
