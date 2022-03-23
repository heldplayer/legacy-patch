# Legacy Patch

[Link back to the main list](https://github.com/heldplayer/legacy-patch/tree/master)

## Instructions for building

This version is being built using IntelliJ IDEA.

The following libraries are required to build it:
- [asm-debug-all:4.1](https://mvnrepository.com/artifact/org.ow2.asm/asm-debug-all/4.1)
- [guava:14.0.1](https://mvnrepository.com/artifact/com.google.guava/guava/14.0.1)
- launchwrapper:1.8
- forge-1.7.10-10.13.4.1614-1.7.10-universal.jar (get it from [MinecraftForge](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.7.10.html))

Suggested is putting these in the `lib` folder, then adding them in IDEA in the Project Structure under Libraries.

Afterwards, building is as simple as building the artifact in Build > Build Artifacts... then legacy-patch > Build.

## Minecraft classes

Note that it this method does not allow compiling anything making use of Minecraft class names, as this is a simple copy of the 1.6.4 branch.

It might be possible to get a dev workspace in 1.7.10, but right now this is not necessary so no effort has been put in.
