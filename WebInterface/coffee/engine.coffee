class Engine
  constructor: ->
    @resolution = [640, 480]
    @bgcolor = 0.fffff0;
    @obj_stack = []
    @scene = new THREE.Scene
    @camera = new THREE.PerspectiveCamera 75, @resolution[0] / @resolution[1], 1, 10000
    @camera.position.y = 33
    @camera.position.z = 100
    @camera_mode = "BIRD"

    @amb_light = new THREE.AmbientLight 0xffffff, 1.0
    @scene.add @amb_light

    @renderer = new THREE.WebGLRenderer
    @renderer.clearColor = @bgcolor
    @renderer.clear
    @renderer.setSize @resolution[0], @resolution[1]

    now = new Date
    @start_time = now.getTime()

  get_canvas: (target_div) ->
    @renderer.domElement

  add: (obj) ->
    @obj_stack.push obj
    @scene.add drawable for drawable in obj.drawables

  render: ->
    # console.log "rendering"
    time = (new Date).getTime() - @start_time

    obj.follow(@camera.position) for obj in @obj_stack
    obj.animate(time) for obj in @obj_stack

    switch @camera_mode
      when "BIRD"
        @camera.position.set 0, 60, 0
        @camera.rotation.set -Math.PI / 2, 0, 0
      else
        @camera.position.x =       5 * Math.cos (time / 1200)
        @camera.position.y = 33 +  5 * Math.sin (time / 600)
        @camera.position.z = 50 + 25 * Math.sin (time / 2700)
        @camera.lookAt new THREE.Vector3 0, 0, 0

    @renderer.render @scene, @camera

  set_cam_mode: (mode) ->
    @camera_mode = mode
