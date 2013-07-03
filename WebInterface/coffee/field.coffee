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
    @target_pos.z = data.z if data.z?
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
    @field.rotation.set -Math.PI/2, 0, 0
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
    @shadow.position.y = 0.001
    @shadow.rotation.set -Math.PI/2, 0, 0

    @followers = [@ball]
    @drawables = [@ball, @shadow]
    @ball.position.set 0, @ball.geometry.height/2, 0
    @last_update = 0
    @target_pos =
      x: 0,
      y: 0,  # middle of the field
      z: 0
    @anim_factor = 10
    @time = 0

  animate: (time) ->
    @ball.position.x = (@anim_factor * @ball.position.x + @target_pos.x)/(@anim_factor + 1)
    @ball.position.y = Math.max(@ball.geometry.height/2, (@anim_factor * @ball.position.y + 0.5 * @ball.geometry.height + @target_pos.z)/(@anim_factor + 1))
    @ball.position.z = (@anim_factor * @ball.position.z + @target_pos.y)/(@anim_factor + 1)

    @shadow.position.x = @ball.position.x
    @shadow.position.z = @ball.position.z

    s_scale = 1.0 + 1.0 * Math.max(1.0, @ball.position.y)
    @shadow.scale.set s_scale, s_scale, s_scale
    @shadow.material.opacity = Math.min(1.0, Math.max(0.0, 1.0 / @ball.position.y))
    @time = time

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
    @shadow.position.y = 0.005
    @shadow.rotation.set -Math.PI/2, 0, 0

    geometry = new THREE.PlaneGeometry 5, 5
    mat_cfg =
      map:         new THREE.ImageUtils.loadTexture "img/selection.png"
      transparent: true
    material = new THREE.MeshBasicMaterial mat_cfg
    @select = new THREE.Mesh geometry, material
    @select.position.y = 0.01

    @followers = [@shirt]
    @drawables = [@shirt, @shadow, @select]

    @last_update = 0
    @target_pos =
      x: 0,
      y: 0  # middle of the field
    @anim_factor = 10
    @time = 0
    @selected = 2

  animate: (time) ->
    @shirt.position.x = (@anim_factor * @shirt.position.x + @target_pos.x)/(@anim_factor + 1)
    @shirt.position.z = (@anim_factor * @shirt.position.z + @target_pos.y)/(@anim_factor + 1)

    @shadow.position.x = @shirt.position.x
    @shadow.position.z = @shirt.position.z

    s = @shirt.position.y - @shirt.geometry.height
    @shadow.scale.set s, s, s

    switch @selected
      when 2
        @select.material.opacity = 0.01
        @shirt.material.opacity = 1
      when 1
        s = 1 + 0.2 * Math.sin(0.005 * time)
        @select.scale.set s, s, s
        @select.rotation.y = 0.01 * time
        @select.position.x = @shirt.position.x
        @select.position.z = @shirt.position.z
        @select.material.opacity = 1.0
        @shirt.rotation.x = Math.PI/2
        @shirt.material.opacity = 1.0
      else
        @select.material.opacity = 0
        @shirt.material.opacity = 0.5
    @time = time

  toString: ->
    "Player(" + @tricot_image + ")"
