# Fire Conversion
Allows you to convert items from one to another using fire.

# Configuration
The only configeration file is conversions.yml
Every conversion uses the following setup:

```yaml
# Material names can be looked up @ https://jd.papermc.io/paper/1.19/org/bukkit/Material.html
ConversionName: # Can be whatever you want, needs to be unique though.
  sources: # A list of all source materials
    - MATERIAL_1
    - MATERIAL_2
    ...
  result:
    normal: MATERIAL # The result of throwing one of the sources into normal (orange) fire.
    soul: MATERIAL # The result of throwing one of the sources into soul (blue) fire.
```

You can leave one of the results blank (or as NONE for clarity), but must have at least 1 source and 1 result.

If a source shows up multiple times, then it will use the results for the last conversion in the list.
