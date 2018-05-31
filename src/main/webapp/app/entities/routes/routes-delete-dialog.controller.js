(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('RoutesDeleteController',RoutesDeleteController);

    RoutesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Routes'];

    function RoutesDeleteController($uibModalInstance, entity, Routes) {
        var vm = this;

        vm.routes = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Routes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
