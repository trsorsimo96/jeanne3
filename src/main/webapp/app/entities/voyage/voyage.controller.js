(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('VoyageController', VoyageController);

    VoyageController.$inject = ['Voyage', 'VoyageSearch'];

    function VoyageController(Voyage, VoyageSearch) {

        var vm = this;

        vm.voyages = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Voyage.query(function(result) {
                vm.voyages = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            VoyageSearch.query({query: vm.searchQuery}, function(result) {
                vm.voyages = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
