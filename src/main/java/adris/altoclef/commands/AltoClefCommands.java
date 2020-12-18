package adris.altoclef.commands;

import adris.altoclef.AltoClef;
import adris.altoclef.Debug;
import adris.altoclef.tasks.*;
import adris.altoclef.tasks.resources.CollectPlanksTask;
import adris.altoclef.tasksystem.Task;
import adris.altoclef.util.CraftingRecipe;
import adris.altoclef.util.ItemTarget;
import adris.altoclef.util.RecipeTarget;
import adris.altoclef.util.baritone.PlaceBlockNearbySchematic;
import adris.altoclef.util.slots.PlayerSlot;
import adris.altoclef.util.TaskCatalogue;
import baritone.api.utils.BlockOptionalMeta;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import sun.security.util.ArrayUtil;

import java.util.Arrays;

/// This structure was copied from a C# project. Fuck java. All my homies hate java.
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class AltoClefCommands extends CommandList {

    private static void TEMP_TEST_FUNCTION(AltoClef mod, String arg) {
        //mod.runUserTask();
        Debug.logMessage("Running test...");

        /*
        mod.getBlockTracker().trackBlock(Blocks.CRAFTING_TABLE);
        BlockPos target = mod.getBlockTracker().getNearestTracking(mod.getPlayer().getPos());

        mod.getCustomBaritone().getInteractWithBlockPositionProcess().getToBlock(target, true);
        */
        //mod.runUserTask(new PickupDroppedItemTask(Arrays.asList(new ItemTarget(ItemTarget.LOG))));
        //mod.runUserTask(new MineAndCollectTask(Arrays.asList(new ItemTarget(ItemTarget.LOG))));
        ItemTarget B = new ItemTarget("planks");
        ItemTarget s = new ItemTarget("stick");
        ItemTarget o = null;
        CraftingRecipe testRecipe = CraftingRecipe.newShapedRecipe("wooden_pickaxe",new ItemTarget[]{B, B, B, o, s, o, o, s, o});
        ItemTarget targetItem = new ItemTarget(Items.WOODEN_PICKAXE, 1);
        CraftingRecipe testRecipe2 = CraftingRecipe.newShapedRecipe("wooden_sword",new ItemTarget[]{ o, B, o, o, B, o, o, s, o});
        ItemTarget targetItem2 = new ItemTarget(Items.WOODEN_SWORD, 1);


        if (arg.equals("")) {
            //mod.runUserTask(TaskCatalogue.getItemTask("crafting_table", 1));
            mod.runUserTask(new CraftInTableTask(targetItem, testRecipe));

        } else if (arg.equals("both")) {
            mod.runUserTask(new CraftInTableTask(
                    Arrays.asList(new RecipeTarget(targetItem2, testRecipe2), new RecipeTarget(targetItem, testRecipe))
            ));
        } else if (arg.equals("place")) {
            PlaceBlockNearbySchematic schematic = new PlaceBlockNearbySchematic(Blocks.CRAFTING_TABLE);
            schematic.reset();

            Vec3i origin = mod.getPlayer().getBlockPos();
            mod.getClientBaritone().getBuilderProcess().build("Place crafting table nearby", schematic, origin);
        } else if (arg.equals("placereal")) {
            mod.runUserTask(new PlaceBlockNearbyTask(Blocks.CRAFTING_TABLE));
        } else if (arg.equals("craft")) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        for (int i = 5; i > 0; --i) {
                            Debug.logMessage(i + "...");
                            Thread.sleep(1000, 0);
                        }
                        Debug.logMessage("DOING THE THING");
                        mod.getInventoryTracker().craftInstant(testRecipe);
                        Thread.sleep(1000, 0);
                        mod.getInventoryTracker().craftInstant(testRecipe2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else if (arg.equals("table")) {
            mod.runUserTask(TaskCatalogue.getItemTask("crafting_table", 1));
        }

        //mod.getBlockTracker().trackBlock(Blocks.CRAFTING_TABLE);
        //BlockPos target = mod.getBlockTracker().getNearestTracking(mod.getPlayer().getPos());

        //mod.runUserTask(new GetToBlockTask(target, true));

        //mod.runUserTask(new PlaceBlockNearbyTask(Blocks.CRAFTING_TABLE));
    }

    public AltoClefCommands(CommandExecutor executor) throws CommandException {
        super(executor,
            // List commands here
            new HelpCommand(),
            new GetCommand(),
            new StopCommand(),
            new TestCommand()
            //new TestMoveInventoryCommand(),
            //    new TestSwapInventoryCommand()
        );
    }

    static class HelpCommand extends Command {

        public HelpCommand() {
            super("help", "Lists all commands");
        }

        @Override
        protected void Call(AltoClef mod, ArgParser parser) {
            Debug.logMessage("########## HELP: ##########");
            int padSize = 10;
            for(Command c : mod.getCommandExecutor().AllCommands()) {
                StringBuilder line = new StringBuilder();
                //line.append("");
                line.append(c.getName());
                int toAdd = padSize - c.getName().length();
                for (int i = 0; i < toAdd; ++i) {
                    line.append(" ");
                }
                line.append(" ");
                line.append(c.getDescription());
                Debug.logMessage(line.toString());
            }
            Debug.logMessage("###########################");
        }
    }

    static class StopCommand extends Command {

        public StopCommand() {
            super("stop", "Stop task runner (stops all automation)");
        }

        @Override
        protected void Call(AltoClef mod, ArgParser parser) {
            mod.getTaskRunner().disable();
        }
    }

    static class GetCommand extends Command {

        public GetCommand() throws CommandException {
            super("get", "Get an item/resource",
                    new Arg(String.class, "name"),
                    new Arg(Integer.class, "count", 1, 1));
        }

        @Override
        protected void Call(AltoClef mod, ArgParser parser) throws CommandException {
            String resourceName = parser.Get(String.class);
            int count = parser.Get(Integer.class);

            Task targetTask = TaskCatalogue.getItemTask(resourceName, count);
            if (targetTask == null) {
                Debug.logWarning("\"" + resourceName + "\" is not a catalogued resource. Can't get it yet, sorry! If it's a generic block try using baritone.");
                Debug.logWarning("Here's a list of everything we can get for you though:");
                Debug.logWarning(Arrays.toString(TaskCatalogue.resourceNames().toArray()));
            } else {
                mod.runUserTask(targetTask);
            }
        }
    }

    static class TestCommand extends Command {

        public TestCommand() throws CommandException {
            super("test", "Generic command for testing", new Arg(String.class, "extra", "", 0));
        }

        @Override
        protected void Call(AltoClef mod, ArgParser parser) throws CommandException {
            TEMP_TEST_FUNCTION(mod, parser.Get(String.class));
        }
    }

    static class TestMoveInventoryCommand extends Command {

        public TestMoveInventoryCommand() throws Exception {
            super("testmoveinv", "Test command to move items around in inventory",
                    new Arg(Integer.class, "from"),
                    new Arg(Integer.class, "to"),
                    new Arg(Integer.class, "amount", 1, 2)
            );
        }

        @Override
        protected void Call(AltoClef mod, ArgParser parser) throws CommandException {
            int from = parser.Get(Integer.class);
            int to = parser.Get(Integer.class);
            int amount = parser.Get(Integer.class);

            int moved = mod.getInventoryTracker().moveItems(new PlayerSlot(from), new PlayerSlot(to), amount);
            Debug.logMessage("Successfully moved " + moved + " items.");
        }
    }
    static class TestSwapInventoryCommand extends Command {

        public TestSwapInventoryCommand() throws CommandException {
            super("testswapinv", "Test command to swap two slots in the inventory",
                    new Arg(Integer.class, "slot1"),
                    new Arg(Integer.class, "slot2")
            );
        }

        @Override
        protected void Call(AltoClef mod, ArgParser parser) throws CommandException {
            int slot1 = parser.Get(Integer.class);
            int slot2 = parser.Get(Integer.class);

            mod.getInventoryTracker().swapItems(new PlayerSlot(slot1), new PlayerSlot(slot2));
            Debug.logMessage("Successfully swapped.");
        }
    }
}