(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('SegmentDialogController', SegmentDialogController);

    SegmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Segment', 'Voyage', 'Booking'];

    function SegmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Segment, Voyage, Booking) {
        var vm = this;

        vm.segment = entity;
        vm.clear = clear;
        vm.save = save;
        vm.voyages = Voyage.query();
        vm.bookings = Booking.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.segment.id !== null) {
                Segment.update(vm.segment, onSaveSuccess, onSaveError);
            } else {
                Segment.save(vm.segment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:segmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
