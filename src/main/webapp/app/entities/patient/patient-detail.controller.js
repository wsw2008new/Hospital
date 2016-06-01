(function() {
    'use strict';

    angular
        .module('hospitalApp')
        .controller('PatientDetailController', PatientDetailController);

    PatientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Patient', 'Doctor', 'Examinations'];

    function PatientDetailController($scope, $rootScope, $stateParams, entity, Patient, Doctor, Examinations) {
        var vm = this;

        vm.patient = entity;

        var unsubscribe = $rootScope.$on('hospitalApp:patientUpdate', function(event, result) {
            vm.patient = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
