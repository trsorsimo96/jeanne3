(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('BookingDialogController', BookingDialogController);

    BookingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Booking', 'Passenger', 'Fare', 'Email', 'Segment'];

    function BookingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Booking, Passenger, Fare, Email, Segment) {
        var vm = this;

        vm.booking = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.passengers = Passenger.query();
        vm.fares = Fare.query();
        vm.emails = Email.query();
        vm.segments = Segment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.booking.id !== null) {
                Booking.update(vm.booking, onSaveSuccess, onSaveError);
            } else {
                Booking.save(vm.booking, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:bookingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.bookingDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
