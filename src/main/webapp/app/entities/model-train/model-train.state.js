(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('model-train', {
            parent: 'entity',
            url: '/model-train',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.modelTrain.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-train/model-trains.html',
                    controller: 'ModelTrainController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('modelTrain');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('model-train-detail', {
            parent: 'model-train',
            url: '/model-train/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.modelTrain.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-train/model-train-detail.html',
                    controller: 'ModelTrainDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('modelTrain');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ModelTrain', function($stateParams, ModelTrain) {
                    return ModelTrain.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'model-train',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('model-train-detail.edit', {
            parent: 'model-train-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-train/model-train-dialog.html',
                    controller: 'ModelTrainDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelTrain', function(ModelTrain) {
                            return ModelTrain.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-train.new', {
            parent: 'model-train',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-train/model-train-dialog.html',
                    controller: 'ModelTrainDialogController',
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
                    $state.go('model-train', null, { reload: 'model-train' });
                }, function() {
                    $state.go('model-train');
                });
            }]
        })
        .state('model-train.edit', {
            parent: 'model-train',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-train/model-train-dialog.html',
                    controller: 'ModelTrainDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelTrain', function(ModelTrain) {
                            return ModelTrain.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-train', null, { reload: 'model-train' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-train.delete', {
            parent: 'model-train',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-train/model-train-delete-dialog.html',
                    controller: 'ModelTrainDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ModelTrain', function(ModelTrain) {
                            return ModelTrain.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-train', null, { reload: 'model-train' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
