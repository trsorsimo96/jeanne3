(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ModelTrainDeleteController',ModelTrainDeleteController);

    ModelTrainDeleteController.$inject = ['$uibModalInstance', 'entity', 'ModelTrain'];

    function ModelTrainDeleteController($uibModalInstance, entity, ModelTrain) {
        var vm = this;

        vm.modelTrain = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ModelTrain.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
