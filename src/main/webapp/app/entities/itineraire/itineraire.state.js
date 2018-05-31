(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('itineraire', {
            parent: 'entity',
            url: '/itineraire',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.itineraire.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/itineraire/itineraires.html',
                    controller: 'ItineraireController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('itineraire');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('itineraire-detail', {
            parent: 'itineraire',
            url: '/itineraire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.itineraire.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/itineraire/itineraire-detail.html',
                    controller: 'ItineraireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('itineraire');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Itineraire', function($stateParams, Itineraire) {
                    return Itineraire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'itineraire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('itineraire-detail.edit', {
            parent: 'itineraire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/itineraire/itineraire-dialog.html',
                    controller: 'ItineraireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Itineraire', function(Itineraire) {
                            return Itineraire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('itineraire.new', {
            parent: 'itineraire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/itineraire/itineraire-dialog.html',
                    controller: 'ItineraireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateDepart: null,
                                dateArrive: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('itineraire', null, { reload: 'itineraire' });
                }, function() {
                    $state.go('itineraire');
                });
            }]
        })
        .state('itineraire.edit', {
            parent: 'itineraire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/itineraire/itineraire-dialog.html',
                    controller: 'ItineraireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Itineraire', function(Itineraire) {
                            return Itineraire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('itineraire', null, { reload: 'itineraire' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('itineraire.delete', {
            parent: 'itineraire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/itineraire/itineraire-delete-dialog.html',
                    controller: 'ItineraireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Itineraire', function(Itineraire) {
                            return Itineraire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('itineraire', null, { reload: 'itineraire' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
