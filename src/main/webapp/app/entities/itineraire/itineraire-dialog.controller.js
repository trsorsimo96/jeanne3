(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ItineraireDialogController', ItineraireDialogController);

    ItineraireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Itineraire', 'Voyage', 'Routes', 'Classe', 'Company', 'Day'];

    function ItineraireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Itineraire, Voyage, Routes, Classe, Company, Day) {
        var vm = this;

        vm.itineraire = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.voyages = Voyage.query();
        vm.routes = Routes.query();
        vm.classes = Classe.query();
        vm.companies = Company.query();
        vm.days = Day.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.itineraire.id !== null) {
                Itineraire.update(vm.itineraire, onSaveSuccess, onSaveError);
            } else {
                Itineraire.save(vm.itineraire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:itineraireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateDepart = false;
        vm.datePickerOpenStatus.dateArrive = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
