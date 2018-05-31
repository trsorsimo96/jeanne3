(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ClasseDeleteController',ClasseDeleteController);

    ClasseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Classe'];

    function ClasseDeleteController($uibModalInstance, entity, Classe) {
        var vm = this;

        vm.classe = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Classe.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
