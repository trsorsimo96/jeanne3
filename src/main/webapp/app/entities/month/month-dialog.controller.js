(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('MonthDialogController', MonthDialogController);

    MonthDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Month'];

    function MonthDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Month) {
        var vm = this;

        vm.month = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.month.id !== null) {
                Month.update(vm.month, onSaveSuccess, onSaveError);
            } else {
                Month.save(vm.month, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:monthUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
