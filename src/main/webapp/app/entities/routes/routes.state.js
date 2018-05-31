(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('routes', {
            parent: 'entity',
            url: '/routes',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.routes.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/routes/routes.html',
                    controller: 'RoutesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('routes');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('routes-detail', {
            parent: 'routes',
            url: '/routes/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.routes.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/routes/routes-detail.html',
                    controller: 'RoutesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('routes');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Routes', function($stateParams, Routes) {
                    return Routes.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'routes',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('routes-detail.edit', {
            parent: 'routes-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/routes/routes-dialog.html',
                    controller: 'RoutesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Routes', function(Routes) {
                            return Routes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('routes.new', {
            parent: 'routes',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/routes/routes-dialog.html',
                    controller: 'RoutesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                distance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('routes', null, { reload: 'routes' });
                }, function() {
                    $state.go('routes');
                });
            }]
        })
        .state('routes.edit', {
            parent: 'routes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/routes/routes-dialog.html',
                    controller: 'RoutesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Routes', function(Routes) {
                            return Routes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('routes', null, { reload: 'routes' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('routes.delete', {
            parent: 'routes',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/routes/routes-delete-dialog.html',
                    controller: 'RoutesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Routes', function(Routes) {
                            return Routes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('routes', null, { reload: 'routes' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
