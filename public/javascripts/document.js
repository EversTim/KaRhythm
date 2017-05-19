var player;

function makeArray(d1, d2) {
  var arr = [];
  for (i = 0; i < d1; i++) {
    arr.push(new Array(d2));
  }
  return arr;
}

window.onload = function() {
  player = new Player();

  document.addEventListener('keydown', function(e) {
    player.onKeyDown(e, function() {
      if (/^[a-z]$/.test(e.key) || e.key === "Escape") {
        alert(e.key)
      }
    })
  });

  player.playLoopOnce();
}
