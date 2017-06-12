'use strict';

function Pattern() {
  var _length = parseInt(document.getElementById("length").value);
  var _tracks = parseInt(document.getElementById("tracks").value);
  var _data = undefined;

  onLoad();

  function onLoad() {
    setPatternDataFromPage();
    addOnChangeEventHandlers();
    var i;
    for(i = 0; i < Utilities.getCheckboxes().length; i++) {
      updateLabelClass(Utilities.getCheckboxes()[i]);
    }
  }

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

  function updateLabelClass(target) {
    var parent = target.parentElement;
    if(target.checked) {
      parent.classList.add("checked");
    } else {
      parent.classList.remove("checked");
    }
  }

  function addOnChangeEventHandlers() {
    var i;
    for(i = 0; i < Utilities.getCheckboxes().length; i++) {
      Utilities.getCheckboxes()[i].addEventListener("click", setPatternDataFromPage);
      Utilities.getCheckboxes()[i].addEventListener("click", e => updateLabelClass(e.target));
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
