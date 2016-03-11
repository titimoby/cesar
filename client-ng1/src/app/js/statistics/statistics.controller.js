(function () {

  'use strict';

  angular.module('cesar-account').controller('StatisticsCtrl', function ($http, $q) {
    'ngInject';

    var ctrl = this;
    ctrl.stats = {};


    ctrl.getVote = function (voter, proposalId) {
      var result = undefined;
      ctrl.stats.proposals.forEach(function (elt) {
        if (elt.votes && elt.votes.length > 0) {

          elt.votes.forEach(function (vote) {
            if (vote.proposalId === proposalId && vote.voter === voter) {
              console.log('found for session '+proposalId,vote.voteValue);
              result = vote.voteValue;
            }
          });
        }
      });
      return result;
    };

    //Recuperate count of submitted session
    $q.all([
      $http.get('/app/cfp/vote/nbsessions/SUBMITTED')
        .then(function (response) {
          ctrl.stats.submitted = response.data;
          return $http.get('/app/cfp/vote/nbsessions/ACCEPTED');
        })
        .then(function (response) {
          ctrl.stats.accepted = response.data;
          return $http.get('/app/cfp/vote/nbsessions/REJECTED');
        })
        .then(function (response) {
          ctrl.stats.rejected = response.data;
        }),
      //Recuperate stats by user
      $http.get('/app/cfp/vote/summary')
        .then(function (response) {
          ctrl.stats.dones = response.data;
        }),
      //Recuperate all data for each proposal
      $http.get('/app/cfp/vote')
        .then(function (response) {
          ctrl.stats.proposals = response.data;
          ctrl.stats.proposals.forEach(function(elt){
            if(elt.votes && elt.votes.length>0){
              elt.totalVotes = elt
                .votes
                .map(function(e){
                  return e.voteValue;
                })
                .reduce(function(a, b) {
                  return a + b;
                });
            }
            else{
              elt.totalVotes = -9999;
            }
          });
        })
    ]);
  });

})();