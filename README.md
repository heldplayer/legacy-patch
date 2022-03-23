# Legacy Patch

[Link back to the main list](https://github.com/heldplayer/legacy-patch/tree/master)

## Instructions for building

This version is being built using IntelliJ IDEA.

The following libraries are required to build it:
- [asm-debug-all:4.1](https://mvnrepository.com/artifact/org.ow2.asm/asm-debug-all/4.1)
- [guava:14.0.1](https://mvnrepository.com/artifact/com.google.guava/guava/14.0.1)
- launchwrapper:1.8
- fml-universal:1.6.4-6.4.49.1-1.6.4 (you may need to compile this manually)

Suggested is putting these in the `lib` folder, then adding them in IDEA in the Project Structure under Libraries.

Afterwards, building is as simple as building the artifact in Build > Build Artifacts... then legacy-patch > Build.

## Minecraft classes

Note that it this method does not allow compiling anything making use of Minecraft class names, the reason for this is that I did not manage get a working 1.6.4 dev instance, even after trying to massage Gradle a whole bunch.
Unfortunately this may be a result of the way ForgeGradle worked back in the day, and there may be no way to get it working other than compiling a custom version of ForgeGradle itself based on the original source from then.

For now, this is too much effort for something that is not required. If the need arises I'd be grateful for somebody to take a look into making it possible.
