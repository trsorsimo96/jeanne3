(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('WagonDetailController', WagonDetailController);

    WagonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Wagon', 'ModelTrain', 'Train'];

    function WagonDetailController($scope, $rootScope, $stateParams, previousState, entity, Wagon, ModelTrain, Train) {
        var vm = this;

        vm.wagon = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jeanne3App:wagonUpdate', function(event, result) {
            vm.wagon = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
