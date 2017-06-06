'use strict';

function Pattern() {
  var _length = parseInt(document.getElementById("length").value);
  var _tracks = parseInt(document.getElementById("tracks").value);
  var _data = undefined;

  setPatternDataFromPage();
  addOnChangeEventHandlers();

  function setPatternDataFromPage() {
    _data = Utilities.makeArray(_tracks, _length)
    var i, j;
    for (i = 0; i < _tracks; i++) {
      for (j = 0; j < _length; j++) {
        if (Utilities.getCheckbox(i, j).checked) {
          _data[i][j] = true;
        } else {
          _data[i][j] = false;
        }
      }
    }
  }

  function addOnChangeEventHandlers() {
    var i;
    for(i = 0; i < Utilities.getCheckboxes().length; i++) {
      Utilities.getCheckboxes()[i].addEventListener("click", setPatternDataFromPage);
    }
  }

  return {
    getLength: function() {
      return _length;
    },
    getTracks: function() {
      return _tracks;
    },
    getData: function() {
      return _data;
    },
    getSingle: function(track, length) {
      return _data[track][length];
    },
    play: function(beat) {
      var track;
      var output = [];
      for (track = 0; track < _tracks; track++) {
        if (_data[track][beat]) {
          output.push(track);
        }
      }
      return output;
    }
  }
}
