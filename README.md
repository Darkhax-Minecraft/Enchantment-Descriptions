# Enchantment-Descriptions [![](http://cf.way2muchnoise.eu/250419.svg)](https://minecraft.curseforge.com/projects/enchantment-descriptions) [![](http://cf.way2muchnoise.eu/versions/250419.svg)](https://minecraft.curseforge.com/projects/enchantment-descriptions)
Displays descriptions about an enchantment on the tooltip.

## Adding support

This mod uses a simple format for enchantment descriptions. You can add support for this mod and others using the format by adding a translation key to your language file. The format is `enchantment.MODID.ENDID.desc=`.

## Maven Dependency
If you are using [Gradle](https://gradle.org) to manage your dependencies, add the following into your `build.gradle` file. Make sure to replace the version with the correct one. All versions can be viewed [here](https://maven.blamejared.com/net/darkhax/enchdesc/).
```
repositories {

    maven {
    
        url 'https://maven.blamejared.com'
    }
}

dependencies {

    // Example: compile "net.darkhax.enchdesc:EnchantmentDescriptions-1.16.4:5.0.4"
    compile "net.darkhax.enchdesc:EnchantmentDescriptions-MCVERSION:PUT_FILE_VERSION_HERE"
}
```

## Jar Signing

As of January 11th 2021 officially published builds will be signed. You can validate the integrity of these builds by comparing their signatures with the public fingerprints.

| Hash   | Fingerprint                                                        |
|--------|--------------------------------------------------------------------|
| MD5    | `12F89108EF8DCC223D6723275E87208F`                                 |
| SHA1   | `46D93AD2DC8ADED38A606D3C36A80CB33EFA69D1`                         |
| SHA256 | `EBC4B1678BF90CDBDC4F01B18E6164394C10850BA6C4C748F0FA95F2CB083AE5` |


## Sponsors
<img src="https://nodecraft.com/assets/images/logo-dark.png" width="384" height="90">

This project is sponsored by Nodecraft. Use code [Darkhax](https://nodecraft.com/r/darkhax) for 30% off your first month of service!