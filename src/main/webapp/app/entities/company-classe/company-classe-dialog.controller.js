(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('CompanyClasseDialogController', CompanyClasseDialogController);

    CompanyClasseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanyClasse', 'Company', 'Classe'];

    function CompanyClasseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CompanyClasse, Company, Classe) {
        var vm = this;

        vm.companyClasse = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        vm.classes = Classe.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.companyClasse.id !== null) {
                CompanyClasse.update(vm.companyClasse, onSaveSuccess, onSaveError);
            } else {
                CompanyClasse.save(vm.companyClasse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:companyClasseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
