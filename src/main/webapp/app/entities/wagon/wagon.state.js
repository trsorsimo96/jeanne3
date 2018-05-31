(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wagon', {
            parent: 'entity',
            url: '/wagon',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.wagon.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wagon/wagons.html',
                    controller: 'WagonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wagon');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('wagon-detail', {
            parent: 'wagon',
            url: '/wagon/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.wagon.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wagon/wagon-detail.html',
                    controller: 'WagonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wagon');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Wagon', function($stateParams, Wagon) {
                    return Wagon.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wagon',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wagon-detail.edit', {
            parent: 'wagon-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wagon/wagon-dialog.html',
                    controller: 'WagonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Wagon', function(Wagon) {
                            return Wagon.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wagon.new', {
            parent: 'wagon',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wagon/wagon-dialog.html',
                    controller: 'WagonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                name: null,
                                place: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wagon', null, { reload: 'wagon' });
                }, function() {
                    $state.go('wagon');
                });
            }]
        })
        .state('wagon.edit', {
            parent: 'wagon',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wagon/wagon-dialog.html',
                    controller: 'WagonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Wagon', function(Wagon) {
                            return Wagon.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wagon', null, { reload: 'wagon' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wagon.delete', {
            parent: 'wagon',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wagon/wagon-delete-dialog.html',
                    controller: 'WagonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Wagon', function(Wagon) {
                            return Wagon.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wagon', null, { reload: 'wagon' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
