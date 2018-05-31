(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company-classe', {
            parent: 'entity',
            url: '/company-classe',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.companyClasse.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-classe/company-classes.html',
                    controller: 'CompanyClasseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companyClasse');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('company-classe-detail', {
            parent: 'company-classe',
            url: '/company-classe/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.companyClasse.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-classe/company-classe-detail.html',
                    controller: 'CompanyClasseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companyClasse');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CompanyClasse', function($stateParams, CompanyClasse) {
                    return CompanyClasse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'company-classe',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('company-classe-detail.edit', {
            parent: 'company-classe-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-classe/company-classe-dialog.html',
                    controller: 'CompanyClasseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyClasse', function(CompanyClasse) {
                            return CompanyClasse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-classe.new', {
            parent: 'company-classe',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-classe/company-classe-dialog.html',
                    controller: 'CompanyClasseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                price: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('company-classe', null, { reload: 'company-classe' });
                }, function() {
                    $state.go('company-classe');
                });
            }]
        })
        .state('company-classe.edit', {
            parent: 'company-classe',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-classe/company-classe-dialog.html',
                    controller: 'CompanyClasseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyClasse', function(CompanyClasse) {
                            return CompanyClasse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-classe', null, { reload: 'company-classe' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-classe.delete', {
            parent: 'company-classe',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-classe/company-classe-delete-dialog.html',
                    controller: 'CompanyClasseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CompanyClasse', function(CompanyClasse) {
                            return CompanyClasse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-classe', null, { reload: 'company-classe' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
