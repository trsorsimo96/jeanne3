(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ItineraireController', ItineraireController);

    ItineraireController.$inject = ['Itineraire', 'ItineraireSearch'];

    function ItineraireController(Itineraire, ItineraireSearch) {

        var vm = this;

        vm.itineraires = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Itineraire.query(function(result) {
                vm.itineraires = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ItineraireSearch.query({query: vm.searchQuery}, function(result) {
                vm.itineraires = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
