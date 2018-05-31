(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('FareDialogController', FareDialogController);

    FareDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fare', 'Booking'];

    function FareDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Fare, Booking) {
        var vm = this;

        vm.fare = entity;
        vm.clear = clear;
        vm.save = save;
        vm.bookings = Booking.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fare.id !== null) {
                Fare.update(vm.fare, onSaveSuccess, onSaveError);
            } else {
                Fare.save(vm.fare, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:fareUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
