/*
 * Copyright 2026 Lawnchair contributors
 * Licensed under the Apache License, Version 2.0.
 */
package app.lawnchair.hotseat;

/** Shared mapping between persistent dock ranks and orientation-specific cells. */
public final class DockGrid {
    private DockGrid() { }

    public static int cellX(int rank, int columns, boolean vertical) {
        return vertical ? rank / columns : rank % columns;
    }

    public static int cellY(int rank, int columns, boolean vertical) {
        return vertical ? columns - rank % columns - 1 : rank / columns;
    }

    public static int rank(int x, int y, int columns, boolean vertical) {
        return vertical ? x * columns + columns - y - 1 : y * columns + x;
    }
}
