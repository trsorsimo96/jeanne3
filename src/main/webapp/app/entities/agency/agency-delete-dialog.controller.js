(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('AgencyDeleteController',AgencyDeleteController);

    AgencyDeleteController.$inject = ['$uibModalInstance', 'entity', 'Agency'];

    function AgencyDeleteController($uibModalInstance, entity, Agency) {
        var vm = this;

        vm.agency = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Agency.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
