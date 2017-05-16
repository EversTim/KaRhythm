var player = new Player();

document.addEventListener('keydown', function(e) {
    player.onKeyDown(e, function() {
        if(/^[a-z]$/.test(e.key) || e.key === "Escape") {
            alert(e.key)
        }
    })
});

player.playAllOnce();

setTimeout(function() {
    for(i = 0; i < player.getNumberOfAudioElements(); i++) {
        alert(player.getAudioElement(i).paused)
    }
}, 2000)