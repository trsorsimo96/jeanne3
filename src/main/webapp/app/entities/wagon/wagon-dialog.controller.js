(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('WagonDialogController', WagonDialogController);

    WagonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Wagon', 'ModelTrain', 'Train'];

    function WagonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Wagon, ModelTrain, Train) {
        var vm = this;

        vm.wagon = entity;
        vm.clear = clear;
        vm.save = save;
        vm.modeltrains = ModelTrain.query();
        vm.trains = Train.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wagon.id !== null) {
                Wagon.update(vm.wagon, onSaveSuccess, onSaveError);
            } else {
                Wagon.save(vm.wagon, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:wagonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
