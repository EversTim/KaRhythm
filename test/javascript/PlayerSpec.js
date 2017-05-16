describe("A Player", function() {

    var player;
    var keyPress;

    beforeAll(function() {
        var audio;
        audio = document.createElement("audio");
        document.body.append(audio);
        audio = document.createElement("audio");
        document.body.append(audio);

        document.addEventListener('keydown', function(e) {
          player.onKeyDown(e)
        });

        keyPress = function(key) {
          var event = document.createEvent('Event');
          event.key = key;
          event.initEvent('keydown');
          document.dispatchEvent(event)
        }
    });

    beforeEach(function() {
        player = new Player();
    });

    afterEach(function() {
        player = undefined;
    });

    it("should exist", function() {
        expect(player).toBeDefined();
    });

    it("should be able to start playing", function() {
        player.startPlaying();
        expect(player.isPlaying()).toBe(true);
    });

    it("should be stopped by default", function() {
        expect(player.isPlaying()).toBe(false);
    });

    it("should be able to stop playing", function() {
        player.startPlaying();
        player.stopPlaying();
        expect(player.isPlaying()).toBe(false);
    });

    it("should start playing on keypress g", function() {
        keyPress('g');
        expect(player.isPlaying()).toBe(true);
    });

    it("should stop playing on keypress Esc", function() {
        keyPress('g');
        keyPress('Escape');
        expect(player.isPlaying()).toBe(false);
    });

    it("should be able to return a list of audio elements on the page", function() {
        expect(player.getAudioElements()).toEqual(document.getElementsByTagName("audio"));
        expect(player.getAudioElements().length).toBe(2);
    });

    it("should be able to return the nth audio element on the page", function() {
        var i;
        for(i = 0; i < player.getNumberOfAudioElements(); i++) {
            expect(player.getAudioElement(i)).toBeDefined();
        }
        expect(player.getAudioElement(player.getNumberOfAudioElements())).toBeUndefined();
    });

    it("should play all audio tags on playAllOnce()", function() {
        player.playAllOnce();
        var i;
        for(i = 0; i < player.getNumberOfAudioElements(); i++) {
            expect(player.getAudioElement(i).paused).toBe(false);
        }
    });
});