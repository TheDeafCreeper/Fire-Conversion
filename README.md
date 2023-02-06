# Fire Conversion
Allows you to convert items from one to another using fire.

# Configuration
The only configeration file is conversions.yml
Every conversion uses one of the following setups:

```yaml
# Material names can be looked up @ https://jd.papermc.io/paper/1.19/org/bukkit/Material.html

# Simple Conversion
ConversionName: # Can be whatever you want, needs to be unique though.
  sources: # A list of all source materials (what you throw into the fire)
    - MATERIAL_1
    - MATERIAL_2
  result:
    normal: MATERIAL # The result of throwing one of the sources into normal (orange) fire.
    soul: MATERIAL # The result of throwing one of the sources into soul (blue) fire.
    #You can define an amount of material by doing MATERIAL-AMOUNT, it defaults to 1.

# Complex Conversion
ConversionName:
  sources:
    - MATERIAL_1
    - MATERIAL_2
  result:
    normal:
      - RESULT_1-AMOUNT-WEIGHT # The weight is optional, and defaults to 1
      - RESULT_2-AMOUNT-WEIGHT
    soul:
      - RESULT_1-AMOUNT-WEIGHT
```

Weight determines the odds of that result being picked, for example with the following conversion:
```yaml
DiamondOreToDiamond:
  sources:
    - DIAMOND_ORE
    - DEEPSLATE_DIAMOND_ORE
  result:
    normal:
      - DIAMOND-1-10 # This would have a 71.4% chance of dropping 1
      - DIAMOND-2-3 # 21.4% chance of dropping 2
      - DIAMOND-3-1 # and 7.1% chance of dropping 3
```
You can figure out the precentage from the weight by doing WEIGHT/TOTAL_WEIGHT, for example 10/(10+3+1) = 0.714... (x100 for the percentage)
You can also leave one of the results as NONE (or any other invalid material), but must have at least 1 source and 1 result.

If a source shows up multiple times, then it will use the results for the last conversion in the list.

You CANNOT use the following materials as a result:
- AIR
- CAVE_AIR
- VOID_AIR
