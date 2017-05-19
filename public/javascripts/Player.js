function Player() {
  var _isPlaying = false;

  function getDataFromPage() {
    _data = Utilities.makeArray(_tracks, _length)
    var i, j;
    for (i = 0; i < _tracks; i++) {
      for (j = 0; j < _length; j++) {
        if (this.getCheckbox(i, j).checked) {
          _data[i][j] = true;
        } else {
          _data[i][j] = false;
        }
      }
    }
  }

  var _checkboxes = document.getElementsByClassName("pccheckbox")

  var _pattern = new Pattern(getDataFromPage);

  var _audioElements = document.getElementsByTagName("audio");
  var _numberOfAudioElements = _audioElements.length;

  if (_checkboxes.length != _pattern.getLength() * _pattern.getTracks()) {
    throw 'LengthTrackBoxCountMismatch';
  }

  function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  return {
    startPlaying: function() {
      _isPlaying = true;
    },
    stopPlaying: function() {
      _isPlaying = false;
    },
    isPlaying: function() {
      return _isPlaying
    },
    onKeyDown: function(e, callback = function() {}) {
      var key = e.key;
      callback();
      if (key === "g") {
        this.startPlaying();
      } else if (key === "Escape") {
        this.stopPlaying();
      }
    },
    getAudioElements: function() {
      return _audioElements;
    },
    getAudioElement: function(i) {
      return this.getAudioElements()[i];
    },
    getNumberOfAudioElements: function() {
      return _numberOfAudioElements;
    },
    playAllOnce: function() {
      var i;
      for (i = 0; i < _pattern.getTracks(); i++) {
        this.getAudioElement(i).play();
      }
    },
    getCheckboxes: function() {
      return _checkboxes;
    },
    getCheckbox: function(beat, track) {
      return this.getCheckboxes()[beat + track * _pattern.getLength()];
    },
    playLoopOnce: async function(callback, doSleep) {
      if (doSleep === undefined) {
        doSleep = true;
      }
      if (callback === undefined) {
        //callback = this.play
        callback = function(a, b, c) {};
      }
      var i, j;
      for (i = 0; i < _pattern.getLength(); i++) {
        for (j = 0; j < _pattern.getTracks(); j++) {
          if (this.getCheckbox(i, j).checked) {
            callback(i, j, true);
          } else {
            callback(i, j, false);
          }
        }
        if (doSleep) {
          await sleep(500);
        }
      }
    },
    getAudioElementByTrackNumber: function(track) {
      var i;
      for (i = 0; i < _pattern.getTracks(); i++) {
        var elem = this.getAudioElement(i);
        if (elem.dataset.tracknumber === track) {
          return elem;
        }
      }
      throw 'AudioElementForTrackNotFoundException';
    },
    getPattern: function() {
      return _pattern;
    }
  }
};
