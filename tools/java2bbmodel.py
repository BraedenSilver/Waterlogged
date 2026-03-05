#!/usr/bin/env python3
"""java2bbmodel.py - Waterlogged Java model -> BlockBench .bbmodel

Parses *Blockbench.java (geometry) + *Animations.java (animations)
and writes a .bbmodel you can open in BlockBench for editing.

Quick start (auto mode, no args):
    python3 tools/java2bbmodel.py

Auto mode is specific to this repository:
  - input:  src/main/java/net/braeden/waterlogged/entity/client/blockbench/*Blockbench.java
  - optional animation pair: *Animations.java
  - output: tools/blockbench/<Name>/<Name>.bbmodel

Manual mode (advanced):
    python3 tools/java2bbmodel.py \
        src/.../blockbench/FooBlockbench.java \
        [src/.../blockbench/FooAnimations.java] \
        tools/blockbench/Foo/Foo.bbmodel

Coordinate conversion (Java model → BlockBench modded_entity flip_y=true):
    bb_x =  java_x
    bb_y =  24 - java_abs_y      (applied to element from/to AND group origin)
    bb_z =  java_z
    from/to Y coords swap (lower bound in BB = 24 - upper bound in Java)

Rotation convention (Java radians → BB degrees):
    bb_rx = -java_rx  (negated due to Y-axis flip)
    bb_ry =  java_ry  (unchanged)
    bb_rz = -java_rz  (negated due to Y-axis flip)

NOTE: If rotations look mirrored in BlockBench, try negating all three
or removing the negation. The convention depends on BB version.
"""

import base64, json, math, re, sys, uuid as _uuid
from pathlib import Path
from typing import Optional


DEFAULT_SRC_DIR = Path("src/main/java/net/braeden/waterlogged/entity/client/blockbench")
DEFAULT_OUT_DIR = Path("tools/blockbench")


def camel_to_snake(name):
    """CrabModel → crab,  BubbleEye → bubble_eye,  MahiMahi → mahi_mahi"""
    s = re.sub(r'([A-Z]+)([A-Z][a-z])', r'\1_\2', name)
    return re.sub(r'([a-z\d])([A-Z])', r'\1_\2', s).lower()


def find_src_main(java_file: Path):
    """Walk up the directory tree to find the 'src/main' root."""
    for parent in java_file.parents:
        if parent.name == 'main' and parent.parent.name == 'src':
            return parent
    return None


def find_entity_tex_root(src_main: Path):
    """
    Scan src/main/resources/assets/*/textures/entity/ and return the first
    directory that exists.  Works regardless of the mod ID folder name.
    """
    assets = src_main / "resources" / "assets"
    if not assets.is_dir():
        return None
    for mod_dir in sorted(assets.iterdir()):
        candidate = mod_dir / "textures" / "entity"
        if candidate.is_dir():
            return candidate
    return None


def load_textures(model_name, geo_path: Path, tex_dir_override=None):
    """
    Find all PNGs for this model and return a list of BB texture dicts
    with embedded base64 source.  Returns [] if the folder doesn't exist.

    Auto-detects:
      - src/main root by walking up the file tree
      - assets/*/textures/entity/ by scanning (mod ID agnostic)
      - texture sub-folder: <entity_root>/<snake_model_name>/
    """
    if tex_dir_override and tex_dir_override.is_dir():
        folder = tex_dir_override
    else:
        src_main = find_src_main(geo_path)
        if not src_main:
            return []
        entity_root = find_entity_tex_root(src_main)
        if not entity_root:
            return []
        snake = camel_to_snake(model_name)
        folder = entity_root / snake

    if not folder.is_dir():
        return []
    textures = []
    for i, png in enumerate(sorted(folder.glob('*.png'))):
        data = base64.b64encode(png.read_bytes()).decode()
        textures.append({
            "name":      png.name,
            "id":        str(i),
            "width":     0,    # BB reads actual size from the PNG data
            "height":    0,
            "uv_width":  0,
            "uv_height": 0,
            "particle":  False,
            "internal":  True,
            "saved":     False,
            "uuid":      gen(),
            "source":    f"data:image/png;base64,{data}",
        })
    return textures


def gen():
    return str(_uuid.uuid4())


def convert_single_model(geo_path: Path, out_path: Path, anim_path: Optional[Path] = None, tex_override: Optional[Path] = None):
    """Convert one Java model (+ optional animations) into one .bbmodel file."""
    tw, th, parts = parse_geometry(geo_path.read_text())
    anims = []
    if anim_path and anim_path.exists():
        anims = parse_animations(anim_path.read_text())

    model_name = out_path.stem
    textures = load_textures(model_name, geo_path, tex_override)
    bbmodel = build_bbmodel(model_name, tw, th, parts, anims, textures)

    out_path.parent.mkdir(parents=True, exist_ok=True)
    out_path.write_text(json.dumps(bbmodel, indent=2))
    cubes = sum(len(p['boxes']) for p in parts.values())
    return len(parts), cubes, len(anims), len(textures)


