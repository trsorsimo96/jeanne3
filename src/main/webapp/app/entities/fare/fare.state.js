(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fare', {
            parent: 'entity',
            url: '/fare',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.fare.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fare/fares.html',
                    controller: 'FareController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fare');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('fare-detail', {
            parent: 'fare',
            url: '/fare/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.fare.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fare/fare-detail.html',
                    controller: 'FareDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fare');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Fare', function($stateParams, Fare) {
                    return Fare.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'fare',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('fare-detail.edit', {
            parent: 'fare-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fare/fare-dialog.html',
                    controller: 'FareDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fare', function(Fare) {
                            return Fare.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fare.new', {
            parent: 'fare',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fare/fare-dialog.html',
                    controller: 'FareDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                segmentNumber: null,
                                price: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('fare', null, { reload: 'fare' });
                }, function() {
                    $state.go('fare');
                });
            }]
        })
        .state('fare.edit', {
            parent: 'fare',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fare/fare-dialog.html',
                    controller: 'FareDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fare', function(Fare) {
                            return Fare.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fare', null, { reload: 'fare' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fare.delete', {
            parent: 'fare',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fare/fare-delete-dialog.html',
                    controller: 'FareDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Fare', function(Fare) {
                            return Fare.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fare', null, { reload: 'fare' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
