class Drawable
  constructor: ->

  animate: (time) ->

  follow: (target, reset = false) ->
    for f in @followers
      do (f) ->
        if reset
          f.rotation.set -Math.PI/2, 0, 0
        else
          f.lookAt target

  toString: ->
    "Drawable"

class Moveable extends Drawable
  constructor: ->

  update: (time, data) ->
    @target_pos.x = data.x if data.x?
    @target_pos.y = data.y if data.y?
    @last_update = time if time?

  toString: ->
    "Moveable"

class Field extends Drawable
  constructor: (texturefile, @reality) ->
    @ratio = @reality.width / @reality.height
    @width = 120
    @height = 90
    geometry = new THREE.PlaneGeometry(@width, @height)
    mat_cfg = 
      map:  new THREE.ImageUtils.loadTexture texturefile
      side: THREE.DoubleSide
    material = new THREE.MeshLambertMaterial(mat_cfg)
    @field = new THREE.Mesh geometry, material
    @field.rotation.x = Math.PI/2
    @drawables = [@field]
    @followers = []

  toString: ->
    "Field"

class Ball extends Moveable
  constructor: (texturefile) ->
    geometry = new THREE.PlaneGeometry(2, 2)
    mat_cfg =
      map:       new THREE.ImageUtils.loadTexture texturefile
      alphaTest: 0.5
    material = new THREE.MeshBasicMaterial mat_cfg
    @ball = new THREE.Mesh(geometry, material)
    geometry = new THREE.PlaneGeometry 1, 1
    mat_cfg =
      map:         new THREE.ImageUtils.loadTexture "img/shadow.png"
      transparent: true
    material = new THREE.MeshBasicMaterial mat_cfg
    @shadow = new THREE.Mesh geometry, material
    @shadow.rotation.x = -Math.PI/2
    @shadow.position.y = 0.001
    @followers = [@ball]
    @drawables = [@ball, @shadow]
    @ball.position.set 0, @ball.geometry.height/2, 0
    @last_update = 0
    @target_pos =
      x: 0,
      y: 0  # middle of the field
    @anim_factor = 10

  animate: (time) ->
    @ball.position.x = (@anim_factor * @ball.position.x + @target_pos.x )/(@anim_factor + 1)
    @ball.position.z = (@anim_factor * @ball.position.z + @target_pos.y )/(@anim_factor + 1)

    @shadow.position.x = @ball.position.x
    @shadow.position.z = @ball.position.z

    s_scale = 3.0 + @ball.scale.x + @ball.position.y - @ball.geometry.height
    @shadow.scale.set s_scale, s_scale, s_scale

  toString: ->
    "Ball"

class Player extends Moveable
  constructor: (@id, @name, @team, @tricot_image) ->
    geometry = new THREE.PlaneGeometry(4, 4)
    mat_cfg =
      map:       new THREE.ImageUtils.loadTexture @tricot_image
      transparent: true
    material = new THREE.MeshBasicMaterial mat_cfg
    @shirt = new THREE.Mesh(geometry, material)
    @shirt.position.y = @shirt.geometry.height/2

    geometry = new THREE.PlaneGeometry 2, 2
    mat_cfg =
      map:         new THREE.ImageUtils.loadTexture "img/shadow.png"
      alpha:       0.5
      transparent: true
    material = new THREE.MeshBasicMaterial mat_cfg
    @shadow = new THREE.Mesh geometry, material
    @shadow.rotation.x = -Math.PI/2
    @shadow.position.y = 0.005

    geometry = new THREE.PlaneGeometry 5, 5
    mat_cfg =
      map:         new THREE.ImageUtils.loadTexture "img/selection.png"
      transparent: true
    material = new THREE.MeshBasicMaterial mat_cfg
    @select = new THREE.Mesh geometry, material
    @select.rotation.x = -Math.PI/2
    @select.position.y = 0.01

    @followers = [@shirt]
    @drawables = [@shirt, @shadow, @select]

    @last_update = 0
    @target_pos =
      x: 0,
      y: 0  # middle of the field
    @anim_factor = 10
    @selected = false

  animate: (time) ->
    @shirt.position.x = (@anim_factor * @shirt.position.x + @target_pos.x )/(@anim_factor + 1)
    @shirt.position.z = (@anim_factor * @shirt.position.z + @target_pos.y )/(@anim_factor + 1)

    @shadow.position.x = @shirt.position.x
    @shadow.position.z = @shirt.position.z

    @shadow.scale.x = @shirt.position.y
    @shadow.scale.y = @shirt.position.y
    @shadow.scale.z = @shirt.position.y
    @shadow.scale.x -= @shirt.geometry.height
    @shadow.scale.y -= @shirt.geometry.height
    @shadow.scale.z -= @shirt.geometry.height

    if @selected
      s = 1 * (1 + 0.2 * Math.sin(0.005 * time))
      @select.scale.set s, s, s
      @select.rotation.z = 0.01 * time
      @select.position.x = @shirt.position.x
      @select.position.z = @shirt.position.z
      @shirt.material.opacity = 1.0
      console.log @select.rotation.y
    else
      @select.scale.set 0, 0, 0
      @shirt.material.opacity = 0.5
      console.log @shirt

  toString: ->
    "Player(" + @tricot_image + ")"
