# Modifiers
Modifiers come in one of six rarities, [Common](https://github.com/TheDarkTurnip/dark-dungeon/blob/master/algorithms/rarity/common.md), [Uncommon](https://github.com/TheDarkTurnip/dark-dungeon/blob/master/algorithms/rarity/uncommon.md), [Rare](https://github.com/TheDarkTurnip/dark-dungeon/blob/master/algorithms/rarity/rare.md), [Epic](https://github.com/TheDarkTurnip/dark-dungeon/blob/master/algorithms/rarity/epic.md), [Master](https://github.com/TheDarkTurnip/dark-dungeon/blob/master/algorithms/rarity/master.md), and [Legendary](https://github.com/TheDarkTurnip/dark-dungeon/blob/master/algorithms/rarity/legendary.md).
There are currently only two ways of obtaining a new modifier. 
1. Find a new item in a chest. This form uses the [Weighted Random Method](#weighted-random-method) to determine the modifer.
2. Reroll an item in the Item-Reroller (in the endgame area). This form uses the [Minimum Rounded Weighted Random Method](#minimum-rounded-weighted-random-method) to determine the modifier. 

## Weighted Random Method
This method retrieves sums up the weights of all rarity values, and then generates a random number between 0 and the sum. It then loops through all the values, adding to a counter variable each weight. When the counter variable reaches a value that is higher than the random number generated it returns it. This process is repeated until an item finds a compatible modifier. 
``` java
double completeWeight = 0.0;
for (Modifiers modifier : Modifiers.values()) {
    completeWeight += modifier.getWeight();
}
double r = Math.random() * completeWeight;
double countWeight = 0.0;
for (Modifiers modifier : Modifiers.values()) {
    countWeight += modifier.getWeight();
    if (countWeight >= r) {
        return modifier;
    }
}
```
## Minimum Rounded Weighted Random Method
This method is similar to the `weighted random method`, however, it first sets all modifiers with a weight less than 0, to 1, and while looping through the values, excludes all with a `weight > 6`.
```java
int weightCutoff = 6;
double completeWeight = 0.0;
for (Modifiers modifier : Modifiers.values()) {
    if (modifier.getWeight() > weightCutoff) continue;
    if (modifier.getWeight() <= 0) {
        completeWeight += 1;
    } else {
        completeWeight += modifier.getWeight();
    }
}
double r = Math.random() * completeWeight;
double countWeight = 0.0;
for (Modifiers modifier : Modifiers.values()) {
    if (modifier.getWeight() > weightCutoff) continue;
    if (modifier.getWeight() <= 0) {
        countWeight += 1;
    } else {
        countWeight += modifier.getWeight();
    }
    if (countWeight >= r) {
        return m;
    }
}
```
## Weights
This is the source code that determines the weight of ALL modfiers:
``` java
switch (this) {
case FLEETING:
case HARD:
case PRECISE:
case SIGHTED:
case STABLE:
case JAGGED:
	return 10;
case GUARDING:
case HASTY:
case SPIKED:
case MYSTIC:
case LARGE:
	return 8;
case ANGRY:
case ARMORED:
case LUCKY:
case STURDY:
case STAUNCH:
case MANIC:
	return 7;
case MENACING:
case QUICK:
case CELESTIAL:
case MASSIVE:
case WARDING:
	return 6;
case MYTHICAL:
case ZEALOUS:
case RUTHLESS:
	return 3;
case DEMONIC:
	return 2;
case GODLY:
	return 1;
case WRITABLE:
	return 0;
default: return 10;
}
```
## Modifier Effects
### Fleeting
Increases the `movement speed` by 0.02
### Hard
Increases the `protection` by 0.5
### Precise
Increases the `luck` by 0.5
### Sighted
Increases the `damage on bows` by 1.0
### Stable
Increases the `durability` by 100
### Jagged
Increases the `damage on swords` by 1.0
### Guarding
Increases the `protection` by 1.0
### Hasty
Increases the `movement speed` by 0.04
### Spiked
Increases the `damage on swords` by 2.0
### Mystic
Increases the `damage on staffs` by 1.5
### Large
Increases the `range on staffs` by 1.0
### Angry
Increases the `damage on swords` by 3.0
### Armored
Increases the `protection` by 1.5
### Lucky
Increases the `luck` by 1.0
### Sturdy
Increases the `durability` by 200
### Staunch
Increases the `damage on bows` by 2.0
### Manic
Increases the `damage on staffs` by 2.5
### Menacing
Increases the `damage on swords` by 4.0
### Quick
Increases the `movement speed` by 0.06
### Celestial
Increases the `damage on staffs` by 3.5
### Massive
Increases the `range on staffs` by 2.0
### Warding
Increases the `protection` by 2.0
### Mythical
Increases the `damage on staffs` by 5.0
### Zealous
Increases the `movement speed` by 0.06

Increases the `protection` by 2.0

Increases the `damage on bows` by 3.0

Increases the `durability` by 150

Increases the `damage on swords` by 5.0

Increases the `range on staffs` by 4.0

Increases the `damage on staffs` by 5.5
### Ruthless
Increases the `movement speed` by 0.06
Increases the `luck` by 0.25

Increases the `damage on bows` by 3.0

Increases the `damage on swords` by 5.0

Increases the `range on staffs` by 3.0

Increases the `damage on staffs` by 5.5
### Demonic
Increases the `movement speed` by 0.04

Increases the `luck` by 0.25

Increases the `damage on bows` by 5.0

Increases the `durability` by 150

Increases the `damage on swords` by 6.0

Increases the `range on staffs` by 3.0

Increases the `damage on staffs` by 6.75
### Godly
Increases the `movement speed` by 0.1

Increases the `protection` by 4.0

Increases the `luck` by 0.75

Increases the `damage on bows` by 7.0

Increases the `durability` by 300

Increases the `damage on swords` by 7.0

Increases the `range on staffs` by 5.0

Increases the `damage on staffs` by 7.0
### Writable
Increases the `tokens` to 3.0

