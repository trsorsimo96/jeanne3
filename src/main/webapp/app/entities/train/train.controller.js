(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('TrainController', TrainController);

    TrainController.$inject = ['Train', 'TrainSearch'];

    function TrainController(Train, TrainSearch) {

        var vm = this;

        vm.trains = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Train.query(function(result) {
                vm.trains = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TrainSearch.query({query: vm.searchQuery}, function(result) {
                vm.trains = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
