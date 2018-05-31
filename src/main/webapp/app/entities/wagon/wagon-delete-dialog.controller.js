(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('WagonDeleteController',WagonDeleteController);

    WagonDeleteController.$inject = ['$uibModalInstance', 'entity', 'Wagon'];

    function WagonDeleteController($uibModalInstance, entity, Wagon) {
        var vm = this;

        vm.wagon = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Wagon.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
