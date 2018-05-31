(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('VoyageDialogController', VoyageDialogController);

    VoyageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Voyage', 'Segment', 'Routes', 'Car', 'Train', 'Itineraire'];

    function VoyageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Voyage, Segment, Routes, Car, Train, Itineraire) {
        var vm = this;

        vm.voyage = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.segments = Segment.query();
        vm.routes = Routes.query();
        vm.cars = Car.query();
        vm.trains = Train.query();
        vm.itineraires = Itineraire.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.voyage.id !== null) {
                Voyage.update(vm.voyage, onSaveSuccess, onSaveError);
            } else {
                Voyage.save(vm.voyage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:voyageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.datedepart = false;
        vm.datePickerOpenStatus.datearrive = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
