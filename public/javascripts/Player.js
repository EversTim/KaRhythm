function Player() {
  var _isPlaying = false;

  var _audioElements = document.getElementsByTagName("audio");
  var _numberOfAudioElements = _audioElements.length;

  var _length = document.getElementById("length").innerHTML;
  var _tracks = document.getElementById("tracks").innerHTML;
  var _checkboxes = document.getElementsByClassName("pccheckbox")
  if (_checkboxes.length != _length * _tracks) {
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
      for (i = 0; i < _tracks; i++) {
        this.getAudioElement(i).play();
      }
    },
    getCheckboxes: function() {
      return _checkboxes;
    },
    getCheckbox: function(beat, track) {
      return this.getCheckboxes()[beat + track * _length];
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
      for (i = 0; i < _length; i++) {
        for (j = 0; j < _tracks; j++) {
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
      for (i = 0; i < _tracks; i++) {
        var elem = this.getAudioElement(i);
        if (elem.dataset.tracknumber === track) {
          return elem;
        }
      }
      throw 'AudioElementForTrackNotFoundException';
    },
    getLength: function() {
      return _length;
    },
    getTracks: function() {
      return _tracks;
    }
  }
};
