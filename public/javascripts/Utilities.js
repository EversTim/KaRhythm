'use strict';

var Utilities = (function() {

  var _checkboxes = document.getElementsByClassName("pccheckbox");

  return {
    makeArray: function(d1, d2) {
      var arr = [];
      var i;
      for (i = 0; i < d1; i++) {
        arr.push(new Array(d2));
      }
      return arr;
    },
    getCheckbox: function(track, beat) {
      var i;
      for (i = 0; i < _checkboxes.length; i++) {
        if ((parseInt(_checkboxes[i].dataset.tracknumber) === track) &&
          (parseInt(_checkboxes[i].dataset.beat) === beat)) {
          return _checkboxes[i];
        }
      }
      throw 'CheckboxNotFoundException';
    },
    getCheckboxes: function() {
      return _checkboxes;
    }
  }
})();
