var Utilities = (function() {
  return {
    makeArray: function(d1, d2) {
      var arr = [];
      for (i = 0; i < d1; i++) {
        arr.push(new Array(d2));
      }
      return arr;
    }
  }
})();
