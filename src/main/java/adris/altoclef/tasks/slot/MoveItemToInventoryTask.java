package adris.altoclef.tasks.slot;

import adris.altoclef.AltoClef;
import adris.altoclef.tasksystem.Task;
import adris.altoclef.util.ItemTarget;

public class MoveItemToInventoryTask extends Task {

    private final ItemTarget _toMove;

    public MoveItemToInventoryTask(ItemTarget toMove) {
        _toMove = toMove;
    }


    @Override
    protected void onStart(AltoClef mod) {

    }

    @Override
    protected Task onTick(AltoClef mod) {
        return null;
    }

    @Override
    protected void onStop(AltoClef mod, Task interruptTask) {

    }

    @Override
    protected boolean isEqual(Task obj) {
        return false;
    }

    @Override
    protected String toDebugString() {
        return null;
    }
}