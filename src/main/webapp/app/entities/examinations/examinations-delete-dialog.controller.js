(function() {
    'use strict';

    angular
        .module('hospitalApp')
        .controller('ExaminationsDeleteController',ExaminationsDeleteController);

    ExaminationsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Examinations'];

    function ExaminationsDeleteController($uibModalInstance, entity, Examinations) {
        var vm = this;

        vm.examinations = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Examinations.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
