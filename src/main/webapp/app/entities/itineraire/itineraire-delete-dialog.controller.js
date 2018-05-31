(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ItineraireDeleteController',ItineraireDeleteController);

    ItineraireDeleteController.$inject = ['$uibModalInstance', 'entity', 'Itineraire'];

    function ItineraireDeleteController($uibModalInstance, entity, Itineraire) {
        var vm = this;

        vm.itineraire = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Itineraire.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
