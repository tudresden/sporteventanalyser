class Drawable
  constructor: ->
    @drawables = []
    @followers = []

  animate: (time) ->

  follow: (target) ->
    for f in @followers
      do (f) ->
        f.lookAt target

  toString: ->
    "Drawable"

class Field extends Drawable
  constructor: ->
    @width = 120
    @height = 90
    geometry = new THREE.PlaneGeometry(@width, @height)
    mat_cfg = 
      map:  new THREE.ImageUtils.loadTexture "img/Fussballfeld.svg"
      side: THREE.DoubleSide
    material = new THREE.MeshLambertMaterial(mat_cfg)
    @field = new THREE.Mesh geometry, material
    @field.rotation.x = Math.PI/2
    @drawables = [@field]
    @followers = []
  toString: ->
    "Field"

class Ball extends Drawable
  constructor: ->
    geometry = new THREE.PlaneGeometry(2, 2)
    mat_cfg =
      map:       new THREE.ImageUtils.loadTexture "img/ball.png"
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
    @shadow.position.y = 0.01
    @followers = [@ball]
    @drawables = [@ball, @shadow]
  animate: (time) ->
    @ball.position.y = 1
    @ball.position.y += Math.abs 2 * Math.sin time/120
    
    @ball.position.x = 2
    @ball.position.x += 2 * Math.cos time/120

    @shadow.position.x = @ball.position.x
    @shadow.position.z = @ball.position.z

    @shadow.scale.x = @ball.position.y
    @shadow.scale.x -= @ball.geometry.height

    @shadow.scale.y = @ball.position.y
    @shadow.scale.y -= @ball.geometry.height

    @shadow.scale.z = @ball.position.y
    @shadow.scale.z -= @ball.geometry.height
  toString: ->
    "Ball"

class Player extends Drawable
  constructor: (@tricot_image) ->
    geometry = new THREE.PlaneGeometry(4, 4)
    mat_cfg =
      map:       new THREE.ImageUtils.loadTexture @tricot_image
      alphaTest: 0.5
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
    @shadow.position.y = 0.01
    @followers = [@shirt]
    @drawables = [@shirt, @shadow]
    @last_update = 0
    @target_pos =
      x: 0,
      y: 0  # middle of the field
    @anim_factor = 10

  update: (time, data) ->
    @target_pos = data.pos
    @last_update = time

  animate: (time) ->
    @shirt.position.x = (@anim_factor * @shirt.position.x + @target_pos.x )/(@anim_factor + 1)
    @shirt.position.z = (@anim_factor * @shirt.position.z + @target_pos.y )/(@anim_factor + 1)

    @shadow.position.x = @shirt.position.x
    @shadow.position.z = @shirt.position.z

    @shadow.scale.x = @shirt.position.y
    @shadow.scale.x -= @shirt.geometry.height

    @shadow.scale.y = @shirt.position.y
    @shadow.scale.y -= @shirt.geometry.height

    @shadow.scale.z = @shirt.position.y
    @shadow.scale.z -= @shirt.geometry.height

  toString: ->
    "Player(" + @tricot_image + ")"
