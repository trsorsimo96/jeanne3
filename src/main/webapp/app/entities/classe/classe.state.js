(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('classe', {
            parent: 'entity',
            url: '/classe',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.classe.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/classe/classes.html',
                    controller: 'ClasseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classe');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('classe-detail', {
            parent: 'classe',
            url: '/classe/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.classe.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/classe/classe-detail.html',
                    controller: 'ClasseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classe');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Classe', function($stateParams, Classe) {
                    return Classe.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'classe',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('classe-detail.edit', {
            parent: 'classe-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classe/classe-dialog.html',
                    controller: 'ClasseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Classe', function(Classe) {
                            return Classe.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('classe.new', {
            parent: 'classe',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classe/classe-dialog.html',
                    controller: 'ClasseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('classe', null, { reload: 'classe' });
                }, function() {
                    $state.go('classe');
                });
            }]
        })
        .state('classe.edit', {
            parent: 'classe',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classe/classe-dialog.html',
                    controller: 'ClasseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Classe', function(Classe) {
                            return Classe.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('classe', null, { reload: 'classe' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('classe.delete', {
            parent: 'classe',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classe/classe-delete-dialog.html',
                    controller: 'ClasseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Classe', function(Classe) {
                            return Classe.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('classe', null, { reload: 'classe' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
