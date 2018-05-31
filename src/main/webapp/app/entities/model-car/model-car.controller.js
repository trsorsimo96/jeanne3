(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ModelCarController', ModelCarController);

    ModelCarController.$inject = ['DataUtils', 'ModelCar', 'ModelCarSearch'];

    function ModelCarController(DataUtils, ModelCar, ModelCarSearch) {

        var vm = this;

        vm.modelCars = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ModelCar.query(function(result) {
                vm.modelCars = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ModelCarSearch.query({query: vm.searchQuery}, function(result) {
                vm.modelCars = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
