(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('VoyageDeleteController',VoyageDeleteController);

    VoyageDeleteController.$inject = ['$uibModalInstance', 'entity', 'Voyage'];

    function VoyageDeleteController($uibModalInstance, entity, Voyage) {
        var vm = this;

        vm.voyage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Voyage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
