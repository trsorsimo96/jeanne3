(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('PassengerDialogController', PassengerDialogController);

    PassengerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Passenger', 'Booking'];

    function PassengerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Passenger, Booking) {
        var vm = this;

        vm.passenger = entity;
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
            if (vm.passenger.id !== null) {
                Passenger.update(vm.passenger, onSaveSuccess, onSaveError);
            } else {
                Passenger.save(vm.passenger, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:passengerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
