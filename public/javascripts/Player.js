function Player() {
    var _isPlaying = false;

    var _audioElements = undefined;
    var _numberOfAudioElements = undefined;

    return {
        startPlaying: function() {
            _isPlaying = true;
        },
        stopPlaying: function() {
            _isPlaying = false;
        },
        isPlaying: function(){
            return _isPlaying
        },
        onKeyDown: function(e, callback = function(){}) {
            var key = e.key;
            callback();
            if(key === "g") {
                this.startPlaying();
            } else if(key === "Escape") {
                this.stopPlaying();
            }
        },
        getAudioElements: function() {
            // "lazy initialisation"
            if(_audioElements == undefined) {
              _audioElements = document.getElementsByTagName("audio");
              _numberOfAudioElements = _audioElements.length;
            }
            return _audioElements;
        },
        getAudioElement: function(i) {
            return this.getAudioElements()[i];
        },
        getNumberOfAudioElements: function() {
            return _numberOfAudioElements;
        },
        playAllOnce: function() {
            var length = this.getAudioElements().length;
            var i;
            for(i = 0; i < length; i++) {
                this.getAudioElement(i).play();
            }
        }
    }
};