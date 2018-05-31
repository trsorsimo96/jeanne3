(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('SegmentDetailController', SegmentDetailController);

    SegmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Segment', 'Voyage', 'Booking'];

    function SegmentDetailController($scope, $rootScope, $stateParams, previousState, entity, Segment, Voyage, Booking) {
        var vm = this;

        vm.segment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jeanne3App:segmentUpdate', function(event, result) {
            vm.segment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
