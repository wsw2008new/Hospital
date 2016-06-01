(function() {
    'use strict';

    angular
        .module('hospitalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('examinations', {
            parent: 'entity',
            url: '/examinations?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Examinations'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/examinations/examinations.html',
                    controller: 'ExaminationsController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('examinations-detail', {
            parent: 'entity',
            url: '/examinations/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Examinations'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/examinations/examinations-detail.html',
                    controller: 'ExaminationsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Examinations', function($stateParams, Examinations) {
                    return Examinations.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('examinations.new', {
            parent: 'examinations',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/examinations/examinations-dialog.html',
                    controller: 'ExaminationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                conclusion: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('examinations', null, { reload: true });
                }, function() {
                    $state.go('examinations');
                });
            }]
        })
        .state('examinations.edit', {
            parent: 'examinations',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/examinations/examinations-dialog.html',
                    controller: 'ExaminationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Examinations', function(Examinations) {
                            return Examinations.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('examinations', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('examinations.delete', {
            parent: 'examinations',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/examinations/examinations-delete-dialog.html',
                    controller: 'ExaminationsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Examinations', function(Examinations) {
                            return Examinations.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('examinations', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
