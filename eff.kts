#!/usr/bin/env kscript

@file:DependsOn("com.github.holgerbrandl:kscript-support:1.2.4")

import kscript.util.*
import java.nio.file.FileSystems
import java.util.concurrent.TimeUnit

val usage = """easy-ffmpeg: A wrapper around ffmpeg for converting image sequences to video files.

Usage:
  eff.kts [--rate=<rt> -e <ext> --crf=<crf> -p <p> -- -o <output> <images_dir>] 
  eff.kts [-h]


Options:
  -h --help            Show this screen.
  -r <rt> --rate=<rt>  Framerate [default: 30].
  -e <ext> --extension=<ext>  Image file's extension [default: .png].
  --crf=<crf>  Lower indicates better quality. 0-50?: Int. See ffmpeg for more details. [default: 12]
  -o <output> --out=<output>  Output file [default: happy.mp4].
  -p <p> --preset=<p>  Preset [default: veryslow]. 
  <images_dir> The directory where images are located.

  Created by Fereidoon Mehri.
"""

val myOpts = DocOpt(args, usage)
println(myOpts.parsedArgs)
var possiblePwd = FileSystems.getDefault().getPath(myOpts.parsedArgs["<images_dir>"]?.toString() ?: "").toAbsolutePath().toFile()
if (possiblePwd.isDirectory == false)
possiblePwd = possiblePwd.parentFile

//val cmd = """ffmpeg -pattern_type glob -i '${possiblePwd.toString()}/*${myOpts.getString("extension")}' -r ${myOpts.getInt("rate")} -pix_fmt yuv420p  -c:v libx264 -preset ${myOpts.getString("preset")} -crf ${myOpts.getInt("crf")} '${possiblePwd.toString()}/${myOpts.getString("out")}'"""

ProcessBuilder("ffmpeg", "-pattern_type", "glob", "-i", "$possiblePwd/*${myOpts.getString("extension")}", "-r", "${myOpts.getNumber("rate")}", "-pix_fmt", "yuv420p", "-c:v", "libx264", "-preset", "${myOpts.getString("preset")}", "-crf", "${myOpts.getInt("crf")}", "$possiblePwd/${myOpts.getString("out")}")
    .directory(possiblePwd)
    .redirectInput(ProcessBuilder.Redirect.INHERIT)
    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
    .redirectError(ProcessBuilder.Redirect.INHERIT)
    .start()
    .waitFor(90, TimeUnit.DAYS)
