(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('voyage', {
            parent: 'entity',
            url: '/voyage',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.voyage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/voyage/voyages.html',
                    controller: 'VoyageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('voyage');
                    $translatePartialLoader.addPart('typeVoyage');
                    $translatePartialLoader.addPart('stateVoyage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('voyage-detail', {
            parent: 'voyage',
            url: '/voyage/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.voyage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/voyage/voyage-detail.html',
                    controller: 'VoyageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('voyage');
                    $translatePartialLoader.addPart('typeVoyage');
                    $translatePartialLoader.addPart('stateVoyage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Voyage', function($stateParams, Voyage) {
                    return Voyage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'voyage',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('voyage-detail.edit', {
            parent: 'voyage-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/voyage/voyage-dialog.html',
                    controller: 'VoyageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Voyage', function(Voyage) {
                            return Voyage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('voyage.new', {
            parent: 'voyage',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/voyage/voyage-dialog.html',
                    controller: 'VoyageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numero: null,
                                datedepart: null,
                                datearrive: null,
                                type: null,
                                state: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('voyage', null, { reload: 'voyage' });
                }, function() {
                    $state.go('voyage');
                });
            }]
        })
        .state('voyage.edit', {
            parent: 'voyage',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/voyage/voyage-dialog.html',
                    controller: 'VoyageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Voyage', function(Voyage) {
                            return Voyage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('voyage', null, { reload: 'voyage' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('voyage.delete', {
            parent: 'voyage',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/voyage/voyage-delete-dialog.html',
                    controller: 'VoyageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Voyage', function(Voyage) {
                            return Voyage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('voyage', null, { reload: 'voyage' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
