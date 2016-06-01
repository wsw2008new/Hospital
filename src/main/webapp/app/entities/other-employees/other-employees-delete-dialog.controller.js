(function() {
    'use strict';

    angular
        .module('hospitalApp')
        .controller('OtherEmployeesDeleteController',OtherEmployeesDeleteController);

    OtherEmployeesDeleteController.$inject = ['$uibModalInstance', 'entity', 'OtherEmployees'];

    function OtherEmployeesDeleteController($uibModalInstance, entity, OtherEmployees) {
        var vm = this;

        vm.otherEmployees = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OtherEmployees.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