def run_auto_mode(src_dir: Path = DEFAULT_SRC_DIR, out_dir: Path = DEFAULT_OUT_DIR):
    """Batch-convert all Waterlogged Blockbench Java models to tools/blockbench."""
    if not src_dir.is_dir():
        print(f"Error: source directory not found: {src_dir}")
        print("Run this script from the repository root.")
        sys.exit(1)

    geo_files = sorted(src_dir.glob("*Blockbench.java"))
    if not geo_files:
        print(f"No *Blockbench.java files found in {src_dir}")
        sys.exit(1)

    total = 0
    for geo_path in geo_files:
        base = geo_path.name[:-len("Blockbench.java")]
        anim_path = src_dir / f"{base}Animations.java"
        if not anim_path.exists():
            anim_path = None

        out_path = out_dir / base / f"{base}.bbmodel"
        bones, cubes, anim_count, tex_count = convert_single_model(geo_path, out_path, anim_path)
        print(f"Written: {out_path}")
        print(f"  {bones} bones, {cubes} cubes, {anim_count} animations, {tex_count} textures")
        total += 1

    print(f"Done. Converted {total} model(s) into {out_dir}")

def r2d(r):
    """Radians to degrees, trimmed."""
    return round(math.degrees(float(r)), 6)

def floats(s):
    """Parse comma-separated float literals (strips F/f suffix). Stops at non-numeric tokens."""
    result = []
    for x in s.split(','):
        x = x.strip().rstrip('Ff')
        try:
            result.append(float(x))
        except ValueError:
            break   # e.g. CubeDeformation.NONE — stop here
    return result


# ── 1. Parse geometry ─────────────────────────────────────────────────────────

def balanced_call(text, start):
    """Given text[start] == '(', return text[start:end+1] with balanced parens."""
    depth = 0
    for i in range(start, len(text)):
        if text[i] == '(':
            depth += 1
        elif text[i] == ')':
            depth -= 1
            if depth == 0:
                return text[start:i+1]
    return text[start:]


def parse_geometry(src):
    """
    Returns (tex_w, tex_h, parts).
    parts dict: name → {parent, offset[3], rot_deg[3], boxes[{uv[2], off[3], size[3]}]}
    parent == '__root__' means top-level (child of meshRoot).
    """
    m = re.search(r'LayerDefinition\.create\(mesh,\s*(\d+),\s*(\d+)\)', src)
    tw, th = (int(m.group(1)), int(m.group(2))) if m else (64, 64)

    flat = re.sub(r'\s+', ' ', src)

    var2part = {}   # java variable name → part name
    parts    = {}   # part name → data

    for m in re.finditer(
        r'(?:PartDefinition\s+(\w+)\s*=\s*)?(\w+)\.addOrReplaceChild\s*(\()',
        flat
    ):
        var_result  = m.group(1)   # new PartDefinition var (may be None)
        parent_var  = m.group(2)   # variable the method is called on
        call_start  = m.start(3)

        # Extract the balanced argument block for this call
        window = balanced_call(flat, call_start)

        # Part name — first string literal
        nm = re.match(r'\(\s*"(\w+)"', window)
        if not nm:
            continue
        part_name = nm.group(1)

        # Resolve parent
        if parent_var in ('meshRoot', 'mesh'):
            parent_name = '__root__'
        else:
            parent_name = var2part.get(parent_var, parent_var)

        if var_result:
            var2part[var_result] = part_name
        var2part[part_name] = part_name

        # Pose
        offset = [0.0, 0.0, 0.0]
        rot    = [0.0, 0.0, 0.0]
        pm = re.search(r'PartPose\.offsetAndRotation\(([^)]+)\)', window)
        if pm:
            vals   = floats(pm.group(1))
            offset = vals[:3]
            rot    = [r2d(vals[3]), r2d(vals[4]), r2d(vals[5])]
        else:
            pm = re.search(r'PartPose\.offset\(([^)]+)\)', window)
            if pm:
                offset = floats(pm.group(1))

        # Boxes — texOffs(u,v).addBox(ox,oy,oz,w,h,d[,deformation])
        boxes = []
        for bm in re.finditer(
            r'texOffs\((\d+),\s*(\d+)\)\s*\.addBox\(([^)]+)\)', window
        ):
            u, v   = int(bm.group(1)), int(bm.group(2))
            vals   = floats(bm.group(3))
            boxes.append({'uv': [u, v], 'off': vals[:3], 'size': vals[3:6]})

        parts[part_name] = {
            'parent': parent_name,
            'offset': offset,
            'rot':    rot,       # degrees, local rotation
            'boxes':  boxes,
        }

    return tw, th, parts


