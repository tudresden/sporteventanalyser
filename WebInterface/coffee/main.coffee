engine = new Engine()
field = new Field()
ball = new Ball()
console.log field.toString()

engine.add field
engine.add ball

$ ->
  console.log "Initializing"
  $("#field").append engine.get_canvas()
  for button in $("#perspectives_menu li button")
    do (button) ->
      button.onclick = engine.set_cam_mode(button.id)
  
  black = false
  run = ->
    requestAnimationFrame run
    engine.render()

  requestAnimationFrame run
