(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ModelTrainDetailController', ModelTrainDetailController);

    ModelTrainDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ModelTrain', 'Wagon'];

    function ModelTrainDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ModelTrain, Wagon) {
        var vm = this;

        vm.modelTrain = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('jeanne3App:modelTrainUpdate', function(event, result) {
            vm.modelTrain = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
