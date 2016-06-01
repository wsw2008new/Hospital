(function() {
    'use strict';

    angular
        .module('hospitalApp')
        .controller('OtherEmployeesDetailController', OtherEmployeesDetailController);

    OtherEmployeesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'OtherEmployees'];

    function OtherEmployeesDetailController($scope, $rootScope, $stateParams, entity, OtherEmployees) {
        var vm = this;

        vm.otherEmployees = entity;

        var unsubscribe = $rootScope.$on('hospitalApp:otherEmployeesUpdate', function(event, result) {
            vm.otherEmployees = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
