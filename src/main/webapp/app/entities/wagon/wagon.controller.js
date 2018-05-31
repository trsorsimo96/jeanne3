(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('WagonController', WagonController);

    WagonController.$inject = ['Wagon', 'WagonSearch'];

    function WagonController(Wagon, WagonSearch) {

        var vm = this;

        vm.wagons = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Wagon.query(function(result) {
                vm.wagons = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            WagonSearch.query({query: vm.searchQuery}, function(result) {
                vm.wagons = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
