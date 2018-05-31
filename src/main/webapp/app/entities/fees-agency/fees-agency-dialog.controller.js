(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('FeesAgencyDialogController', FeesAgencyDialogController);

    FeesAgencyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FeesAgency', 'Company', 'Agency'];

    function FeesAgencyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FeesAgency, Company, Agency) {
        var vm = this;

        vm.feesAgency = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        vm.agencies = Agency.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.feesAgency.id !== null) {
                FeesAgency.update(vm.feesAgency, onSaveSuccess, onSaveError);
            } else {
                FeesAgency.save(vm.feesAgency, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:feesAgencyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
