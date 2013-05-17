engine = new Engine()
field = new Field()
ball = new Ball()

players = []
for i in [1..11]
  do (i) ->
    players.push new Player "img/ball.png"
for i in [1..11]
  do (i) ->
    players.push new Player "img/ball.png"


console.log field.toString()

engine.add field
engine.add ball
engine.add plr for plr in players


$ ->
  console.log "Initializing"
  $("#field").append engine.get_canvas()
  for button in $("#perspectives_menu li button")
    do (button) ->
      button.onclick = engine.set_cam_mode(button.id)
  
  run = ->
    requestAnimationFrame run
    engine.render()

  requestAnimationFrame run

  update = ->
    window.setTimeout update, 1000

    # setting new player positions
    for plr in players
      do (plr) ->
        data =
          pos:
            x: Math.random() * field.width - field.width/2
            y: Math.random() * field.height - field.height/2
        plr.update 0, data

  window.setTimeout update, 1000
