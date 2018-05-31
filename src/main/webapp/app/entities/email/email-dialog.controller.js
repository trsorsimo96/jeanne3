(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('EmailDialogController', EmailDialogController);

    EmailDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Email', 'Booking'];

    function EmailDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Email, Booking) {
        var vm = this;

        vm.email = entity;
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
            if (vm.email.id !== null) {
                Email.update(vm.email, onSaveSuccess, onSaveError);
            } else {
                Email.save(vm.email, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:emailUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
