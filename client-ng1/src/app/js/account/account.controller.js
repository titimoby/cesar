(function () {

  'use strict';

  angular.module('cesar-account').controller('AccountCtrl', function ($rootScope, $http, $state, $translate, $filter, account, LANGUAGES) {
    'ngInject';

    var ctrl = this;
    ctrl.account = account;

    ctrl.getDate = function(date){
      return $filter('date')(date, "dd/MM/yyyy")
    };

    if(ctrl.account){
      ctrl.updateAccount = function () {
        $http
          .put('app/account', angular.copy(ctrl.account), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function () {
            if(ctrl.account.defaultLanguage){
              $translate.use((ctrl.account.defaultLanguage === LANGUAGES.us) ? LANGUAGES.us : LANGUAGES.fr);
            }
            $rootScope.$broadcast('event:auth-changed');
            $state.go('home');
          })
          .catch(function (response) {
            if (response.data.type && response.data.type === 'USER_NOT_FOUND'){
              ctrl.errorMessage = 'USER_NOT_FOUND';
            }
            else {
              ctrl.errorMessage = 'UNDEFINED';
            }
          });
      };
    }


  });

})();