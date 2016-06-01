(function() {
    'use strict';

    angular
        .module('hospitalApp')
        .controller('ExaminationsDetailController', ExaminationsDetailController);

    ExaminationsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Examinations', 'Doctor', 'Patient'];

    function ExaminationsDetailController($scope, $rootScope, $stateParams, entity, Examinations, Doctor, Patient) {
        var vm = this;

        vm.examinations = entity;

        var unsubscribe = $rootScope.$on('hospitalApp:examinationsUpdate', function(event, result) {
            vm.examinations = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
