(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('model-car', {
            parent: 'entity',
            url: '/model-car',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.modelCar.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-car/model-cars.html',
                    controller: 'ModelCarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('modelCar');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('model-car-detail', {
            parent: 'model-car',
            url: '/model-car/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.modelCar.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-car/model-car-detail.html',
                    controller: 'ModelCarDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('modelCar');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ModelCar', function($stateParams, ModelCar) {
                    return ModelCar.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'model-car',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('model-car-detail.edit', {
            parent: 'model-car-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-car/model-car-dialog.html',
                    controller: 'ModelCarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelCar', function(ModelCar) {
                            return ModelCar.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-car.new', {
            parent: 'model-car',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-car/model-car-dialog.html',
                    controller: 'ModelCarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                name: null,
                                nbNormalRange: null,
                                nbSeatLeft: null,
                                nbSeatRight: null,
                                nbSeatBefore: null,
                                nbSeatBelow: null,
                                image: null,
                                imageContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('model-car', null, { reload: 'model-car' });
                }, function() {
                    $state.go('model-car');
                });
            }]
        })
        .state('model-car.edit', {
            parent: 'model-car',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-car/model-car-dialog.html',
                    controller: 'ModelCarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelCar', function(ModelCar) {
                            return ModelCar.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-car', null, { reload: 'model-car' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-car.delete', {
            parent: 'model-car',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-car/model-car-delete-dialog.html',
                    controller: 'ModelCarDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ModelCar', function(ModelCar) {
                            return ModelCar.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-car', null, { reload: 'model-car' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
