(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ModelTrainDialogController', ModelTrainDialogController);

    ModelTrainDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ModelTrain', 'Wagon'];

    function ModelTrainDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ModelTrain, Wagon) {
        var vm = this;

        vm.modelTrain = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.wagons = Wagon.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.modelTrain.id !== null) {
                ModelTrain.update(vm.modelTrain, onSaveSuccess, onSaveError);
            } else {
                ModelTrain.save(vm.modelTrain, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:modelTrainUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, modelTrain) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        modelTrain.image = base64Data;
                        modelTrain.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
