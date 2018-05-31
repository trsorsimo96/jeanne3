(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ClasseDialogController', ClasseDialogController);

    ClasseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Classe', 'Itineraire', 'CompanyClasse'];

    function ClasseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Classe, Itineraire, CompanyClasse) {
        var vm = this;

        vm.classe = entity;
        vm.clear = clear;
        vm.save = save;
        vm.itineraires = Itineraire.query();
        vm.companyclasses = CompanyClasse.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.classe.id !== null) {
                Classe.update(vm.classe, onSaveSuccess, onSaveError);
            } else {
                Classe.save(vm.classe, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:classeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
