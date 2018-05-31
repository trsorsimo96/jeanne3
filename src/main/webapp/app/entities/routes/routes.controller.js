(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('RoutesController', RoutesController);

    RoutesController.$inject = ['Routes', 'RoutesSearch'];

    function RoutesController(Routes, RoutesSearch) {

        var vm = this;

        vm.routes = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Routes.query(function(result) {
                vm.routes = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            RoutesSearch.query({query: vm.searchQuery}, function(result) {
                vm.routes = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