# ── 2. Parse animations ───────────────────────────────────────────────────────

def parse_animations(src):
    """
    Returns list of {name, length, loop, channels}.
    channels: {part_name: {target: [{time, xyz[3]}]}}
    """
    flat = re.sub(r'\s+', ' ', src)
    anims = []

    for am in re.finditer(
        r'AnimationDefinition\s+(\w+)\s*=\s*AnimationDefinition\.Builder\.withLength\(([\d.]+)F?\)',
        flat
    ):
        var_name = am.group(1)
        length   = float(am.group(2))
        start    = am.end()
        end      = flat.find('.build()', start)
        if end == -1:
            end = start + 4000
        block = flat[start:end]
        loop  = '.looping()' in block

        channels = {}
        for cm in re.finditer(
            r'\.addAnimation\(\s*"(\w+)",\s*new AnimationChannel\(\s*AnimationChannel\.Targets\.(\w+)',
            block
        ):
            part   = cm.group(1)
            target = cm.group(2).lower()   # ROTATION / POSITION / SCALE
            ch_block = block[cm.end(): cm.end() + 600]
            keyframes = []
            for kf in re.finditer(
                r'new Keyframe\(\s*([\d.]+)F?,\s*KeyframeAnimations\.\w+Vec\(([^)]+)\)',
                ch_block
            ):
                time = float(kf.group(1))
                xyz  = floats(kf.group(2))
                keyframes.append({'time': time, 'xyz': xyz[:3]})
            channels.setdefault(part, {})[target] = keyframes

        anims.append({
            'name':     var_name.lower(),
            'length':   length,
            'loop':     loop,
            'channels': channels,
        })

    return anims


# ── 3. Build .bbmodel ─────────────────────────────────────────────────────────

