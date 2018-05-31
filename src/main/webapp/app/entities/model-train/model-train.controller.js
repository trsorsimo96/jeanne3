(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ModelTrainController', ModelTrainController);

    ModelTrainController.$inject = ['DataUtils', 'ModelTrain', 'ModelTrainSearch'];

    function ModelTrainController(DataUtils, ModelTrain, ModelTrainSearch) {

        var vm = this;

        vm.modelTrains = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ModelTrain.query(function(result) {
                vm.modelTrains = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ModelTrainSearch.query({query: vm.searchQuery}, function(result) {
                vm.modelTrains = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
