(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('FareController', FareController);

    FareController.$inject = ['Fare', 'FareSearch'];

    function FareController(Fare, FareSearch) {

        var vm = this;

        vm.fares = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Fare.query(function(result) {
                vm.fares = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FareSearch.query({query: vm.searchQuery}, function(result) {
                vm.fares = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