def build_bbmodel(model_name, tw, th, parts, anims, textures=None):

    # Compute absolute Java-space pivots (sum of offsets up the tree)
    abs_pivot = {}
    def compute_abs(name):
        if name in abs_pivot:
            return abs_pivot[name]
        p = parts[name]
        if p['parent'] == '__root__' or p['parent'] not in parts:
            abs_pivot[name] = list(p['offset'])
        else:
            par = compute_abs(p['parent'])
            abs_pivot[name] = [par[i] + p['offset'][i] for i in range(3)]
        return abs_pivot[name]

    for name in parts:
        compute_abs(name)

    # Assign stable UUIDs to each part (used as bone IDs in animations)
    part_uuid = {name: gen() for name in parts}

    # ── elements (cubes) ────────────────────────────────────────────────────
    elements = []
    part_element_uuids = {name: [] for name in parts}

    for name, pdata in parts.items():
        ax, ay, az = abs_pivot[name]
        bb_origin  = [ax, 24.0 - ay, az]

        for box in pdata['boxes']:
            ox, oy, oz = box['off']
            sx, sy, sz = box['size']

            # Java absolute corners
            jfx, jfy, jfz = ax + ox,      ay + oy,      az + oz
            jtx, jty, jtz = jfx + sx,     jfy + sy,     jfz + sz

            # BlockBench (Y flipped, lower/upper swap on Y)
            bb_from = [jfx,  24.0 - jty,  jfz]
            bb_to   = [jtx,  24.0 - jfy,  jtz]

            # Build box_uv faces manually
            u, v       = box['uv']
            w, h, d    = int(abs(sx)), int(abs(sy)), int(abs(sz))
            # Minecraft box_uv layout:
            #   east:  (u,       v+d)     → (u+d,     v+d+h)
            #   west:  (u+d+w,   v+d)     → (u+2d+w,  v+d+h)
            #   up:    (u+d,     v)       → (u+d+w,   v+d)
            #   down:  (u+d+w,   v)       → (u+2d+w,  v+d)
            #   north: (u+d,     v+d)     → (u+d+w,   v+d+h)
            #   south: (u+2d+w,  v+d)     → (u+2d+2w, v+d+h)
            faces = {
                "north": {"uv": [u+d,       v+d,     u+d+w,     v+d+h], "texture": 0},
                "south": {"uv": [u+2*d+w,   v+d,     u+2*d+2*w, v+d+h], "texture": 0},
                "east":  {"uv": [u,         v+d,     u+d,       v+d+h], "texture": 0},
                "west":  {"uv": [u+d+w,     v+d,     u+2*d+w,   v+d+h], "texture": 0},
                "up":    {"uv": [u+d,       v,       u+d+w,     v+d  ], "texture": 0},
                "down":  {"uv": [u+d+w,     v,       u+2*d+w,   v+d  ], "texture": 0},
            }

            elem_uuid = gen()
            part_element_uuids[name].append(elem_uuid)
            elements.append({
                "name":       name,
                "box_uv":     True,
                "from":       bb_from,
                "to":         bb_to,
                "rotation":   [0, 0, 0],   # rotation lives on the group/bone
                "origin":     bb_origin,
                "uv_offset":  [u, v],
                "faces":      faces,
                "type":       "cube",
                "uuid":       elem_uuid,
            })

    # ── outliner (groups / bones) ────────────────────────────────────────────
    def bb_rot(rx, ry, rz):
        """Convert Java degrees to BB degrees (negate X and Z for Y-flip)."""
        return [-rx, ry, -rz]

    def build_group(name):
        pdata  = parts[name]
        ax, ay, az = abs_pivot[name]
        rx, ry, rz = pdata['rot']
        children_parts = [n for n, p in parts.items() if p['parent'] == name]
        children = list(part_element_uuids[name])
        for child in children_parts:
            children.append(build_group(child))
        return {
            "name":     name,
            "origin":   [ax, 24.0 - ay, az],
            "rotation": bb_rot(rx, ry, rz),
            "uuid":     part_uuid[name],
            "export":   True,
            "isOpen":   True,
            "children": children,
        }

    top_level = [n for n, p in parts.items()
                 if p['parent'] == '__root__' or p['parent'] not in parts]
    outliner = [build_group(n) for n in top_level]

    # ── animations ───────────────────────────────────────────────────────────
    bb_anims = []
    for anim in anims:
        animators = {}
        for part_name, channels in anim['channels'].items():
            pid = part_uuid.get(part_name)
            if not pid:
                continue
            kf_list = []
            for target, keyframes in channels.items():
                for kf in keyframes:
                    x, y, z = kf['xyz']
                    if target == 'rotation':
                        # Same sign convention as geometry rotations
                        dp = {"x": str(-x), "y": str(y), "z": str(-z)}
                    elif target == 'position':
                        # Y delta: positive in Java = down; negate for BB Y-up
                        dp = {"x": str(x), "y": str(-y), "z": str(z)}
                    else:
                        dp = {"x": str(x), "y": str(y), "z": str(z)}
                    kf_list.append({
                        "channel":       target,
                        "data_points":   [dp],
                        "uuid":          gen(),
                        "time":          kf['time'],
                        "color":         -1,
                        "interpolation": "linear",
                    })
            animators[pid] = {
                "name":      part_name,
                "type":      "bone",
                "keyframes": kf_list,
            }
        bb_anims.append({
            "uuid":      gen(),
            "name":      f"animation.{model_name.lower()}.{anim['name']}",
            "loop":      "loop" if anim['loop'] else "once",
            "override":  False,
            "length":    anim['length'],
            "snapping":  24,
            "selected":  False,
            "saved":     False,
            "animators": animators,
        })

    return {
        "meta": {
            "format_version":  "5.0",
            "model_format":    "modded_entity",
            "box_uv":          True,
        },
        "name":                    model_name,
        "modded_entity_flip_y":    True,
        "modded_entity_version":   "Fabric 1.17+",
        "visible_box":             [1, 1, 0],
        "resolution":              {"width": tw, "height": th},
        "elements":                elements,
        "outliner":                outliner,
        "animations":  bb_anims,
        "textures":    textures if textures else [{
            "name":       f"{model_name.lower()}.png",
            "id":         "0",
            "width":      tw,
            "height":     th,
            "uv_width":   tw,
            "uv_height":  th,
            "particle":   False,
            "internal":   True,
            "saved":      False,
            "uuid":       gen(),
            "source":     "",
        }],
    }


# ── main ──────────────────────────────────────────────────────────────────────

def main():
    args = sys.argv[1:]

    # Default: repository-specific batch conversion with no extra scripts.
    if not args:
        run_auto_mode()
        return

    if args[0] == '--auto':
        run_auto_mode()
        return

    # Optional: --textures <dir>  to override auto-detection
    tex_override = None
    if '--textures' in args:
        i = args.index('--textures')
        tex_override = Path(args[i+1])
        args = args[:i] + args[i+2:]

    if len(args) < 2:
        print(__doc__)
        sys.exit(1)

    geo_path = Path(args[0])

    # Distinguish: 2 args = no animations file,  3 args = with animations
    if len(args) == 2:
        anim_path = None
        out_path  = Path(args[1])
    else:
        anim_path = Path(args[1])
        out_path  = Path(args[2])

    bones, cubes, anim_count, tex_count = convert_single_model(geo_path, out_path, anim_path, tex_override)
    print(f"Written: {out_path}")
    print(f"  {bones} bones, {cubes} cubes, {anim_count} animations, {tex_count} textures")


if __name__ == '__main__':
    main()
