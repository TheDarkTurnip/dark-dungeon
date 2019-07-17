# Modifiers

Modifiers come in one of six rarities, [Common](https://github.com/TheDarkTurnip/dark-dungeon/blob/items/algorithms/rarity/common.md), [Uncommon](https://github.com/TheDarkTurnip/dark-dungeon/blob/items/algorithms/rarity/uncommon.md), [Rare](https://github.com/TheDarkTurnip/dark-dungeon/blob/items/algorithms/rarity/rare.md), [Epic](https://github.com/TheDarkTurnip/dark-dungeon/blob/items/algorithms/rarity/epic.md), [Master](https://github.com/TheDarkTurnip/dark-dungeon/blob/items/algorithms/rarity/master.md), and [Legendary](https://github.com/TheDarkTurnip/dark-dungeon/blob/items/algorithms/rarity/legendary.md).

There are currently only two ways of obtaining a new modifier. 

1. Find a new item in a chest. This form uses the [Weighted Random Method](#weighted-random-method) to determine the modifer.
2. Reroll an item in the Item-Reroller (in the endgame area). This form uses the [Minimum Rounded Weighted Random Method](#minimum-rounded-weighted-random-method) to determine the modifier. 

## Weighted Random Method

This method retrieves sums up the weights of all rarity values, and then generates a random number between 0 and the sum. It then loops through all the values, adding to a counter variable each weight. When the counter variable reaches a value that is higher than the random number generated it returns it. This process is repeated until an item finds a compatible modifier. 

The source:

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

