package de.glowman554.framework.client.mod.impl;

import de.glowman554.config.auto.Saved;
import de.glowman554.framework.client.config.Configurable;
import de.glowman554.framework.client.mod.Mod;

import java.util.Random;

public class ModTips extends Mod {
    private final Random random = new Random();
    @Saved
    @Configurable(text = "Tips")
    private final String[] tips = new String[]{
            "Place a bed in the nether, it'll be a blast!",
            "Drink milk to get rid of potion effects!",
            "Iron Golems are the protectors of villages, they will attack you if you attack a villager.",
            "Sugarcane can grow on dirt!",
            "Digging straight down has a 75% chance of finding a surprise.",
            "Creepers are scared of cats!",
            "Don't look an enderman in the eyes, or they may get upset.",
            "§4Fear the steve whose eyes §fglow white.",
            "You can ring a bell during a raid to make nearby illagers glow.",
            "You can shear a snow golem to remove the jack o' lantern.",
            "Wearing a mob head will make it hard for them to detect you, try it out with a creeper!",
            "Basalt can be made from lava flowing onto a space on top of soul soil and adjacent to blue ice.",
            "Lightning can transform a red mooshroom into a brown mooshroom.",
            "You can rename a axolotl in a bucket, and it will keep the name when you place it!",
            "Cursed armor bound to you? Die to remove it!",
            "When lightning strikes a creeper, something electrifying happens!",
            "Don't forget to wear gold around piglins, or they will become angered.",
            "Hoglins fear warped fungus.",
            "Build a portal out of glowstone to bring you to a world above the clouds.",
            "Evokers can change turn blue sheep red.",
            "Name a mob dinnerbone and see what happens!",
            "Name a sheep 'jeb_' and see what happens!",
            "Flooded caves are a great source of ores!",
            "Goat horns makes the same sound as the illager raid horns.",
            "Be careful if you have bad omen, you may face a bad time in a village if you do.",
            "Some say that pigs and creepers have a common ancestor.",
            "Villagers with a green outfit may have trouble finding a job.",
            "If you're lucky while killing wither skeletons, you may just get a skull.",
            "When lightning strikes a villager, it will transform into a witch.",
            "Fireworks can be loaded into a crossbow.",
            "If an enderman is mad at you, look them in the eyes to prevent moving.",
            "Strongholds generate in rings around the center of the world.",
            "Pink sheep can very rarely spawn.",
            "The mending enchantment can fix your tools at the cost of experience.",
            "Fishing can be a good source of both food and loot!",
            "Axolotls can protect you from the dangers of the sea.",
            "Diamonds are most easily found deep in the deepslate layer of the world.",
            "Deepslate was originally called grimstone!",
            "You can collect the breath of the ender dragon with just a bottle!",
            "Powdered snow can accumulate in a cauldron while it's snowing.",
            "Be careful of powdered snow, you can fall right through it and begin freezing.",
            "Minecraft bee is trans.",
            "Don't forget to sleep, or the phantoms may awaken.",
            "If a skeleton freezes to death, it will become a stray.",
            "If a zombie drowns, it will become a drowned.",
            "The trident is a weapon that excels in the water.",
            "Polar bears will protect their cubs.",
            "Pandas have 6 different personality types, Normal, Lazy, Worried, Playful, Aggressive, Weak, and Brown",
            "Use glow ink on a sign to make the text glow.",
            "The phase of the moon impacts slimes spawning in swamps, they spawn the most during the full moon.",
            "Dig under an azalea tree, and you will find a lush cave.",
            "Zombies can turn villagers into zombies.",
            "Put a campfire under a bee hive to pacify the bees and safely collect honey.",
            "Be careful around goats, as they may ram into you.",
            "You can breed hoglins with crimson mushrooms.",
            "Both hoglins and piglins will zombify when they leave the nether.",
            "If you travel enough, you may find an island with giant mushrooms.",
            "Use striders to travel across the vast lava oceans in the nether. They can be lead with a warped mushroom on a stick.",
            "Elder Guardians will give you mining fatigue when you get too close to an ocean monument.",
            "Elder Guardians will drop sponges on death.",
            "When in a piglin bastion, piglins will defend their loot.",
            "Piglins Brutes don't care about gold, and will attack no matter what.",
            "End Cities are found throughout the outer end islands, and they contain some rare loot.",
            "Put arrows around a lingering potion and you get tipped arrows.",
            "Horses are a fast way to travel around the world, and you can breed them to get better stats.",
            "Once you are below y=0, all stone is replaced by deepslate, which is stronger.",
            "Large ore veins of copper and iron can be found deep underground.",
            "Both the top and bottom of the nether has bedrock, an unbreakable block.",
            "Legends say there are ancient cities deep underground, with rare treasures found nowhere else.",
            "Swift Sneak is an enchantment that boosts your movement while sneaking.",
            "Don't bring your magma cubes near a frog, or it may just get eaten.",
            "Sculk contains souls, and will drop experience when you break it.",
            "Give an allay an item, and they will collect more for you!",
            "Frogs have three types, Cold, Warm, and Temperate, that can be bred in their respective climates.",
            "Sculk Catalyst will steal the soul of closely killed mobs, and use it to spread sculk",
            "Use wool to prevent sounds from echoing down in the deep dark",
            "You can breed frogs with slimeballs.",
            "They say a strong beast with echoing power exists in the deepest parts of the world.",
            "Dripstone under mud can dry it out until it becomes mud.",
            "If you give an allay and amethyst shard while it's dancing to music, it can duplicate",
            "Trading with villagers is a great way to get emeralds.",
            "Be quiet around sculk shriekers, or you will regret it.",
            "If you place two blocks of snow with a jack o' lantern atop them, you can build a snowman!",
            "Leather boots prevent you from falling through powdered snow!",
            "If a shulker is hit by a shulker bullet, there is a chance it duplicates!",
            "Some materials get enchantments better, like gold.",
            "Try to eat larger foods, like cooked meats, they keep you filled for longer.",
    };

    @Override
    public String getId() {
        return "tips";
    }

    @Override
    public String getName() {
        return "Tips";
    }

    @Override
    public boolean defaultEnable() {
        return true;
    }

    public String getRandomTip() {
        return tips[random.nextInt(tips.length)];
    }

    @Override
    public boolean isHacked() {
        return false;
    }
}
