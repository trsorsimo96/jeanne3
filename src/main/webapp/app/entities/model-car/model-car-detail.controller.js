(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ModelCarDetailController', ModelCarDetailController);

    ModelCarDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ModelCar', 'Car'];

    function ModelCarDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ModelCar, Car) {
        var vm = this;

        vm.modelCar = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('jeanne3App:modelCarUpdate', function(event, result) {
            vm.modelCar = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
